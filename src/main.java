import java.sql.*;
import java.util.Scanner;

public class main {

    private static Scanner scanner = new Scanner( System.in );

    public static void main(String[] args) throws SQLException {
        int version = -1;

        // If no username or password is entered
        if (args.length < 2) {
            System.out.println("ERROR: PROGRAM REQUIRES A USERNAME AND PASSWORD!!!");
            System.exit(-1);
        }

        // 0. If no additional arguments are entered
        else if (args.length == 2) {
            System.out.println("\n-------------------------------------------------");
            System.out.println("1 - Report Patient Information");
            System.out.println("2 – Report Primary Care Physician Information");
            System.out.println("3 – Report Operation Information");
            System.out.println("4 – Update Patient Blood Type");
            System.out.println("5 – Exit Program");
            System.out.println("-------------------------------------------------\n");
            System.exit(-1);
        }

        // If the username, password, and a value are entered
        else if (args.length == 3) {
            try {
                version = Integer.parseInt(args[2]);
            } catch (Exception e) {
                System.out.println("OPTION VALUE HAS TO BE AN INTEGER");
                e.printStackTrace();
                System.exit(-1);
            }
        } else {
            System.out.println("INVALID NUMBER OF PARAMETERS!!!");
            System.exit(-1);
        }

        String USERID = args[0];
        String PASSWORD = args[1];

        System.out.println("-------Oracle JDBC COnnection Testing ---------");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

        } catch (ClassNotFoundException e){
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
            return;
        }

        System.out.println("Oracle JDBC Driver Registered!");
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@csorcl.cs.wpi.edu:1521:orcl", USERID, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }
        System.out.println("Oracle JDBC Driver Connected!\n");

        String userInput;
        switch (version) {
            case 1:
                // Report Patient Information
                System.out.println("Entering \"Report Patient Information\" Mode");
                System.out.println("--------------------------------------------------");
                System.out.print("Enter Patient’s Healthcare ID: ");
                userInput = scanner.nextLine();
                // Performing the query
                try {
                    Statement stmt = connection.createStatement();
                    String str = "SELECT * FROM PATIENT WHERE HEALTHCAREID =" + userInput;
                    ResultSet rset = stmt.executeQuery(str);

                    if (!rset.isBeforeFirst()) {
                        System.out.println("NO DATA FOUND!!!");

                    } else {
                        int healthID = 0;
                        String firstName, lastName, city, state, birthDate, bloodType;
                        firstName = lastName = city = state = birthDate = bloodType = "";
                        // Process the results
                        while (rset.next()) {
                            healthID = rset.getInt("healthCareID");
                            firstName = rset.getString("firstName");
                            lastName = rset.getString("lastName");
                            city = rset.getString("city");
                            state = rset.getString("state");
                            birthDate = rset.getString("birthDate");
                            bloodType = rset.getString("bloodType");
                        } // end while

                        System.out.println("\n-------------------------------------------------");
                        System.out.println("Patient Information:");
                        System.out.println("Healthcare ID: " + healthID);
                        System.out.println("First Name: " + firstName);
                        System.out.println("Last Name: " + lastName);
                        System.out.println("City: " + city);
                        System.out.println("State: " + state);
                        System.out.println("Birth Date: " + birthDate);
                        System.out.println("Blood Type: " + bloodType);
                        System.out.println("-------------------------------------------------\n");

                        rset.close();
                        stmt.close();
                        connection.close();
                    }

                } catch (SQLException e) {
                    System.out.println("Get Data Failed! Check output console");
                    e.printStackTrace();
                    return;
                }

            case 2 :
                // Report Primary Care Physician Information
                System.out.println("Entering \"Report Primary Care Physician Information\" Mode");
                System.out.println("--------------------------------------------------");
                System.out.print( "Enter Primary Care Physician ID: " );
                userInput = scanner.nextLine();

                // Performing the query
                try {
                    Statement stmt = connection.createStatement();
                    String str = "SELECT PCP.PHYSICIANID, D.FIRSTNAME, D.LASTNAME, PCP.SPECIALTY, PCP.MEDICALFACILITY " +
                            "FROM PCP JOIN DOCTOR D on PCP.PHYSICIANID = D.PHYSICIANID and PCP.ROLE = D.ROLE " +
                            "WHERE PCP.physicianID = " + userInput;
                    ResultSet rset = stmt.executeQuery(str);

                    if (!rset.isBeforeFirst()) {
                        System.out.println("NO DATA FOUND!!!");

                    } else {
                        int physID = 0;
                        String firstName, lastName, specialty, medFacility;

                        firstName = lastName = specialty = medFacility = "";

                        // Process the results
                        while (rset.next()) {
                            physID = rset.getInt("physicianID");
                            firstName = rset.getString("firstName");
                            lastName = rset.getString("lastName");
                            specialty = rset.getString("specialty");
                            medFacility = rset.getString("medicalFacility");
                        } // end while

                        System.out.println("\n-------------------------------------------------");
                        System.out.println("        Primary Care Physician Information");
                        System.out.println("-------------------------------------------------");
                        System.out.println("Full Name: " + firstName + " " + lastName);
                        System.out.println("Physician ID: " + physID);
                        System.out.println("Specialty: " + specialty);
                        System.out.println("Medical Facility: " + medFacility);
                        System.out.println("-------------------------------------------------\n");

                        rset.close();
                        stmt.close();
                        connection.close();
                    }

                } catch (SQLException e) {
                    System.out.println("Get Data Failed! Check output console");
                    e.printStackTrace();
                    return;
                }
                break;

            case 3 :
                // Report Operation Information
                System.out.println("Entering \"Operation Information\" Mode");
                System.out.println("--------------------------------------------------");
                System.out.print( "Enter Operation Invoice Number: " );
                userInput = scanner.nextLine();

                try {
                    Statement stmt = connection.createStatement();
                    String str = "Select O.INVOICENUMBER, O.OPERATIONDATE, D.FIRSTNAME as DFirstName, D.LASTNAME as DLastName, " +
                            "S.BOARDCERTIFIED, P.FIRSTNAME, P.LASTNAME, P.BLOODTYPE, P.CITY, P.STATE " +
                            "from Operation O join Surgeon S on O.physicianID = S.physicianID " +
                            "join Patient P on O.healthCareID = P.healthCareID join Doctor D on S.physicianID = D.physicianID " +
                            "where invoiceNumber  =" + userInput;


                    ResultSet rset = stmt.executeQuery(str);

                    if (!rset.isBeforeFirst()) {
                        System.out.println("NO DATA FOUND!!!");

                    } else {
                        int invoiceNumber = 0;
                        String operationDate, firstName, lastName, boardCertified, pFirstN, pLastN, bloodType, city, state;

                        operationDate = firstName = lastName = boardCertified = pFirstN = pLastN = bloodType = city = state = "";
                        // Process the results
                        while (rset.next()) {
                            invoiceNumber = rset.getInt("invoiceNumber");
                            operationDate = rset.getString("operationDate");
                            firstName = rset.getString("DFirstName");
                            lastName = rset.getString("DLastName");
                            boardCertified = rset.getString("boardCertified");
                            pFirstN = rset.getString("firstName");
                            pLastN = rset.getString("lastName");
                            bloodType = rset.getString("bloodType");
                            city = rset.getString("city");
                            state = rset.getString("state");

                        }

                        System.out.println("\n-------------------------------------------------");
                        System.out.println("Operation Information");
                        System.out.println("-------------------------------------------------");
                        System.out.println("Invoice Number: " + invoiceNumber);
                        System.out.println("Operation Date: " + operationDate);
                        System.out.println("Surgeon Full Name: " + firstName + " " + lastName);
                        System.out.println("Board Certified?: " + boardCertified);
                        System.out.println("Patient Full Name: " + pFirstN + " " + pLastN);
                        System.out.println("Blood Type: " + bloodType);
                        System.out.println("City: " + city);
                        System.out.println("State: " + state);
                        System.out.println("-------------------------------------------------\n");
                    }

                    rset.close();
                    stmt.close();
                    connection.close();

                } catch (SQLException e) {
                    System.out.println("Get Data Failed! Check output console");
                    e.printStackTrace();
                    return;
                }

                break;

            default:
                System.out.println("OPTION VALUE IS NOT WITHIN ACCEPTED RANGE");
                connection.close();
                System.exit(-1);
                break;
        }
    }
}
