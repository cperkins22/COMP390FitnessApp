package com.fitnessapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.UUID;

/**
 * JUnit test class for DailyLog
 * Tests constructors and getter/setter methods
 */
public class DailyLogTest {

    private DailyLog testLog;

    @BeforeEach
    public void setUp() {
        testLog = new DailyLog();
    }

    // ========== Constructor Tests ==========

    @Test
    @DisplayName("Test default constructor initializes fields correctly")
    public void testDefaultConstructor() {
        DailyLog log = new DailyLog();

        assertNotNull(log.getId());
        assertNotNull(log.getDate());
        assertEquals(0, log.getTotalCalories());
        assertEquals(0, log.getTotalWorkouts());
        assertNull(log.getNotes());
    }

    @Test
    @DisplayName("Test constructor with all parameters")
    public void testFullConstructor() {
        UUID customId = UUID.randomUUID();
        Date customDate = new Date();

        DailyLog log = new DailyLog(customId, customDate, 2000, 2, "Good day");

        assertEquals(customId, log.getId());
        assertEquals(customDate, log.getDate());
        assertEquals(2000, log.getTotalCalories());
        assertEquals(2, log.getTotalWorkouts());
        assertEquals("Good day", log.getNotes());
    }

    // ========== Getter Tests ==========

    @Test
    @DisplayName("Test getId returns UUID")
    public void testGetId() {
        assertNotNull(testLog.getId());
        assertTrue(testLog.getId() instanceof UUID);
    }

    @Test
    @DisplayName("Test getDate returns Date object")
    public void testGetDate() {
        assertNotNull(testLog.getDate());
        assertTrue(testLog.getDate() instanceof Date);
    }

    // ========== Setter Tests ==========

    @Test
    @DisplayName("Test setTotalCalories updates value")
    public void testSetTotalCalories() {
        testLog.setTotalCalories(2500);
        assertEquals(2500, testLog.getTotalCalories());
    }

    @Test
    @DisplayName("Test setTotalWorkouts updates value")
    public void testSetTotalWorkouts() {
        testLog.setTotalWorkouts(3);
        assertEquals(3, testLog.getTotalWorkouts());
    }

    @Test
    @DisplayName("Test setNotes updates value")
    public void testSetNotes() {
        testLog.setNotes("Felt energized today");
        assertEquals("Felt energized today", testLog.getNotes());
    }

    @Test
    @DisplayName("Test setDate updates date")
    public void testSetDate() {
        Date newDate = new Date(System.currentTimeMillis() - 86400000); // Yesterday
        testLog.setDate(newDate);
        assertEquals(newDate, testLog.getDate());
    }

    // ========== Integration Test ==========

    @Test
    @DisplayName("Test setting all values together")
    public void testSetAllValues() {
        testLog.setTotalCalories(1800);
        testLog.setTotalWorkouts(1);
        testLog.setNotes("Rest day");

        assertEquals(1800, testLog.getTotalCalories());
        assertEquals(1, testLog.getTotalWorkouts());
        assertEquals("Rest day", testLog.getNotes());
    }

    @Test
    @DisplayName("Test updating values multiple times")
    public void testUpdateValues() {
        testLog.setTotalCalories(1000);
        assertEquals(1000, testLog.getTotalCalories());

        testLog.setTotalCalories(2000);
        assertEquals(2000, testLog.getTotalCalories());

        testLog.setTotalWorkouts(1);
        testLog.setTotalWorkouts(2);
        assertEquals(2, testLog.getTotalWorkouts());
    }
}