package com.fitnessapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.UUID;

/**
 * JUnit test class for Workout
 * Tests constructors, addExercise, getTotalVolume, and key methods
 */
public class WorkoutTest {

    private Workout testWorkout;

    @BeforeEach
    public void setUp() {
        testWorkout = new Workout("Leg Day");
    }

    // ========== Constructor Tests ==========

    @Test
    @DisplayName("Test default constructor initializes fields")
    public void testDefaultConstructor() {
        Workout workout = new Workout();

        assertNotNull(workout.getId());
        assertNotNull(workout.getDate());
        assertNotNull(workout.getExercises());
        assertTrue(workout.getExercises().isEmpty());
        assertTrue(workout.getName().startsWith("Workout"));
    }

    @Test
    @DisplayName("Test constructor with name")
    public void testConstructorWithName() {
        Workout workout = new Workout("Push Day");

        assertNotNull(workout.getId());
        assertEquals("Push Day", workout.getName());
        assertNotNull(workout.getDate());
        assertTrue(workout.getExercises().isEmpty());
    }

    @Test
    @DisplayName("Test constructor with UUID (database loading)")
    public void testConstructorWithUUID() {
        UUID customId = UUID.randomUUID();
        Date customDate = new Date();

        Workout workout = new Workout(customId, customDate, "Heavy lifting", "Pull Day");

        assertEquals(customId, workout.getId());
        assertEquals(customDate, workout.getDate());
        assertEquals("Heavy lifting", workout.getNotes());
        assertEquals("Pull Day", workout.getName());
    }

    // ========== addExercise Tests ==========

    @Test
    @DisplayName("Test addExercise - adds single exercise")
    public void testAddExercise_SingleExercise() {
        Exercise exercise = new Exercise("Squat", "Leg exercise");
        testWorkout.addExercise(exercise);

        assertEquals(1, testWorkout.getExercises().size());
        assertEquals("Squat", testWorkout.getExercises().get(0).getName());
    }

    @Test
    @DisplayName("Test addExercise - adds multiple exercises")
    public void testAddExercise_MultipleExercises() {
        testWorkout.addExercise(new Exercise("Squat", "Legs"));
        testWorkout.addExercise(new Exercise("Leg Press", "Legs"));
        testWorkout.addExercise(new Exercise("Leg Curl", "Legs"));

        assertEquals(3, testWorkout.getExercises().size());
    }

    // ========== getTotalVolume Tests ==========

    @Test
    @DisplayName("Test getTotalVolume - no exercises")
    public void testGetTotalVolume_NoExercises() {
        float volume = testWorkout.getTotalVolume();
        assertEquals(0.0f, volume);
    }

    @Test
    @DisplayName("Test getTotalVolume - single exercise with sets")
    public void testGetTotalVolume_SingleExercise() {
        Exercise exercise = new Exercise("Squat", "Legs");
        exercise.addSet(new ExerciseSet(10, 135.0f));  // 1350
        exercise.addSet(new ExerciseSet(8, 185.0f));   // 1480

        testWorkout.addExercise(exercise);

        // Total = 1350 + 1480 = 2830
        float volume = testWorkout.getTotalVolume();
        assertEquals(2830.0f, volume, 0.01f);
    }

    @Test
    @DisplayName("Test getTotalVolume - multiple exercises with sets")
    public void testGetTotalVolume_MultipleExercises() {
        Exercise squat = new Exercise("Squat", "Legs");
        squat.addSet(new ExerciseSet(10, 135.0f));  // 1350
        squat.addSet(new ExerciseSet(8, 185.0f));   // 1480

        Exercise legPress = new Exercise("Leg Press", "Legs");
        legPress.addSet(new ExerciseSet(12, 200.0f));  // 2400
        legPress.addSet(new ExerciseSet(10, 220.0f));  // 2200

        testWorkout.addExercise(squat);
        testWorkout.addExercise(legPress);

        // Total = 1350 + 1480 + 2400 + 2200 = 7430
        float volume = testWorkout.getTotalVolume();
        assertEquals(7430.0f, volume, 0.01f);
    }

    // ========== Getters/Setters Tests ==========

    @Test
    @DisplayName("Test setName and getName")
    public void testSetAndGetName() {
        testWorkout.setName("Upper Body");
        assertEquals("Upper Body", testWorkout.getName());
    }

    @Test
    @DisplayName("Test setNotes and getNotes")
    public void testSetAndGetNotes() {
        testWorkout.setNotes("Focus on form");
        assertEquals("Focus on form", testWorkout.getNotes());
    }

    @Test
    @DisplayName("Test setDate and getDate")
    public void testSetAndGetDate() {
        Date newDate = new Date(System.currentTimeMillis() - 86400000); // Yesterday
        testWorkout.setDate(newDate);
        assertEquals(newDate, testWorkout.getDate());
    }

    // ========== toString Test ==========

    @Test
    @DisplayName("Test toString returns workout name")
    public void testToString() {
        String result = testWorkout.toString();
        assertEquals("Leg Day", result);
    }
}