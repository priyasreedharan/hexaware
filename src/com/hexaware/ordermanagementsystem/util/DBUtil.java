package com.hexaware.ordermanagementsystem.util;
import java.sql.*;


public class DBUtil {

        private static String URL = "jdbc:mysql://localhost:3306/oms";
        private static String USERNAME = "root";
        private static String PASSWORD = "admin";

        public static Connection getDBConn() throws SQLException {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
}
}
