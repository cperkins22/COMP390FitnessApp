package com.fitnessapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

/**
 * JUnit test class for User
 * Tests all methods including constructors, BMI calculation, updateStats, getters, setters, and utility methods
 */
public class UserTest {

    private User testUser;
    private UUID testId;

    @BeforeEach
    public void setUp() {
        // Initialize a test user before each test
        testUser = new User("John", "Doe", "john@test.com", "1234", 70.0f, 180.0f);
        testId = UUID.randomUUID();
    }

    // ========== Constructor Tests ==========

    @Test
    @DisplayName("Test default constructor initializes fields correctly")
    public void testDefaultConstructor() {
        User user = new User();
        assertNotNull(user.getId(), "ID should not be null");
        assertEquals("", user.getFirstName(), "First name should be empty string");
        assertEquals("", user.getLastName(), "Last name should be empty string");
        assertEquals("", user.getEmail(), "Email should be empty string");
        assertEquals("", user.getPin(), "PIN should be empty string");
        assertEquals(0.0f, user.getHeight(), "Height should be 0.0");
        assertEquals(0.0f, user.getWeight(), "Weight should be 0.0");
        assertEquals(0.0f, user.getWeightGoal(), "Weight goal should be 0.0");
    }

    @Test
    @DisplayName("Test parameterized constructor without UUID")
    public void testParameterizedConstructor() {
        User user = new User("Jane", "Smith", "jane@test.com", "5678", 65.0f, 140.0f);
        assertNotNull(user.getId(), "ID should be auto-generated");
        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("jane@test.com", user.getEmail());
        assertEquals("5678", user.getPin());
        assertEquals(65.0f, user.getHeight());
        assertEquals(140.0f, user.getWeight());
        assertEquals(0.0f, user.getWeightGoal(), "Weight goal should default to 0.0");
    }

    @Test
    @DisplayName("Test constructor with UUID (for database loading)")
    public void testConstructorWithUUID() {
        UUID customId = UUID.randomUUID();
        User user = new User(customId, "Bob", "Johnson", "bob@test.com", "9999", 72.0f, 200.0f);
        assertEquals(customId, user.getId(), "ID should match provided UUID");
        assertEquals("Bob", user.getFirstName());
        assertEquals("Johnson", user.getLastName());
        assertEquals("bob@test.com", user.getEmail());
        assertEquals("9999", user.getPin());
        assertEquals(72.0f, user.getHeight());
        assertEquals(200.0f, user.getWeight());
    }

    @Test
    @DisplayName("Test constructor with UUID and weight goal")
    public void testConstructorWithWeightGoal() {
        UUID customId = UUID.randomUUID();
        User user = new User(customId, "Alice", "Williams", "alice@test.com", "1111", 68.0f, 150.0f, 140.0f);
        assertEquals(customId, user.getId());
        assertEquals("Alice", user.getFirstName());
        assertEquals("Williams", user.getLastName());
        assertEquals(140.0f, user.getWeightGoal(), "Weight goal should be set correctly");
    }

    // ========== BMI Calculation Tests ==========

    @Test
    @DisplayName("Test BMI calculation with valid inputs")
    public void testGetBMI_ValidInputs() {
        // Height: 70 inches, Weight: 180 pounds
        // Expected BMI = (180 / (70 * 70)) * 703 = 25.82
        float bmi = testUser.getBMI();
        assertEquals(25.82f, bmi, 0.01f, "BMI should be approximately 25.82");
    }

    @Test
    @DisplayName("Test BMI calculation with zero height")
    public void testGetBMI_ZeroHeight() {
        testUser.setHeight(0.0f);
        float bmi = testUser.getBMI();
        assertEquals(0.0f, bmi, "BMI should return 0.0 when height is zero");
    }

    @Test
    @DisplayName("Test BMI calculation with negative height")
    public void testGetBMI_NegativeHeight() {
        testUser.setHeight(-5.0f);
        float bmi = testUser.getBMI();
        assertEquals(0.0f, bmi, "BMI should return 0.0 when height is negative");
    }

    @Test
    @DisplayName("Test BMI calculation with different valid values")
    public void testGetBMI_DifferentValues() {
        User user = new User("Test", "User", "test@test.com", "0000", 65.0f, 140.0f);
        // Expected BMI = (140 / (65 * 65)) * 703 = 23.29
        float bmi = user.getBMI();
        assertEquals(23.29f, bmi, 0.01f, "BMI should be approximately 23.29");
    }

    // ========== updateStats Tests ==========

    @Test
    @DisplayName("Test updateStats with valid inputs")
    public void testUpdateStats_ValidInputs() {
        int result = testUser.updateStats(72.0f, 190.0f);
        assertEquals(0, result, "updateStats should return 0 for success");
        assertEquals(72.0f, testUser.getHeight(), "Height should be updated to 72.0");
        assertEquals(190.0f, testUser.getWeight(), "Weight should be updated to 190.0");
    }

    @Test
    @DisplayName("Test updateStats with negative height")
    public void testUpdateStats_NegativeHeight() {
        int result = testUser.updateStats(-5.0f, 180.0f);
        assertEquals(-1, result, "updateStats should return -1 for invalid height");
        assertEquals(70.0f, testUser.getHeight(), "Height should remain unchanged");
        assertEquals(180.0f, testUser.getWeight(), "Weight should remain unchanged");
    }

    @Test
    @DisplayName("Test updateStats with negative weight")
    public void testUpdateStats_NegativeWeight() {
        int result = testUser.updateStats(70.0f, -10.0f);
        assertEquals(-1, result, "updateStats should return -1 for invalid weight");
        assertEquals(70.0f, testUser.getHeight(), "Height should remain unchanged");
        assertEquals(180.0f, testUser.getWeight(), "Weight should remain unchanged");
    }

    @Test
    @DisplayName("Test updateStats with zero height")
    public void testUpdateStats_ZeroHeight() {
        int result = testUser.updateStats(0.0f, 180.0f);
        assertEquals(-1, result, "updateStats should return -1 for zero height");
    }

    @Test
    @DisplayName("Test updateStats with zero weight")
    public void testUpdateStats_ZeroWeight() {
        int result = testUser.updateStats(70.0f, 0.0f);
        assertEquals(-1, result, "updateStats should return -1 for zero weight");
    }

    @Test
    @DisplayName("Test updateStats with both negative values")
    public void testUpdateStats_BothNegative() {
        int result = testUser.updateStats(-5.0f, -10.0f);
        assertEquals(-1, result, "updateStats should return -1 for both negative values");
    }

    // ========== Getter Tests ==========

    @Test
    @DisplayName("Test getId returns non-null UUID")
    public void testGetId() {
        assertNotNull(testUser.getId(), "ID should not be null");
        assertTrue(testUser.getId() instanceof UUID, "ID should be a UUID instance");
    }

    @Test
    @DisplayName("Test getFirstName")
    public void testGetFirstName() {
        assertEquals("John", testUser.getFirstName());
    }

    @Test
    @DisplayName("Test getLastName")
    public void testGetLastName() {
        assertEquals("Doe", testUser.getLastName());
    }

    @Test
    @DisplayName("Test getFullName concatenates first and last name")
    public void testGetFullName() {
        assertEquals("John Doe", testUser.getFullName());
    }

    @Test
    @DisplayName("Test getFullName with empty names")
    public void testGetFullName_EmptyNames() {
        User user = new User();
        assertEquals(" ", user.getFullName(), "Full name should be a space when names are empty");
    }

    @Test
    @DisplayName("Test getEmail")
    public void testGetEmail() {
        assertEquals("john@test.com", testUser.getEmail());
    }

    @Test
    @DisplayName("Test getPin")
    public void testGetPin() {
        assertEquals("1234", testUser.getPin());
    }

    @Test
    @DisplayName("Test getHeight")
    public void testGetHeight() {
        assertEquals(70.0f, testUser.getHeight());
    }

    @Test
    @DisplayName("Test getWeight")
    public void testGetWeight() {
        assertEquals(180.0f, testUser.getWeight());
    }

    @Test
    @DisplayName("Test getWeightGoal")
    public void testGetWeightGoal() {
        assertEquals(0.0f, testUser.getWeightGoal(), "Default weight goal should be 0.0");
    }

    // ========== Setter Tests ==========

    @Test
    @DisplayName("Test setFirstName")
    public void testSetFirstName() {
        testUser.setFirstName("Jane");
        assertEquals("Jane", testUser.getFirstName());
    }

    @Test
    @DisplayName("Test setLastName")
    public void testSetLastName() {
        testUser.setLastName("Smith");
        assertEquals("Smith", testUser.getLastName());
    }

    @Test
    @DisplayName("Test setEmail")
    public void testSetEmail() {
        testUser.setEmail("newemail@test.com");
        assertEquals("newemail@test.com", testUser.getEmail());
    }

    @Test
    @DisplayName("Test setPin")
    public void testSetPin() {
        testUser.setPin("9999");
        assertEquals("9999", testUser.getPin());
    }

    @Test
    @DisplayName("Test setHeight")
    public void testSetHeight() {
        testUser.setHeight(75.0f);
        assertEquals(75.0f, testUser.getHeight());
    }

    @Test
    @DisplayName("Test setWeight")
    public void testSetWeight() {
        testUser.setWeight(200.0f);
        assertEquals(200.0f, testUser.getWeight());
    }

    @Test
    @DisplayName("Test setWeightGoal")
    public void testSetWeightGoal() {
        testUser.setWeightGoal(175.0f);
        assertEquals(175.0f, testUser.getWeightGoal());
    }

    // ========== equals() Tests ==========

    @Test
    @DisplayName("Test equals with same object")
    public void testEquals_SameObject() {
        assertTrue(testUser.equals(testUser), "User should equal itself");
    }

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
    @DisplayName("Test equals with null")
    public void testEquals_Null() {
        assertFalse(testUser.equals(null), "User should not equal null");
    }

    @Test
    @DisplayName("Test equals with different class")
    public void testEquals_DifferentClass() {
        assertFalse(testUser.equals("Not a User"), "User should not equal different class");
    }

    // ========== hashCode() Tests ==========

    @Test
    @DisplayName("Test hashCode consistency")
    public void testHashCode_Consistency() {
        int hash1 = testUser.hashCode();
        int hash2 = testUser.hashCode();
        assertEquals(hash1, hash2, "hashCode should be consistent across calls");
    }

    @Test
    @DisplayName("Test hashCode with same UUID")
    public void testHashCode_SameUUID() {
        UUID sharedId = UUID.randomUUID();
        User user1 = new User(sharedId, "John", "Doe", "john@test.com", "1234", 70.0f, 180.0f);
        User user2 = new User(sharedId, "Jane", "Smith", "jane@test.com", "5678", 65.0f, 140.0f);
        assertEquals(user1.hashCode(), user2.hashCode(), "Users with same UUID should have same hashCode");
    }

    // ========== toString() Tests ==========

    @Test
    @DisplayName("Test toString contains all fields")
    public void testToString() {
        String result = testUser.toString();
        assertTrue(result.contains("John"), "toString should contain first name");
        assertTrue(result.contains("Doe"), "toString should contain last name");
        assertTrue(result.contains("john@test.com"), "toString should contain email");
        assertTrue(result.contains("height=70.0"), "toString should contain height");
        assertTrue(result.contains("weight=180.0"), "toString should contain weight");
        assertTrue(result.contains("BMI="), "toString should contain BMI");
    }

    @Test
    @DisplayName("Test toString format")
    public void testToString_Format() {
        String result = testUser.toString();
        assertTrue(result.startsWith("User{"), "toString should start with 'User{'");
        assertTrue(result.endsWith("}"), "toString should end with '}'");
    }

    // ========== Integration/Edge Case Tests ==========

    @Test
    @DisplayName("Test BMI updates when weight changes")
    public void testBMI_UpdatesWithWeight() {
        float initialBMI = testUser.getBMI();
        testUser.setWeight(200.0f);
        float newBMI = testUser.getBMI();
        assertNotEquals(initialBMI, newBMI, "BMI should change when weight changes");
        assertTrue(newBMI > initialBMI, "BMI should increase when weight increases");
    }

    @Test
    @DisplayName("Test BMI updates when height changes")
    public void testBMI_UpdatesWithHeight() {
        float initialBMI = testUser.getBMI();
        testUser.setHeight(75.0f);
        float newBMI = testUser.getBMI();
        assertNotEquals(initialBMI, newBMI, "BMI should change when height changes");
        assertTrue(newBMI < initialBMI, "BMI should decrease when height increases");
    }

    @Test
    @DisplayName("Test updateStats and BMI calculation together")
    public void testUpdateStats_AndBMI() {
        testUser.updateStats(72.0f, 200.0f);
        float expectedBMI = (200.0f / (72.0f * 72.0f)) * 703;
        assertEquals(expectedBMI, testUser.getBMI(), 0.01f, "BMI should reflect updated stats");
    }
}