package com.carnage.util.DDBB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDDBB {

    public class JDBCUtils {


    }
        private static final String URL = "jdbc:mysql://localhost:3306/carnage";
        private static final String USER = "root";
        private static final String PASS = "";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASS);
        }

        public static void closeQuietly(AutoCloseable ac) {
            if (ac != null) {
                try { ac.close(); } catch (Exception ignored) {}
            }
        }
}
