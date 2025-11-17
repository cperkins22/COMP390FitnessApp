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
     * Creates all database tables if they don't exist.
     * NOTE: If you had an old fitness.db with a different layout,
     * delete that file so this schema can be applied cleanly.
     */
    private static void ensureSchema(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            // Create users table
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "  id TEXT PRIMARY KEY," +
                            "  first_name TEXT NOT NULL," +
                            "  last_name  TEXT NOT NULL," +
                            "  email      TEXT UNIQUE NOT NULL," +
                            "  pin        TEXT NOT NULL," +   // PIN instead of password
                            "  height_in  REAL NOT NULL," +   // inches
                            "  weight_lb  REAL NOT NULL," +   // pounds
                            "  weight_goal_lb REAL DEFAULT 0" + // weight goal in pounds
                            ")"
            );

            // Create workouts table - each workout belongs to a user
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS workouts (" +
                            "  id TEXT PRIMARY KEY," +
                            "  user_id TEXT NOT NULL," +
                            "  name TEXT," +
                            "  date TEXT NOT NULL," +         // ISO date format
                            "  notes TEXT," +
                            "  FOREIGN KEY (user_id) REFERENCES users(id)" +
                            ")"
            );

            // Create exercises table - each exercise belongs to a workout
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS exercises (" +
                            "  id TEXT PRIMARY KEY," +
                            "  workout_id TEXT NOT NULL," +
                            "  name TEXT NOT NULL," +
                            "  description TEXT," +
                            "  sets INTEGER NOT NULL," +
                            "  reps_per_set INTEGER NOT NULL," +
                            "  FOREIGN KEY (workout_id) REFERENCES workouts(id)" +
                            ")"
            );

            // Create exercise_sets table - each set belongs to an exercise
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS exercise_sets (" +
                            "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "  exercise_id TEXT NOT NULL," +
                            "  reps INTEGER NOT NULL," +
                            "  weight REAL NOT NULL," +
                            "  FOREIGN KEY (exercise_id) REFERENCES exercises(id)" +
                            ")"
            );

            //TRACKED TABLES (For user archives)

            // Create tracked_workouts table
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS tracked_workouts (" +
                            "id TEXT PRIMARY KEY, " +                  // workout UUID
                            "user_id TEXT NOT NULL, " +                // user UUID, stored in DB only
                            "workout_name TEXT NOT NULL, " +
                            "date_completed DATETIME DEFAULT CURRENT_TIMESTAMP" +
                            ");"
            );

            // Create tracked_exercises table
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS tracked_exercises (" +
                            "id TEXT PRIMARY KEY, " +                  // exercise UUID
                            "tracked_workout_id TEXT NOT NULL, " +
                            "name TEXT NOT NULL, " +
                            "FOREIGN KEY(tracked_workout_id) REFERENCES tracked_workouts(id) ON DELETE CASCADE" +
                            ");"
            );


            // Create tracked_exercise_sets table
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS tracked_exercise_sets (" +
                            "tracked_exercise_id TEXT NOT NULL, " +    // link to parent exercise UUID
                            "reps INTEGER NOT NULL, " +
                            "weight REAL NOT NULL, " +
                            "FOREIGN KEY(tracked_exercise_id) REFERENCES tracked_exercises(id) ON DELETE CASCADE" +
                            ");"
            );

            // Create meals table - each meal belongs to a user
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS meals (" +
                            "  id TEXT PRIMARY KEY," +
                            "  user_id TEXT NOT NULL," +
                            "  date TEXT NOT NULL," +         // ISO date format
                            "  name TEXT NOT NULL," +
                            "  calories INTEGER NOT NULL," +
                            "  protein REAL NOT NULL," +      // grams
                            "  carbs REAL NOT NULL," +        // grams
                            "  fat REAL NOT NULL," +          // grams
                            "  FOREIGN KEY (user_id) REFERENCES users(id)" +
                            ")"
            );

            // Create daily_logs table - daily summary for each user
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS daily_logs (" +
                            "  id TEXT PRIMARY KEY," +
                            "  user_id TEXT NOT NULL," +
                            "  date TEXT NOT NULL," +         // ISO date format, should be unique per user
                            "  total_calories INTEGER," +
                            "  total_workouts INTEGER," +
                            "  notes TEXT," +
                            "  FOREIGN KEY (user_id) REFERENCES users(id)" +
                            ")"
            );
        }
    }
}