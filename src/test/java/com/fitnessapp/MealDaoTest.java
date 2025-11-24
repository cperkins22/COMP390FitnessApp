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
 * JUnit test class for MealDao
 * Tests CRUD operations and key query methods
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MealDaoTest {

    private MealDao mealDao;
    private UUID testUserId;
    private Meal testMeal;

    @BeforeEach
    public void setUp() throws SQLException {
        mealDao = new MealDao();
        testUserId = UUID.randomUUID();

        // Create test meal
        testMeal = new Meal("Test Breakfast");
        testMeal.setCalories(500);
        testMeal.setProtein(30.0);
        testMeal.setCarbs(60.0);
        testMeal.setFat(15.0);

        // Clear meals table before each test
        clearMealsTable();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        clearMealsTable();
    }

    private void clearMealsTable() throws SQLException {
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM meals");
        }
    }

    // ========== INSERT Tests ==========

    @Test
    @Order(1)
    @DisplayName("Test insert - meal is saved to database")
    public void testInsert_Success() throws SQLException {
        mealDao.insert(testMeal, testUserId);

        Optional<Meal> found = mealDao.findById(testMeal.getId());
        assertTrue(found.isPresent(), "Meal should be found after insert");

        Meal retrieved = found.get();
        assertEquals(testMeal.getId(), retrieved.getId());
        assertEquals("Test Breakfast", retrieved.getName());
        assertEquals(500, retrieved.getCalories());
        assertEquals(30.0, retrieved.getProtein());
        assertEquals(60.0, retrieved.getCarbs());
        assertEquals(15.0, retrieved.getFat());
    }

    @Test
    @Order(2)
    @DisplayName("Test insert - multiple meals for same user")
    public void testInsert_MultipleMeals() throws SQLException {
        Meal meal1 = new Meal("Breakfast");
        Meal meal2 = new Meal("Lunch");

        mealDao.insert(meal1, testUserId);
        mealDao.insert(meal2, testUserId);

        List<Meal> userMeals = mealDao.findByUserId(testUserId);
        assertEquals(2, userMeals.size(), "User should have 2 meals");
    }

    // ========== FIND BY ID Tests ==========

    @Test
    @Order(3)
    @DisplayName("Test findById - existing meal")
    public void testFindById_ExistingMeal() throws SQLException {
        mealDao.insert(testMeal, testUserId);

        Optional<Meal> found = mealDao.findById(testMeal.getId());
        assertTrue(found.isPresent());
        assertEquals(testMeal.getId(), found.get().getId());
    }

    @Test
    @Order(4)
    @DisplayName("Test findById - non-existing meal")
    public void testFindById_NonExisting() throws SQLException {
        UUID randomId = UUID.randomUUID();
        Optional<Meal> found = mealDao.findById(randomId);
        assertFalse(found.isPresent(), "Non-existing meal should return empty");
    }

    // ========== FIND BY USER ID Tests ==========

    @Test
    @Order(5)
    @DisplayName("Test findByUserId - returns user's meals")
    public void testFindByUserId_Success() throws SQLException {
        mealDao.insert(testMeal, testUserId);

        Meal meal2 = new Meal("Lunch");
        meal2.setCalories(700);
        mealDao.insert(meal2, testUserId);

        List<Meal> meals = mealDao.findByUserId(testUserId);
        assertEquals(2, meals.size());
    }

    @Test
    @Order(6)
    @DisplayName("Test findByUserId - empty for new user")
    public void testFindByUserId_EmptyList() throws SQLException {
        UUID newUserId = UUID.randomUUID();
        List<Meal> meals = mealDao.findByUserId(newUserId);
        assertTrue(meals.isEmpty(), "New user should have no meals");
    }

    // ========== FIND BY USER ID AND DATE Tests ==========

    @Test
    @Order(7)
    @DisplayName("Test findByUserIdAndDate - returns meals for specific date")
    public void testFindByUserIdAndDate_Success() throws SQLException {
        Date today = new Date();
        testMeal.setDate(today);
        mealDao.insert(testMeal, testUserId);

        List<Meal> meals = mealDao.findByUserIdAndDate(testUserId, today);
        assertEquals(1, meals.size());
        assertEquals(testMeal.getId(), meals.get(0).getId());
    }

    @Test
    @Order(8)
    @DisplayName("Test findByUserIdAndDate - empty for different date")
    public void testFindByUserIdAndDate_DifferentDate() throws SQLException {
        mealDao.insert(testMeal, testUserId);

        // Query for a date far in the past
        Date pastDate = new Date(System.currentTimeMillis() - 86400000L * 365); // 1 year ago
        List<Meal> meals = mealDao.findByUserIdAndDate(testUserId, pastDate);
        assertTrue(meals.isEmpty(), "Should return empty for different date");
    }

    // ========== UPDATE Tests ==========

    @Test
    @Order(9)
    @DisplayName("Test update - meal information is updated")
    public void testUpdate_Success() throws SQLException {
        mealDao.insert(testMeal, testUserId);

        testMeal.setName("Updated Breakfast");
        testMeal.setCalories(600);
        testMeal.setProtein(35.0);
        mealDao.update(testMeal);

        Optional<Meal> found = mealDao.findById(testMeal.getId());
        assertTrue(found.isPresent());
        Meal updated = found.get();
        assertEquals("Updated Breakfast", updated.getName());
        assertEquals(600, updated.getCalories());
        assertEquals(35.0, updated.getProtein());
    }

    // ========== DELETE Tests ==========

    @Test
    @Order(10)
    @DisplayName("Test delete - meal is removed from database")
    public void testDelete_Success() throws SQLException {
        mealDao.insert(testMeal, testUserId);

        Optional<Meal> foundBefore = mealDao.findById(testMeal.getId());
        assertTrue(foundBefore.isPresent(), "Meal should exist before delete");

        mealDao.delete(testMeal.getId());

        Optional<Meal> foundAfter = mealDao.findById(testMeal.getId());
        assertFalse(foundAfter.isPresent(), "Meal should not exist after delete");
    }

    @Test
    @Order(11)
    @DisplayName("Test delete - non-existing meal doesn't throw error")
    public void testDelete_NonExisting() throws SQLException {
        UUID randomId = UUID.randomUUID();
        assertDoesNotThrow(() -> mealDao.delete(randomId));
    }

    // ========== INTEGRATION Test ==========

    @Test
    @Order(12)
    @DisplayName("Integration - Full CRUD workflow")
    public void testIntegration_FullWorkflow() throws SQLException {
        // Insert
        mealDao.insert(testMeal, testUserId);
        List<Meal> meals = mealDao.findByUserId(testUserId);
        assertEquals(1, meals.size());

        // Update
        testMeal.setCalories(700);
        mealDao.update(testMeal);
        Optional<Meal> updated = mealDao.findById(testMeal.getId());
        assertEquals(700, updated.get().getCalories());

        // Delete
        mealDao.delete(testMeal.getId());
        meals = mealDao.findByUserId(testUserId);
        assertTrue(meals.isEmpty());
    }
}