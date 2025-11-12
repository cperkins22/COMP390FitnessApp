package com.fitnessapp;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class UserDao {

    // ---------- Create ----------
    public void insert(User u) throws SQLException {
        final String sql = """
            INSERT INTO users (id, first_name, last_name, email, height_in, weight_lb)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getId().toString());
            ps.setString(2, u.getFirstName());
            ps.setString(3, u.getLastName());
            ps.setString(4, u.getEmail());
            ps.setFloat(5, u.getHeight());   // inches
            ps.setFloat(6, u.getWeight());   // pounds
            ps.executeUpdate();
        }
    }

    // ---------- Read ----------
    public Optional<User> findByEmail(String email) throws SQLException {
        final String sql = """
            SELECT id, first_name, last_name, email, height_in, weight_lb
            FROM users
            WHERE email = ?
        """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(mapRow(rs));
            }
        }
    }

    public Optional<User> findById(UUID id) throws SQLException {
        final String sql = """
            SELECT id, first_name, last_name, email, height_in, weight_lb
            FROM users
            WHERE id = ?
        """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(mapRow(rs));
            }
        }
    }

    // ---------- Update ----------
    public int update(User u) throws SQLException {
        final String sql = """
            UPDATE users
            SET first_name = ?, last_name = ?, email = ?, height_in = ?, weight_lb = ?
            WHERE id = ?
        """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getFirstName());
            ps.setString(2, u.getLastName());
            ps.setString(3, u.getEmail());
            ps.setFloat(4, u.getHeight());
            ps.setFloat(5, u.getWeight());
            ps.setString(6, u.getId().toString());
            return ps.executeUpdate(); // rows affected (0 or 1)
        }
    }

    // ---------- Delete ----------
    public int deleteById(UUID id) throws SQLException {
        final String sql = "DELETE FROM users WHERE id = ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id.toString());
            return ps.executeUpdate();
        }
    }

    // ---------- Utility ----------
    public boolean existsByEmail(String email) throws SQLException {
        final String sql = "SELECT 1 FROM users WHERE email = ? LIMIT 1";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        String first = rs.getString("first_name");
        String last  = rs.getString("last_name");
        String email = rs.getString("email");
        float height = rs.getFloat("height_in"); // inches
        float weight = rs.getFloat("weight_lb"); // pounds
        return new User(id, first, last, email, height, weight);
    }
}