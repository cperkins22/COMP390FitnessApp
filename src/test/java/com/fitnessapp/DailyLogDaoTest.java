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
 * JUnit test class for DailyLogDao
 * Tests CRUD operations and special query methods
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DailyLogDaoTest {

    private DailyLogDao dailyLogDao;
    private UUID testUserId;
    private DailyLog testLog;

    @BeforeEach
    public void setUp() throws SQLException {
        dailyLogDao = new DailyLogDao();
        testUserId = UUID.randomUUID();

        // Create test daily log
        testLog = new DailyLog();
        testLog.setTotalCalories(2000);
        testLog.setTotalWorkouts(2);
        testLog.setNotes("Good day");

        // Clear daily_logs table before each test
        clearDailyLogsTable();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        clearDailyLogsTable();
    }

    private void clearDailyLogsTable() throws SQLException {
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM daily_logs");
        }
    }

    // ========== INSERT Tests ==========

    @Test
    @Order(1)
    @DisplayName("Test insert - daily log is saved to database")
    public void testInsert_Success() throws SQLException {
        dailyLogDao.insert(testLog, testUserId);

        Optional<DailyLog> found = dailyLogDao.findById(testLog.getId());
        assertTrue(found.isPresent(), "Daily log should be found after insert");

        DailyLog retrieved = found.get();
        assertEquals(testLog.getId(), retrieved.getId());
        assertEquals(2000, retrieved.getTotalCalories());
        assertEquals(2, retrieved.getTotalWorkouts());
        assertEquals("Good day", retrieved.getNotes());
    }

    // ========== FIND BY ID Tests ==========

    @Test
    @Order(2)
    @DisplayName("Test findById - existing log")
    public void testFindById_ExistingLog() throws SQLException {
        dailyLogDao.insert(testLog, testUserId);

        Optional<DailyLog> found = dailyLogDao.findById(testLog.getId());
        assertTrue(found.isPresent());
        assertEquals(testLog.getId(), found.get().getId());
    }

    @Test
    @Order(3)
    @DisplayName("Test findById - non-existing log")
    public void testFindById_NonExisting() throws SQLException {
        UUID randomId = UUID.randomUUID();
        Optional<DailyLog> found = dailyLogDao.findById(randomId);
        assertFalse(found.isPresent(), "Non-existing log should return empty");
    }

    // ========== FIND BY USER ID Tests ==========

    @Test
    @Order(4)
    @DisplayName("Test findByUserId - returns user's logs")
    public void testFindByUserId_Success() throws SQLException {
        dailyLogDao.insert(testLog, testUserId);

        DailyLog log2 = new DailyLog();
        log2.setTotalCalories(1800);
        dailyLogDao.insert(log2, testUserId);

        List<DailyLog> logs = dailyLogDao.findByUserId(testUserId);
        assertEquals(2, logs.size());
    }

    @Test
    @Order(5)
    @DisplayName("Test findByUserId - empty for new user")
    public void testFindByUserId_EmptyList() throws SQLException {
        UUID newUserId = UUID.randomUUID();
        List<DailyLog> logs = dailyLogDao.findByUserId(newUserId);
        assertTrue(logs.isEmpty(), "New user should have no logs");
    }

    // ========== FIND BY USER ID AND DATE Tests ==========

    @Test
    @Order(6)
    @DisplayName("Test findByUserIdAndDate - finds log for specific date")
    public void testFindByUserIdAndDate_Success() throws SQLException {
        Date today = DailyLogDao.normalizeToDateOnly(new Date());
        testLog.setDate(today);
        dailyLogDao.insert(testLog, testUserId);

        Optional<DailyLog> found = dailyLogDao.findByUserIdAndDate(testUserId, today);
        assertTrue(found.isPresent());
        assertEquals(testLog.getId(), found.get().getId());
    }

    @Test
    @Order(7)
    @DisplayName("Test findByUserIdAndDate - empty for different date")
    public void testFindByUserIdAndDate_DifferentDate() throws SQLException {
        dailyLogDao.insert(testLog, testUserId);

        Date pastDate = new Date(System.currentTimeMillis() - 86400000L * 365); // 1 year ago
        Optional<DailyLog> found = dailyLogDao.findByUserIdAndDate(testUserId, pastDate);
        assertFalse(found.isPresent(), "Should return empty for different date");
    }

    // ========== UPDATE Tests ==========

    @Test
    @Order(8)
    @DisplayName("Test update - daily log information updated")
    public void testUpdate_Success() throws SQLException {
        dailyLogDao.insert(testLog, testUserId);

        testLog.setTotalCalories(2500);
        testLog.setTotalWorkouts(3);
        testLog.setNotes("Great workout day");
        dailyLogDao.update(testLog);

        Optional<DailyLog> found = dailyLogDao.findById(testLog.getId());
        assertTrue(found.isPresent());
        DailyLog updated = found.get();
        assertEquals(2500, updated.getTotalCalories());
        assertEquals(3, updated.getTotalWorkouts());
        assertEquals("Great workout day", updated.getNotes());
    }

    // ========== DELETE Tests ==========

    @Test
    @Order(9)
    @DisplayName("Test delete - log removed from database")
    public void testDelete_Success() throws SQLException {
        dailyLogDao.insert(testLog, testUserId);

        Optional<DailyLog> foundBefore = dailyLogDao.findById(testLog.getId());
        assertTrue(foundBefore.isPresent(), "Log should exist before delete");

        dailyLogDao.delete(testLog.getId());

        Optional<DailyLog> foundAfter = dailyLogDao.findById(testLog.getId());
        assertFalse(foundAfter.isPresent(), "Log should not exist after delete");
    }

    // ========== GET OR CREATE TODAY Tests ==========

    @Test
    @Order(10)
    @DisplayName("Test getOrCreateToday - creates new log if none exists")
    public void testGetOrCreateToday_CreatesNew() throws SQLException {
        DailyLog todayLog = dailyLogDao.getOrCreateToday(testUserId);

        assertNotNull(todayLog);
        assertEquals(0, todayLog.getTotalCalories());
        assertEquals(0, todayLog.getTotalWorkouts());
    }

    @Test
    @Order(11)
    @DisplayName("Test getOrCreateToday - returns existing log for today")
    public void testGetOrCreateToday_ReturnsExisting() throws SQLException {
        Date today = DailyLogDao.normalizeToDateOnly(new Date());
        testLog.setDate(today);
        dailyLogDao.insert(testLog, testUserId);

        DailyLog retrieved = dailyLogDao.getOrCreateToday(testUserId);

        assertEquals(testLog.getId(), retrieved.getId());
        assertEquals(2000, retrieved.getTotalCalories());
    }

    // ========== NORMALIZE DATE Test ==========

    @Test
    @Order(12)
    @DisplayName("Test normalizeToDateOnly - sets time to midnight")
    public void testNormalizeToDateOnly() {
        Date dateWithTime = new Date();
        Date normalized = DailyLogDao.normalizeToDateOnly(dateWithTime);

        String timeStr = normalized.toString();
        assertTrue(timeStr.contains("00:00:00"), "Time should be midnight");
    }
}