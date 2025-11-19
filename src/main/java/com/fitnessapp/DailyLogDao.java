package com.fitnessapp;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Data access object for the daily_logs table.
 * Handles saving and loading daily summary logs for users.
 */
public class DailyLogDao {

    /** Date format for storing dates as strings in SQLite */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // ---------- Create ----------//
    /**
     * Inserts a new daily log into the database.
     * @param log the daily log to save
     * @param userId the ID of the user who owns this log
     * @throws SQLException if a database error occurs
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
     * @throws SQLException if a database error occurs
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
     * @param userId the userId to search by
     * @throws SQLException if a database error occurs
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
     * @throws SQLException if a database error occurs
     */
    public Optional<DailyLog> findByUserIdAndDate(UUID userId, Date date) throws SQLException {
        final String sql = "SELECT * FROM daily_logs WHERE user_id = ? AND date = ?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, userId.toString());
            ps.setString(2, DATE_FORMAT.format(date)); // normalized at midnight

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    // ---------- Update ----------//
    /**
     * Updates a daily log.
     * @param log the DailyLog to update
     * @throws SQLException if a database error occurs
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

    // ---------- Delete ----------//
    /**
     * Deletes a daily log from the database.
     * @param id the id of the log to delete
     * @throws SQLException if a database error occurs
     */
    public void delete(UUID id) throws SQLException {
        final String sql = "DELETE FROM daily_logs WHERE id = ?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, id.toString());
            ps.executeUpdate();
        }
    }

    // ---------- Helper ----------//
    /**
     * Maps a ResultSet row to a DailyLog object.
     *
     * @param rs the ResultSet pointing to the current row
     * @return a DailyLog object with values from the row
     * @throws SQLException if a database access error occurs
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

    /**
     * Returns the daily log for today for the user, creating one if it does not exist.
     *
     * @param userId the ID of the user
     * @return the DailyLog for today
     * @throws SQLException if a database error occurs
     */
    public DailyLog getOrCreateToday(UUID userId) throws SQLException {
        Date today = normalizeToDateOnly(new Date());

        // Check if today's log already exists
        Optional<DailyLog> existing = findByUserIdAndDate(userId, today);
        if (existing.isPresent()) {
            return existing.get();
        }

        // If not, create a new one
        DailyLog newLog = new DailyLog();
        insert(newLog, userId);
        return newLog;
    }

    /**
     * Normalizes a Date object to midnight (time set to 00:00:00).
     *
     * @param date the date to normalize
     * @return a new Date object with the same date and time set to midnight
     */
    public static Date normalizeToDateOnly(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}//class end
