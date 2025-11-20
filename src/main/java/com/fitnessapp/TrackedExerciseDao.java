package com.fitnessapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Data access object for TrackedExercise.
 * Handles saving and loading exercises associated with a tracked workout.
 */
public class TrackedExerciseDao {

    /** Database connection used for queries. */
    private final Connection conn;

    /** DAO for handling individual exercise sets. */
    private final TrackedExerciseSetDao setDao;

    /**
     * Constructs a new TrackedExerciseDao with the given database connection.
     * @param conn the database connection
     */
    public TrackedExerciseDao(Connection conn) {
        this.conn = conn;
        this.setDao = new TrackedExerciseSetDao(conn);
    }

    /**
     * Saves a tracked exercise and all its sets to the database.
     * @param exercise the TrackedExercise to save
     * @param trackedWorkoutId the UUID of the associated tracked workout
     * @throws SQLException if a database error occurs
     */
    public void save(TrackedExercise exercise, UUID trackedWorkoutId) throws SQLException {
        String sql = "INSERT INTO tracked_exercises (id, tracked_workout_id, name) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, exercise.getId().toString());
            ps.setString(2, trackedWorkoutId.toString());
            ps.setString(3, exercise.getName());
            ps.executeUpdate();
        }

        // Save all associated sets
        for (TrackedExerciseSet set : exercise.getSets()) {
            setDao.save(set, exercise.getId());
        }
    }

    /**
     * Loads all exercises for a given tracked workout ID, including their sets.
     * @param trackedWorkoutId the UUID of the tracked workout
     * @return a list of TrackedExercise objects
     * @throws SQLException if a database error occurs
     */
    public List<TrackedExercise> findByTrackedWorkoutId(UUID trackedWorkoutId) throws SQLException {
        List<TrackedExercise> exercises = new ArrayList<>();
        String sql = "SELECT * FROM tracked_exercises WHERE tracked_workout_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trackedWorkoutId.toString());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TrackedExercise ex = new TrackedExercise();
                    ex.setName(rs.getString("name"));

                    // Overwrite the auto-generated UUID with the one from the DB
                    ex = overwriteExerciseId(ex, rs.getString("id"));

                    // Load associated sets
                    ex.getSets().addAll(setDao.findByTrackedExerciseId(ex.getId()));

                    exercises.add(ex);
                }
            }
        }

        return exercises;
    }

    /**
     * Helper method to overwrite the UUID of a TrackedExercise using reflection.
     * @param ex the exercise object to modify
     * @param idString the UUID string from the database
     * @return the updated TrackedExercise
     */
    private TrackedExercise overwriteExerciseId(TrackedExercise ex, String idString) {
        try {
            java.lang.reflect.Field field = TrackedExercise.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(ex, UUID.fromString(idString));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ex;
    }
}//class end
