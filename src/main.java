import java.sql.*;

public class main {

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

        connection.close();
    }
}
