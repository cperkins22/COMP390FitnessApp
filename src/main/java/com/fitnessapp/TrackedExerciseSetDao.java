package com.fitnessapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Class meant to support the Tracked Exercise Set Class
 */
public class TrackedExerciseSetDao {
    private Connection conn;

    public TrackedExerciseSetDao(Connection conn) { this.conn = conn; }

    public void save(TrackedExerciseSet set, UUID trackedExerciseId) throws SQLException {
        String sql = "INSERT INTO tracked_exercise_sets (tracked_exercise_id, reps, weight) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trackedExerciseId.toString());
            ps.setInt(2, set.getReps());
            ps.setFloat(3, set.getWeight());
            ps.executeUpdate();
        }
    }

    public List<TrackedExerciseSet> findByTrackedExerciseId(UUID trackedExerciseId) throws SQLException {
        List<TrackedExerciseSet> sets = new ArrayList<>();
        String sql = "SELECT * FROM tracked_exercise_sets WHERE tracked_exercise_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trackedExerciseId.toString());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sets.add(new TrackedExerciseSet(
                            rs.getInt("reps"),
                            rs.getFloat("weight")
                    ));
                }
            }
        }
        return sets;
    }
}


