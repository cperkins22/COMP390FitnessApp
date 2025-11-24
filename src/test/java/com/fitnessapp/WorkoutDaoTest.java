package com.fitnessapp;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JUnit test class for WorkoutDao
 * Tests CRUD operations for workouts with exercises and sets
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WorkoutDaoTest {

    private WorkoutDao workoutDao;
    private UUID testUserId;
    private Workout testWorkout;

    @BeforeEach
    public void setUp() throws SQLException {
        workoutDao = new WorkoutDao();
        testUserId = UUID.randomUUID();

        // Create test workout with exercises and sets
        testWorkout = new Workout("Leg Day");

        Exercise squat = new Exercise("Squat", "Leg exercise");
        squat.addSet(new ExerciseSet(10, 135.0f));
        squat.addSet(new ExerciseSet(8, 185.0f));

        Exercise legPress = new Exercise("Leg Press", "Leg machine");
        legPress.addSet(new ExerciseSet(12, 200.0f));

        testWorkout.addExercise(squat);
        testWorkout.addExercise(legPress);
        testWorkout.setNotes("Focus on form");

        // Clear tables before each test
        clearTables();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        clearTables();
    }

    private void clearTables() throws SQLException {
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM exercise_sets");
            stmt.executeUpdate("DELETE FROM exercises");
            stmt.executeUpdate("DELETE FROM workouts");
        }
    }

    // ========== INSERT Tests ==========

    @Test
    @Order(1)
    @DisplayName("Test insert - workout with exercises and sets saved")
    public void testInsert_Success() throws SQLException {
        workoutDao.insert(testWorkout, testUserId);

        Optional<Workout> found = workoutDao.findById(testWorkout.getId());
        assertTrue(found.isPresent(), "Workout should be found after insert");

        Workout retrieved = found.get();
        assertEquals(testWorkout.getId(), retrieved.getId());
        assertEquals("Leg Day", retrieved.getName());
        assertEquals("Focus on form", retrieved.getNotes());
        assertEquals(2, retrieved.getExercises().size());
    }

    @Test
    @Order(2)
    @DisplayName("Test insert - exercises and sets are saved correctly")
    public void testInsert_ExercisesAndSets() throws SQLException {
        workoutDao.insert(testWorkout, testUserId);

        Optional<Workout> found = workoutDao.findById(testWorkout.getId());
        assertTrue(found.isPresent());

        Workout workout = found.get();
        Exercise firstExercise = workout.getExercises().get(0);

        assertEquals("Squat", firstExercise.getName());
        assertEquals(2, firstExercise.getSetList().size());
        assertEquals(10, firstExercise.getSetList().get(0).getReps());
        assertEquals(135.0f, firstExercise.getSetList().get(0).getWeight());
    }

    // ========== FIND BY ID Tests ==========

    @Test
    @Order(3)
    @DisplayName("Test findById - existing workout")
    public void testFindById_ExistingWorkout() throws SQLException {
        workoutDao.insert(testWorkout, testUserId);

        Optional<Workout> found = workoutDao.findById(testWorkout.getId());
        assertTrue(found.isPresent());
        assertEquals(testWorkout.getId(), found.get().getId());
    }

    @Test
    @Order(4)
    @DisplayName("Test findById - non-existing workout")
    public void testFindById_NonExisting() throws SQLException {
        UUID randomId = UUID.randomUUID();
        Optional<Workout> found = workoutDao.findById(randomId);
        assertFalse(found.isPresent(), "Non-existing workout should return empty");
    }

    // ========== FIND BY USER ID Tests ==========

    @Test
    @Order(5)
    @DisplayName("Test findByUserId - returns user's workouts")
    public void testFindByUserId_Success() throws SQLException {
        workoutDao.insert(testWorkout, testUserId);

        Workout workout2 = new Workout("Push Day");
        workoutDao.insert(workout2, testUserId);

        List<Workout> workouts = workoutDao.findByUserId(testUserId);
        assertEquals(2, workouts.size());
    }

    @Test
    @Order(6)
    @DisplayName("Test findByUserId - empty for new user")
    public void testFindByUserId_EmptyList() throws SQLException {
        UUID newUserId = UUID.randomUUID();
        List<Workout> workouts = workoutDao.findByUserId(newUserId);
        assertTrue(workouts.isEmpty(), "New user should have no workouts");
    }

    @Test
    @Order(7)
    @DisplayName("Test findByUserId - workouts ordered by date descending")
    public void testFindByUserId_OrderedByDate() throws SQLException {
        Workout older = new Workout("Old Workout");
        older.setDate(new Date(System.currentTimeMillis() - 86400000)); // Yesterday

        Workout newer = new Workout("New Workout");
        newer.setDate(new Date()); // Today

        workoutDao.insert(older, testUserId);
        workoutDao.insert(newer, testUserId);

        List<Workout> workouts = workoutDao.findByUserId(testUserId);
        assertEquals(2, workouts.size());
        assertEquals("New Workout", workouts.get(0).getName(), "Newer workout should be first");
    }

    // ========== UPDATE Tests ==========

    @Test
    @Order(8)
    @DisplayName("Test update - workout notes and date updated")
    public void testUpdate_Success() throws SQLException {
        workoutDao.insert(testWorkout, testUserId);

        Date newDate = new Date(System.currentTimeMillis() - 3600000); // 1 hour ago
        testWorkout.setDate(newDate);
        testWorkout.setNotes("Updated notes");
        workoutDao.update(testWorkout);

        Optional<Workout> found = workoutDao.findById(testWorkout.getId());
        assertTrue(found.isPresent());
        assertEquals("Updated notes", found.get().getNotes());
    }

    // ========== DELETE Tests ==========

    @Test
    @Order(9)
    @DisplayName("Test delete - workout and all exercises/sets removed")
    public void testDelete_Success() throws SQLException {
        workoutDao.insert(testWorkout, testUserId);

        Optional<Workout> foundBefore = workoutDao.findById(testWorkout.getId());
        assertTrue(foundBefore.isPresent(), "Workout should exist before delete");

        workoutDao.delete(testWorkout.getId());

        Optional<Workout> foundAfter = workoutDao.findById(testWorkout.getId());
        assertFalse(foundAfter.isPresent(), "Workout should not exist after delete");
    }

    @Test
    @Order(10)
    @DisplayName("Test delete - non-existing workout doesn't throw error")
    public void testDelete_NonExisting() throws SQLException {
        UUID randomId = UUID.randomUUID();
        assertDoesNotThrow(() -> workoutDao.delete(randomId));
    }

    @Test
    @Order(11)
    @DisplayName("Test deleteAllWorkoutsForUser - removes all user workouts")
    public void testDeleteAllWorkoutsForUser() throws SQLException {
        workoutDao.insert(testWorkout, testUserId);

        Workout workout2 = new Workout("Push Day");
        workoutDao.insert(workout2, testUserId);

        List<Workout> beforeDelete = workoutDao.findByUserId(testUserId);
        assertEquals(2, beforeDelete.size());

        workoutDao.deleteAllWorkoutsForUser(testUserId);

        List<Workout> afterDelete = workoutDao.findByUserId(testUserId);
        assertTrue(afterDelete.isEmpty());
    }

    // ========== INTEGRATION Test ==========

    @Test
    @Order(12)
    @DisplayName("Integration - Full CRUD workflow with exercises")
    public void testIntegration_FullWorkflow() throws SQLException {
        // Insert
        workoutDao.insert(testWorkout, testUserId);
        List<Workout> workouts = workoutDao.findByUserId(testUserId);
        assertEquals(1, workouts.size());
        assertEquals(2, workouts.get(0).getExercises().size());

        // Update
        testWorkout.setNotes("New notes");
        workoutDao.update(testWorkout);
        Optional<Workout> updated = workoutDao.findById(testWorkout.getId());
        assertEquals("New notes", updated.get().getNotes());

        // Delete
        workoutDao.delete(testWorkout.getId());
        workouts = workoutDao.findByUserId(testUserId);
        assertTrue(workouts.isEmpty());
    }
}