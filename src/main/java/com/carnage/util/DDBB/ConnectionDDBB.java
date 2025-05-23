package com.carnage.util.DDBB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDDBB {

    private static final String URL = "jdbc:mysql://localhost:3306/carnage";
    private static final String USER = "root";
    private static final String PASS = "";


    /**
     * Establishes a connection to the database.
     *
     * @return a Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }


    /**
     * Closes the given AutoCloseable resource quietly.
     *
     * @param ac the AutoCloseable resource to close
     */
    public static void closeQuietly(AutoCloseable ac) {
        if (ac != null) {
            try {
                ac.close();
            } catch (Exception ignored) {
            }
        }
    }
}
