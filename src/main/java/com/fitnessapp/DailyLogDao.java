package com.fitnessapp;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Date;

/**
 * Data access object for the daily_logs table.
 * Handles saving and loading daily summary logs for users.
 */
public class DailyLogDao {

    // Date format for storing dates as strings in SQLite
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // ---------- Create ----------
    /**
     * Inserts a new daily log into the database.
     * @param log the daily log to save
     * @param userId the ID of the user who owns this log
     */
    public void insert(DailyLog log, UUID userId) throws SQLException {
        final String sql = """
            INSERT INTO daily_logs (id, user_id, date, total_calories, total_workouts, notes)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, log.getId().toString());
            ps.setString(2, userId.toString());
            ps.setString(3, DATE_FORMAT.format(log.getDate()));
            ps.setInt(4, log.getTotalCalories());
            ps.setInt(5, log.getTotalWorkouts());
            ps.setString(6, log.getNotes());

            ps.executeUpdate();
        }
    }

    // ---------- Read single log ----------
    /**
     * Finds a daily log by its ID.
     */
    public Optional<DailyLog> findById(UUID id) throws SQLException {
        final String sql = "SELECT * FROM daily_logs WHERE id = ?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, id.toString());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    /**
     * Finds all daily logs for a specific user.
     */
    public List<DailyLog> findByUserId(UUID userId) throws SQLException {
        final String sql = "SELECT * FROM daily_logs WHERE user_id = ? ORDER BY date DESC";

        List<DailyLog> result = new ArrayList<>();

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, userId.toString());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        }

        return result;
    }

    /**
     * Finds a daily log for a user on a specific date.
     * @param userId the user ID
     * @param date the date to search for (only compares the date part, not time)
     */
    public Optional<DailyLog> findByUserIdAndDate(UUID userId, Date date) throws SQLException {
        // Convert date to string for comparison (just the date part)
        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateOnly.format(date);

        final String sql = "SELECT * FROM daily_logs WHERE user_id = ? AND date LIKE ?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, userId.toString());
            ps.setString(2, dateStr + "%");  // Match any time on this date

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    // ---------- Update ----------
    /**
     * Updates a daily log.
     */
    public void update(DailyLog log) throws SQLException {
        final String sql = """
            UPDATE daily_logs
            SET date = ?, total_calories = ?, total_workouts = ?, notes = ?
            WHERE id = ?
        """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, DATE_FORMAT.format(log.getDate()));
            ps.setInt(2, log.getTotalCalories());
            ps.setInt(3, log.getTotalWorkouts());
            ps.setString(4, log.getNotes());
            ps.setString(5, log.getId().toString());

            ps.executeUpdate();
        }
    }

    // ---------- Delete ----------
    /**
     * Deletes a daily log from the database.
     */
    public void delete(UUID id) throws SQLException {
        final String sql = "DELETE FROM daily_logs WHERE id = ?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, id.toString());
            ps.executeUpdate();
        }
    }

    // ---------- Helper ----------
    /**
     * Maps a database row to a DailyLog object.
     */
    private DailyLog mapRow(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        Date date = null;

        // Parse the date
        try {
            date = DATE_FORMAT.parse(rs.getString("date"));
        } catch (Exception e) {
            // If parsing fails, use current date
            date = new Date();
        }

        int totalCalories = rs.getInt("total_calories");
        int totalWorkouts = rs.getInt("total_workouts");
        String notes = rs.getString("notes");

        return new DailyLog(id, date, totalCalories, totalWorkouts, notes);
    }
}
