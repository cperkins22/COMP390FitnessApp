package com.fitnessapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Data access object for TrackedWorkout to communicate with the database.
 * Provides methods to save and retrieve tracked workouts from the database.
 */
public class TrackedWorkoutDao {
    /** Database connection used for all operations. */
    private final Connection conn;
    /** DAO for handling exercises associated with workouts. */
    private final TrackedExerciseDao exerciseDao;

    /**
     * Constructs a TrackedWorkoutDao with a database connection.
     * @param conn the connection to the database
     */
    public TrackedWorkoutDao(Connection conn) {
        this.conn = conn;
        this.exerciseDao = new TrackedExerciseDao(conn);
    }

    /**
     * Saves a TrackedWorkout to the database, including all its exercises.
     * @param workout the workout to save
     * @param userId the user ID associated with this workout
     * @throws SQLException if a database error occurs
     */
    public void save(TrackedWorkout workout, UUID userId) throws SQLException {
        String sql = "INSERT INTO tracked_workouts (id, user_id, workout_name, date_completed) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, workout.getId().toString());
            ps.setString(2, userId.toString());          // userId passed from session
            ps.setString(3, workout.getName());
            ps.setString(4, workout.getDateCompleted().toString());
            ps.executeUpdate();
        }

        for (TrackedExercise ex : workout.getExercises()) {
            exerciseDao.save(ex, workout.getId());
        }
    }

    /**
     * Finds all workouts for a specific user.
     * @param userId the user's ID
     * @return a list of TrackedWorkout objects
     * @throws SQLException if a database error occurs
     */
    public List<TrackedWorkout> findByUserId(UUID userId) throws SQLException {
        List<TrackedWorkout> workouts = new ArrayList<>();

        String sql = "SELECT * FROM tracked_workouts WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId.toString());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TrackedWorkout workout = new TrackedWorkout();
                    workout.setName(rs.getString("workout_name"));

                    // Parse UUID from DB
                    workout.getId().toString(); // Optional, ID is auto-generated in constructor
                    // If you want to use the DB ID, we can overwrite it:
                    workout = new TrackedWorkout();
                    workout.setName(rs.getString("workout_name"));
                    workout = overwriteWorkoutId(workout, rs.getString("id"));

                    workout.getExercises().addAll(exerciseDao.findByTrackedWorkoutId(UUID.fromString(rs.getString("id"))));
                    workouts.add(workout);
                }
            }
        }

        return workouts;
    }

    /**
     * Helper method to overwrite the auto-generated workout UUID
     * with the UUID stored in the database.
     * @param workout the workout object
     * @param idString the UUID string from the database
     * @return the workout with the ID overwritten
     */
    private TrackedWorkout overwriteWorkoutId(TrackedWorkout workout, String idString) {
        try {
            java.lang.reflect.Field field = TrackedWorkout.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(workout, UUID.fromString(idString));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workout;
    }
}//class diagram


