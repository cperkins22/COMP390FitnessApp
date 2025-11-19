package com.fitnessapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Data Access Object (DAO) for the "users" table.
 * Provides methods to create, read, update, delete, and validate users.
 */
public class UserDao {

    // ---------- CREATE ----------

    /**
     * Inserts a new user into the database.
     * @param u the User object to insert
     * @throws SQLException if a database error occurs
     */
    public void insert(User u) throws SQLException {
        final String sql = """
            INSERT INTO users (id, first_name, last_name, email, pin, height_in, weight_lb, weight_goal_lb)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, u.getId().toString());
            ps.setString(2, u.getFirstName());
            ps.setString(3, u.getLastName());
            ps.setString(4, u.getEmail());
            ps.setString(5, u.getPin());
            ps.setFloat(6, u.getHeight());
            ps.setFloat(7, u.getWeight());
            ps.setFloat(8, u.getWeightGoal());

            ps.executeUpdate();
        }
    }

    // ---------- READ SINGLE USER ----------

    /**
     * Finds a user by their UUID.
     * @param id the user's UUID
     * @return an Optional containing the User if found, otherwise empty
     * @throws SQLException if a database error occurs
     */
    public Optional<User> findById(UUID id) throws SQLException {
        final String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, id.toString());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
    }

    /**
     * Finds a user by their email address.
     * @param email the user's email
     * @return an Optional containing the User if found, otherwise empty
     * @throws SQLException if a database error occurs
     */
    public Optional<User> findByEmail(String email) throws SQLException {
        final String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
    }

    // ---------- LIST ALL USERS ----------

    /**
     * Returns all users in the database, ordered by first and last name.
     * @return a List of User objects
     * @throws SQLException if a database error occurs
     */
    public List<User> findAll() throws SQLException {
        final String sql = "SELECT * FROM users ORDER BY first_name, last_name";
        List<User> result = new ArrayList<>();

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }

        return result;
    }

    // ---------- UPDATE USER ----------

    /**
     * Updates a user's information in the database.
     * @param u the User object containing updated values
     * @throws SQLException if a database error occurs
     */
    public void update(User u) throws SQLException {
        final String sql = """
            UPDATE users
            SET first_name = ?, last_name = ?, email = ?, pin = ?, height_in = ?, weight_lb = ?, weight_goal_lb = ?
            WHERE id = ?
        """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, u.getFirstName());
            ps.setString(2, u.getLastName());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getPin());
            ps.setFloat(5, u.getHeight());
            ps.setFloat(6, u.getWeight());
            ps.setFloat(7, u.getWeightGoal());
            ps.setString(8, u.getId().toString());

            ps.executeUpdate();
        }
    }

    // ---------- DELETE USER ----------

    /**
     * Deletes a user from the database.
     * @param id the UUID of the user to delete
     * @throws SQLException if a database error occurs
     */
    public void delete(UUID id) throws SQLException {
        final String sql = "DELETE FROM users WHERE id = ?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, id.toString());
            ps.executeUpdate();
        }
    }

    // ---------- PIN VALIDATION ----------

    /**
     * Validates a user's PIN.
     * @param id the user's UUID
     * @param pin the PIN to validate
     * @return true if the PIN matches, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean validatePin(UUID id, String pin) throws SQLException {
        final String sql = "SELECT 1 FROM users WHERE id = ? AND pin = ?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, id.toString());
            ps.setString(2, pin);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // ---------- INTERNAL HELPER ----------

    /**
     * Maps a database row to a User object.
     * @param rs the ResultSet positioned at the row to map
     * @return a User object
     * @throws SQLException if a database error occurs
     */
    private User mapRow(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        String first = rs.getString("first_name");
        String last = rs.getString("last_name");
        String email = rs.getString("email");
        String pin = rs.getString("pin");
        float height = rs.getFloat("height_in");
        float weight = rs.getFloat("weight_lb");
        float weightGoal = rs.getFloat("weight_goal_lb");

        return new User(id, first, last, email, pin, height, weight, weightGoal);
    }
}
