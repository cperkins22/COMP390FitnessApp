package com.fitnessapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

/**
 * JUnit test class for Exercise
 * Tests constructors, addSet, getTotalWeightLifted, and key methods
 */
public class ExerciseTest {

    private Exercise testExercise;

    @BeforeEach
    public void setUp() {
        testExercise = new Exercise("Bench Press", "Chest exercise");
    }

    // ========== Constructor Tests ==========

    @Test
    @DisplayName("Test constructor initializes fields correctly")
    public void testConstructor_Success() {
        Exercise exercise = new Exercise("Squat", "Leg exercise");

        assertNotNull(exercise.getId());
        assertEquals("Squat", exercise.getName());
        assertEquals("Leg exercise", exercise.getDescription());
        assertEquals(0, exercise.getSets());
        assertEquals(0, exercise.getRepsPerSet());
        assertNotNull(exercise.getSetList());
        assertTrue(exercise.getSetList().isEmpty());
    }

    @Test
    @DisplayName("Test constructor with UUID (database loading)")
    public void testConstructorWithUUID() {
        UUID customId = UUID.randomUUID();
        Exercise exercise = new Exercise(customId, "Deadlift", "Back exercise", 3, 10);

        assertEquals(customId, exercise.getId());
        assertEquals("Deadlift", exercise.getName());
        assertEquals("Back exercise", exercise.getDescription());
        assertEquals(3, exercise.getSets());
        assertEquals(10, exercise.getRepsPerSet());
    }

    // ========== addSet Tests ==========

    @Test
    @DisplayName("Test addSet - adds valid set")
    public void testAddSet_ValidSet() {
        ExerciseSet set = new ExerciseSet(10, 135.0f);
        testExercise.addSet(set);

        assertEquals(1, testExercise.getSets());
        assertEquals(1, testExercise.getSetList().size());
    }

    @Test
    @DisplayName("Test addSet - adds multiple sets")
    public void testAddSet_MultipleSets() {
        testExercise.addSet(new ExerciseSet(10, 135.0f));
        testExercise.addSet(new ExerciseSet(8, 155.0f));
        testExercise.addSet(new ExerciseSet(6, 175.0f));

        assertEquals(3, testExercise.getSets());
        assertEquals(3, testExercise.getSetList().size());
    }

    @Test
    @DisplayName("Test addSet - ignores null set")
    public void testAddSet_NullSet() {
        testExercise.addSet(null);

        assertEquals(0, testExercise.getSets());
        assertTrue(testExercise.getSetList().isEmpty());
    }

    @Test
    @DisplayName("Test addSet - ignores set with negative reps")
    public void testAddSet_NegativeReps() {
        ExerciseSet invalidSet = new ExerciseSet(-5, 100.0f);
        testExercise.addSet(invalidSet);

        assertEquals(0, testExercise.getSets());
        assertTrue(testExercise.getSetList().isEmpty());
    }

    @Test
    @DisplayName("Test addSet - ignores set with negative weight")
    public void testAddSet_NegativeWeight() {
        ExerciseSet invalidSet = new ExerciseSet(10, -50.0f);
        testExercise.addSet(invalidSet);

        assertEquals(0, testExercise.getSets());
        assertTrue(testExercise.getSetList().isEmpty());
    }

    // ========== getTotalWeightLifted Tests ==========

    @Test
    @DisplayName("Test getTotalWeightLifted - with no sets")
    public void testGetTotalWeightLifted_NoSets() {
        float total = testExercise.getTotalWeightLifted();
        assertEquals(0.0f, total);
    }

    @Test
    @DisplayName("Test getTotalWeightLifted - with single set")
    public void testGetTotalWeightLifted_SingleSet() {
        testExercise.addSet(new ExerciseSet(10, 100.0f));

        // Total = 10 reps × 100 lbs = 1000
        float total = testExercise.getTotalWeightLifted();
        assertEquals(1000.0f, total, 0.01f);
    }

    @Test
    @DisplayName("Test getTotalWeightLifted - with multiple sets")
    public void testGetTotalWeightLifted_MultipleSets() {
        testExercise.addSet(new ExerciseSet(10, 100.0f));  // 1000
        testExercise.addSet(new ExerciseSet(8, 120.0f));   // 960
        testExercise.addSet(new ExerciseSet(6, 140.0f));   // 840

        // Total = 1000 + 960 + 840 = 2800
        float total = testExercise.getTotalWeightLifted();
        assertEquals(2800.0f, total, 0.01f);
    }

    // ========== Getters/Setters Tests ==========

    @Test
    @DisplayName("Test setName and getName")
    public void testSetAndGetName() {
        testExercise.setName("Incline Bench Press");
        assertEquals("Incline Bench Press", testExercise.getName());
    }

    @Test
    @DisplayName("Test setDescription and getDescription")
    public void testSetAndGetDescription() {
        testExercise.setDescription("Upper chest exercise");
        assertEquals("Upper chest exercise", testExercise.getDescription());
    }

    // ========== toString Test ==========

    @Test
    @DisplayName("Test toString returns exercise name")
    public void testToString() {
        String result = testExercise.toString();
        assertEquals("Bench Press", result);
    }
}