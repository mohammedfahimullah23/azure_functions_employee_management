package com.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseUtil {

    public static Connection getConnection() throws Exception {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USERNAME");
        String pass = System.getenv("DB_PASSWORD");

        return DriverManager.getConnection(url, user, pass);
    }
}
