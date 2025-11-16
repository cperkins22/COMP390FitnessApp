package com.fitnessapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TrackedExerciseDao {
    private Connection conn;
    private TrackedExerciseSetDao setDao;

    public TrackedExerciseDao(Connection conn) {
        this.conn = conn;
        this.setDao = new TrackedExerciseSetDao(conn);
    }

    public void save(TrackedExercise exercise, UUID trackedWorkoutId) throws SQLException {
        String sql = "INSERT INTO tracked_exercises (id, tracked_workout_id, name) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, exercise.getId().toString());
            ps.setString(2, trackedWorkoutId.toString());
            ps.setString(3, exercise.getName());
            ps.executeUpdate();
        }

        for (TrackedExerciseSet set : exercise.getSets()) {
            setDao.save(set, exercise.getId());
        }
    }

    // Load exercises for a tracked workout
    public List<TrackedExercise> findByTrackedWorkoutId(UUID trackedWorkoutId) throws SQLException {
        List<TrackedExercise> exercises = new ArrayList<>();
        String sql = "SELECT * FROM tracked_exercises WHERE tracked_workout_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trackedWorkoutId.toString());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TrackedExercise ex = new TrackedExercise();
                    ex.setName(rs.getString("name"));
                    ex = overwriteExerciseId(ex, rs.getString("id"));

                    ex.getSets().addAll(setDao.findByTrackedExerciseId(ex.getId()));
                    exercises.add(ex);
                }
            }
        }

        return exercises;
    }

    // Helper to overwrite UUID
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
}

