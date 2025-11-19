package com.fitnessapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Data access object for TrackedExerciseSet.
 * Handles saving and retrieving tracked exercise sets to/from the database.
 * Each set stores the number of repetitions and the weight used.
 */
public class TrackedExerciseSetDao {
    /** Connection to the database. */
    private Connection conn;
    /**
     * Constructs a new DAO with the given database connection.
     * @param conn the database connection
     */
    public TrackedExerciseSetDao(Connection conn) { this.conn = conn; }

    /**
     * Saves a tracked exercise set to the database, associating it with the given tracked exercise.
     * @param set the tracked exercise set to save
     * @param trackedExerciseId the ID of the tracked exercise this set belongs to
     * @throws SQLException if a database access error occurs
     */
    public void save(TrackedExerciseSet set, UUID trackedExerciseId) throws SQLException {
        String sql = "INSERT INTO tracked_exercise_sets (tracked_exercise_id, reps, weight) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trackedExerciseId.toString());
            ps.setInt(2, set.getReps());
            ps.setFloat(3, set.getWeight());
            ps.executeUpdate();
        }
    }

    /**
     * Retrieves all tracked exercise sets associated with a given tracked exercise.
     * @param trackedExerciseId the ID of the tracked exercise
     * @return a list of tracked exercise sets
     * @throws SQLException if a database access error occurs
     */
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


