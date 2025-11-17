package com.fitnessapp;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Date;

/**
 * Data access object for the meals table.
 * Handles saving and loading user meal logs.
 */
public class MealDao {

    // Date format for storing dates as strings in SQLite
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // ---------- Create ----------
    /**
     * Inserts a new meal into the database.
     * @param meal the meal to save
     * @param userId the ID of the user who logged this meal
     */
    public void insert(Meal meal, UUID userId) throws SQLException {
        final String sql = """
            INSERT INTO meals (id, user_id, date, name, calories, protein, carbs, fat)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, meal.getId().toString());
            ps.setString(2, userId.toString());
            ps.setString(3, DATE_FORMAT.format(meal.getDate()));
            ps.setString(4, meal.getName());
            ps.setInt(5, meal.getCalories());
            ps.setDouble(6, meal.getProtein());
            ps.setDouble(7, meal.getCarbs());
            ps.setDouble(8, meal.getFat());

            ps.executeUpdate();
        }
    }

    // ---------- Read single meal ----------
    /**
     * Finds a meal by its ID.
     */
    public Optional<Meal> findById(UUID id) throws SQLException {
        final String sql = "SELECT * FROM meals WHERE id = ?";

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
     * Finds all meals for a specific user.
     */
    public List<Meal> findByUserId(UUID userId) throws SQLException {
        final String sql = "SELECT * FROM meals WHERE user_id = ? ORDER BY date DESC";

        List<Meal> result = new ArrayList<>();

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
     * Finds all meals for a user on a specific date.
     * @param userId the user ID
     * @param date the date to search for (only compares the date part, not time)
     */
    public List<Meal> findByUserIdAndDate(UUID userId, Date date) throws SQLException {
        // Convert date to string for comparison (just the date part)
        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateOnly.format(date);

        final String sql = "SELECT * FROM meals WHERE user_id = ? AND date LIKE ? ORDER BY date ASC";

        List<Meal> result = new ArrayList<>();

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, userId.toString());
            ps.setString(2, dateStr + "%");  // Match any time on this date

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        }

        return result;
    }

    // ---------- Update ----------
    /**
     * Updates meal information.
     */
    public void update(Meal meal) throws SQLException {
        final String sql = """
            UPDATE meals
            SET date = ?, name = ?, calories = ?, protein = ?, carbs = ?, fat = ?
            WHERE id = ?
        """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, DATE_FORMAT.format(meal.getDate()));
            ps.setString(2, meal.getName());
            ps.setInt(3, meal.getCalories());
            ps.setDouble(4, meal.getProtein());
            ps.setDouble(5, meal.getCarbs());
            ps.setDouble(6, meal.getFat());
            ps.setString(7, meal.getId().toString());

            ps.executeUpdate();
        }
    }

    // ---------- Delete ----------
    /**
     * Deletes a meal from the database.
     */
    public void delete(UUID id) throws SQLException {
        final String sql = "DELETE FROM meals WHERE id = ?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, id.toString());
            ps.executeUpdate();
        }
    }

    // ---------- Helper ----------
    /**
     * Maps a database row to a Meal object.
     */
    private Meal mapRow(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        String name = rs.getString("name");
        Date date = null;

        // Parse the date
        try {
            date = DATE_FORMAT.parse(rs.getString("date"));
        } catch (Exception e) {
            // If parsing fails, use current date
            date = new Date();
        }

        int calories = rs.getInt("calories");
        double protein = rs.getDouble("protein");
        double carbs = rs.getDouble("carbs");
        double fat = rs.getDouble("fat");

        return new Meal(id, date, name, calories, protein, carbs, fat);
    }
}