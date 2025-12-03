package com.fitnessapp;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Date;

/**
 * Data Access Object (DAO) for workouts.
 * Handles operations for workouts, exercises, and exercise sets.
 * Workouts belong to a user and may contain multiple exercises,
 * each of which contains multiple sets.
 */
public class WorkoutDao {

    /** Date format used for storing dates in SQLite */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // ---------- CREATE ----------

    /**
     * Inserts a new workout into the database along with all exercises and sets.
     * @param w the workout to save
     * @param userId the ID of the user who owns this workout
     * @throws SQLException if a database error occurs
     */
    public void insert(Workout w, UUID userId) throws SQLException {
        final String workoutSql = """
            INSERT INTO workouts (id, user_id, name, date, notes)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(workoutSql)) {

            ps.setString(1, w.getId().toString());
            ps.setString(2, userId.toString());
            ps.setString(3, w.getName());
            ps.setString(4, DATE_FORMAT.format(w.getDate()));
            ps.setString(5, w.getNotes());
            ps.executeUpdate();
        }

        // Insert all exercises and sets
        for (Exercise exercise : w.getExercises()) {
            insertExercise(exercise, w.getId());
        }
    }

    /**
     * Inserts an exercise and all its sets for a workout.
     */
    private void insertExercise(Exercise exercise, UUID workoutId) throws SQLException {
        final String exerciseSql = """
            INSERT INTO exercises (id, workout_id, name, description, sets, reps_per_set)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(exerciseSql)) {

            ps.setString(1, exercise.getId().toString());
            ps.setString(2, workoutId.toString());
            ps.setString(3, exercise.getName());
            ps.setString(4, exercise.getDescription());
            ps.setInt(5, exercise.getSets());
            ps.setInt(6, exercise.getRepsPerSet());
            ps.executeUpdate();
        }

        for (ExerciseSet set : exercise.getSetList()) {
            insertSet(set, exercise.getId());
        }
    }

    /**
     * Inserts a set for a specific exercise.
     */
    private void insertSet(ExerciseSet set, UUID exerciseId) throws SQLException {
        final String setSql = """
            INSERT INTO exercise_sets (exercise_id, reps, weight)
            VALUES (?, ?, ?)
        """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(setSql)) {

            ps.setString(1, exerciseId.toString());
            ps.setInt(2, set.getReps());
            ps.setFloat(3, set.getWeight());
            ps.executeUpdate();
        }
    }

    // ---------- READ ----------

    /**
     * Finds a workout by its ID and loads all exercises and sets.
     * @param id the workout ID
     * @return Optional containing the workout if found, empty otherwise
     * @throws SQLException if a database error occurs
     */
    public Optional<Workout> findById(UUID id) throws SQLException {
        final String sql = "SELECT * FROM workouts WHERE id = ?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, id.toString());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Workout workout = mapWorkoutRow(rs);
                    loadExercisesForWorkout(workout);
                    return Optional.of(workout);
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    /**
     * Finds all workouts for a specific user, sorted by date descending.
     */
    public List<Workout> findByUserId(UUID userId) throws SQLException {
        final String sql = "SELECT * FROM workouts WHERE user_id = ? ORDER BY date DESC";
        List<Workout> result = new ArrayList<>();

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, userId.toString());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Workout workout = mapWorkoutRow(rs);
                    loadExercisesForWorkout(workout);
                    result.add(workout);
                }
            }
        }

        return result;
    }

    /**
     * Finds a workout by its name.
     * @param name the workout's name
     * @return an Optional containing the User if found, otherwise empty
     * @throws SQLException if a database error occurs
     */
    public Optional<Workout> findByName(String name) throws SQLException {
        final String sql = "SELECT * FROM workouts WHERE name = ?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapWorkoutRow(rs)) : Optional.empty();
            }
        }
    }

    /**
     * Loads all exercises and their sets for a workout.
     */
    private void loadExercisesForWorkout(Workout workout) throws SQLException {
        final String sql = "SELECT * FROM exercises WHERE workout_id = ?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, workout.getId().toString());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Exercise exercise = mapExerciseRow(rs);
                    loadSetsForExercise(exercise);
                    workout.addExercise(exercise);
                }
            }
        }
    }

    /**
     * Loads all sets for an exercise.
     */
    private void loadSetsForExercise(Exercise exercise) throws SQLException {
        final String sql = "SELECT * FROM exercise_sets WHERE exercise_id = ?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, exercise.getId().toString());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ExerciseSet set = mapSetRow(rs);
                    exercise.addSet(set);
                }
            }
        }
    }

    // ---------- UPDATE ----------

    /**
     * Updates a workout's date and notes. Exercises are not updated.
     */
    public void update(Workout w) throws SQLException {
        final String sql = """
            UPDATE workouts
            SET date = ?, notes = ?
            WHERE id = ?
        """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, DATE_FORMAT.format(w.getDate()));
            ps.setString(2, w.getNotes());
            ps.setString(3, w.getId().toString());
            ps.executeUpdate();
        }
    }

    // ---------- DELETE ----------

    /**
     * Deletes a workout and all its exercises and sets.
     */
    public void delete(UUID id) throws SQLException {
        // Delete all sets
        List<UUID> exerciseIds = new ArrayList<>();
        final String getExercisesSql = "SELECT id FROM exercises WHERE workout_id = ?";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(getExercisesSql)) {

            ps.setString(1, id.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    exerciseIds.add(UUID.fromString(rs.getString("id")));
                }
            }
        }

        for (UUID exerciseId : exerciseIds) {
            final String deleteSetsSql = "DELETE FROM exercise_sets WHERE exercise_id = ?";
            try (Connection c = Database.getConnection();
                 PreparedStatement ps = c.prepareStatement(deleteSetsSql)) {
                ps.setString(1, exerciseId.toString());
                ps.executeUpdate();
            }
        }

        // Delete exercises
        final String deleteExercisesSql = "DELETE FROM exercises WHERE workout_id = ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(deleteExercisesSql)) {
            ps.setString(1, id.toString());
            ps.executeUpdate();
        }

        // Delete workout
        final String deleteWorkoutSql = "DELETE FROM workouts WHERE id = ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(deleteWorkoutSql)) {
            ps.setString(1, id.toString());
            ps.executeUpdate();
        }
    }

    /**
     * Deletes all workouts for a given user.
     */
    public void deleteAllWorkoutsForUser(UUID userId) throws SQLException {
        final String sql = "DELETE FROM workouts WHERE user_id = ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, userId.toString());
            ps.executeUpdate();
        }
    }

    // ---------- MAPPING HELPERS ----------

    /** Maps a database row to a Workout object. */
    private Workout mapWorkoutRow(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        Date date;
        try {
            date = DATE_FORMAT.parse(rs.getString("date"));
        } catch (Exception e) {
            date = new Date();
        }
        String notes = rs.getString("notes");
        String name = rs.getString("name");
        return new Workout(id, date, notes, name);
    }

    /** Maps a database row to an Exercise object. */
    private Exercise mapExerciseRow(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        String name = rs.getString("name");
        String description = rs.getString("description");
        int sets = rs.getInt("sets");
        int repsPerSet = rs.getInt("reps_per_set");
        return new Exercise(id, name, description, sets, repsPerSet);
    }

    /** Maps a database row to an ExerciseSet object. */
    private ExerciseSet mapSetRow(ResultSet rs) throws SQLException {
        int reps = rs.getInt("reps");
        float weight = rs.getFloat("weight");
        return new ExerciseSet(reps, weight);
    }
}//class end
