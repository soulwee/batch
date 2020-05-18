/*
 * Decompiled with CFR 0.149.
 */
package com.demo.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private static String USERNAME = "";
    private static String PASSWORD = "";
    private static String DRIVER = "";
    private static String URL = "";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            try {
                Class.forName(DRIVER);
            }
            catch (ClassNotFoundException e) {
                System.out.println("\u627e\u4e0d\u5230\u9a71\u52a8\u7a0b\u5e8f\u7c7b \uff0c\u52a0\u8f7d\u9a71\u52a8\u5931\u8d25\uff01");
            }
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            System.out.println("------------getConnection error------------");
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public static void initConnectionParams() {
        Properties properties = new Properties();
        try {
            FileInputStream is = new FileInputStream("www/jdbc.properties");
            properties = new Properties();
            properties.load(is);
            URL = properties.getProperty("jdbc.url");
            DRIVER = properties.getProperty("jdbc.driverClassName");
            USERNAME = properties.getProperty("jdbc.username");
            PASSWORD = properties.getProperty("jdbc.password");
        }
        catch (IOException e) {
            System.out.println("------------initConnectionParams error------------");
            e.printStackTrace();
        }
    }

    public static void cleanUp(PreparedStatement pstmt, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Connection releaseConnection(Connection conn, boolean isClose) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
        catch (SQLException e) {
            System.out.println("------------releaseConnection error------------");
        }
        if (isClose) {
            return null;
        }
        Connection newConn = JdbcUtils.getConnection();
        return newConn;
    }
}

