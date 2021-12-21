package ru.sfedu.servicestation.utils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnect {


    public static String jdbcURL;
    static {
        try {
            jdbcURL = ConfigurationUtil.getConfigurationEntry(Constants.JDBC_URL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static  String jdbcUsername;
    static {
        try {
            jdbcUsername = ConfigurationUtil.getConfigurationEntry(Constants.JDBC_USERNAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static  String jdbcPassword;
    static {
        try {
            jdbcPassword = ConfigurationUtil.getConfigurationEntry(Constants.JDBC_PASSWORD);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JDBCConnect() throws IOException {
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
