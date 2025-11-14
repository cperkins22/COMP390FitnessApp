package com.fitnessapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Simple helper for getting a SQLite connection
 * and making sure the schema exists.
 */
public final class Database {
    // SQLite file in the working directory
    private static final String URL = "jdbc:sqlite:fitness.db";

    private Database() {}

    /**
     * Get a new connection and ensure the schema exists.
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        ensureSchema(conn);
        return conn;
    }

    /**
     * Creates the users table if it doesn't exist.
     */
    private static void ensureSchema(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "  id TEXT PRIMARY KEY," +
                            "  first_name TEXT NOT NULL," +
                            "  last_name  TEXT NOT NULL," +
                            "  email      TEXT UNIQUE NOT NULL," +
                            "  pin        TEXT NOT NULL," +
                            "  height_in  REAL NOT NULL," +   // inches
                            "  weight_lb  REAL NOT NULL" +    // pounds
                            ")"
            );
        }
    }
}
