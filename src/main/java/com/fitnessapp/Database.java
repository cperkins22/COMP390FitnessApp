package com.fitnessapp;

// Will need the SQLite JDBC driver on your classpath (e.g., Maven: org.xerial:sqlite-jdbc).
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {
    private static final String URL = "jdbc:sqlite:fitness.db"; // file in working directory

    private Database(){}

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        ensureSchema(conn);
        return conn;
    }

    private static void ensureSchema(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "  id TEXT PRIMARY KEY," +
                            "  first_name TEXT NOT NULL," +
                            "  last_name  TEXT NOT NULL," +
                            "  email      TEXT UNIQUE NOT NULL," +
                            "  height_in  REAL NOT NULL," +   // inches
                            "  weight_lb  REAL NOT NULL" +    // pounds
                            ")"
            );
        }
    }
}
