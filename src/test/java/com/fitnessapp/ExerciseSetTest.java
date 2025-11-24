package com.fitnessapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test class for ExerciseSet
 * Tests constructor and getter/setter methods
 */
public class ExerciseSetTest {

    private ExerciseSet testSet;

    @BeforeEach
    public void setUp() {
        testSet = new ExerciseSet(10, 135.0f);
    }

    // ========== Constructor Tests ==========

    @Test
    @DisplayName("Test constructor initializes reps and weight correctly")
    public void testConstructor_Success() {
        ExerciseSet set = new ExerciseSet(12, 225.0f);

        assertEquals(12, set.getReps());
        assertEquals(225.0f, set.getWeight());
    }

    @Test
    @DisplayName("Test constructor with zero values")
    public void testConstructor_ZeroValues() {
        ExerciseSet set = new ExerciseSet(0, 0.0f);

        assertEquals(0, set.getReps());
        assertEquals(0.0f, set.getWeight());
    }

    // ========== Getter Tests ==========

    @Test
    @DisplayName("Test getReps returns correct value")
    public void testGetReps() {
        assertEquals(10, testSet.getReps());
    }

    @Test
    @DisplayName("Test getWeight returns correct value")
    public void testGetWeight() {
        assertEquals(135.0f, testSet.getWeight());
    }

    // ========== Setter Tests ==========

    @Test
    @DisplayName("Test setReps updates reps value")
    public void testSetReps() {
        testSet.setReps(15);
        assertEquals(15, testSet.getReps());
    }

    @Test
    @DisplayName("Test setWeight updates weight value")
    public void testSetWeight() {
        testSet.setWeight(185.0f);
        assertEquals(185.0f, testSet.getWeight());
    }

    @Test
    @DisplayName("Test updating both reps and weight")
    public void testSetBothValues() {
        testSet.setReps(8);
        testSet.setWeight(205.0f);

        assertEquals(8, testSet.getReps());
        assertEquals(205.0f, testSet.getWeight());
    }

    // ========== Edge Case Test ==========

    @Test
    @DisplayName("Test with negative values (no validation in class)")
    public void testNegativeValues() {
        ExerciseSet set = new ExerciseSet(-5, -100.0f);

        assertEquals(-5, set.getReps());
        assertEquals(-100.0f, set.getWeight());
        // Note: Class doesn't validate, so negative values are stored
    }
}