package com.fitnessapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

/**
 * JUnit test class for User
 * Tests constructors, BMI calculation, updateStats, and key methods
 */
public class UserTest {

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User("John", "Doe", "john@test.com", "1234", 70.0f, 180.0f);
    }

    // ========== Constructor Tests ==========

    @Test
    @DisplayName("Test default constructor initializes fields")
    public void testDefaultConstructor() {
        User user = new User();

        assertNotNull(user.getId());
        assertEquals("", user.getFirstName());
        assertEquals("", user.getLastName());
        assertEquals(0.0f, user.getHeight());
        assertEquals(0.0f, user.getWeight());
        assertEquals(0.0f, user.getWeightGoal());
    }

    @Test
    @DisplayName("Test parameterized constructor")
    public void testParameterizedConstructor() {
        assertEquals("John", testUser.getFirstName());
        assertEquals("Doe", testUser.getLastName());
        assertEquals("john@test.com", testUser.getEmail());
        assertEquals("1234", testUser.getPin());
        assertEquals(70.0f, testUser.getHeight());
        assertEquals(180.0f, testUser.getWeight());
    }

    // ========== BMI Calculation Tests ==========

    @Test
    @DisplayName("Test BMI calculation with valid inputs")
    public void testGetBMI_ValidInputs() {
        // Height: 70 inches, Weight: 180 pounds
        // BMI = (180 / (70 * 70)) * 703 = 25.82
        float bmi = testUser.getBMI();
        assertEquals(25.82f, bmi, 0.01f);
    }

    @Test
    @DisplayName("Test BMI calculation with zero height")
    public void testGetBMI_ZeroHeight() {
        testUser.setHeight(0.0f);
        assertEquals(0.0f, testUser.getBMI());
    }

    @Test
    @DisplayName("Test BMI calculation with negative height")
    public void testGetBMI_NegativeHeight() {
        testUser.setHeight(-5.0f);
        assertEquals(0.0f, testUser.getBMI());
    }

    // ========== updateStats Tests ==========

    @Test
    @DisplayName("Test updateStats with valid inputs")
    public void testUpdateStats_ValidInputs() {
        int result = testUser.updateStats(72.0f, 190.0f);

        assertEquals(0, result, "Should return 0 for success");
        assertEquals(72.0f, testUser.getHeight());
        assertEquals(190.0f, testUser.getWeight());
    }

    @Test
    @DisplayName("Test updateStats with negative height")
    public void testUpdateStats_NegativeHeight() {
        int result = testUser.updateStats(-5.0f, 180.0f);

        assertEquals(-1, result, "Should return -1 for failure");
        assertEquals(70.0f, testUser.getHeight(), "Height should not change");
    }

    @Test
    @DisplayName("Test updateStats with negative weight")
    public void testUpdateStats_NegativeWeight() {
        int result = testUser.updateStats(70.0f, -10.0f);

        assertEquals(-1, result, "Should return -1 for failure");
        assertEquals(180.0f, testUser.getWeight(), "Weight should not change");
    }

    @Test
    @DisplayName("Test updateStats with zero values")
    public void testUpdateStats_ZeroValues() {
        int result = testUser.updateStats(0.0f, 0.0f);
        assertEquals(-1, result, "Should return -1 for zero values");
    }

    // ========== Getter/Setter Tests ==========

    @Test
    @DisplayName("Test getFullName concatenates first and last name")
    public void testGetFullName() {
        assertEquals("John Doe", testUser.getFullName());
    }

    @Test
    @DisplayName("Test setWeightGoal and getWeightGoal")
    public void testWeightGoal() {
        testUser.setWeightGoal(175.0f);
        assertEquals(175.0f, testUser.getWeightGoal());
    }

    // ========== equals() and hashCode() Tests ==========

    @Test
    @DisplayName("Test equals with same UUID")
    public void testEquals_SameUUID() {
        UUID sharedId = UUID.randomUUID();
        User user1 = new User(sharedId, "John", "Doe", "john@test.com", "1234", 70.0f, 180.0f);
        User user2 = new User(sharedId, "Jane", "Smith", "jane@test.com", "5678", 65.0f, 140.0f);

        assertTrue(user1.equals(user2), "Users with same UUID should be equal");
    }

    @Test
    @DisplayName("Test equals with different UUID")
    public void testEquals_DifferentUUID() {
        User user1 = new User("John", "Doe", "john@test.com", "1234", 70.0f, 180.0f);
        User user2 = new User("John", "Doe", "john@test.com", "1234", 70.0f, 180.0f);

        assertFalse(user1.equals(user2), "Users with different UUIDs should not be equal");
    }

    @Test
    @DisplayName("Test hashCode with same UUID")
    public void testHashCode_SameUUID() {
        UUID sharedId = UUID.randomUUID();
        User user1 = new User(sharedId, "John", "Doe", "john@test.com", "1234", 70.0f, 180.0f);
        User user2 = new User(sharedId, "Jane", "Smith", "jane@test.com", "5678", 65.0f, 140.0f);

        assertEquals(user1.hashCode(), user2.hashCode(), "Same UUID should have same hashCode");
    }

    // ========== Integration Test ==========

    @Test
    @DisplayName("Test BMI updates when weight changes")
    public void testBMI_UpdatesWithWeight() {
        float initialBMI = testUser.getBMI();

        testUser.setWeight(200.0f);
        float newBMI = testUser.getBMI();

        assertTrue(newBMI > initialBMI, "BMI should increase when weight increases");
    }
}