package com.fitnessapp;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JUnit test class for UserDao
 * Tests all CRUD operations and database interactions
 * Uses a test database that is cleaned before and after each test
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDaoTest {

    private UserDao userDao;
    private User testUser1;
    private User testUser2;

    @BeforeEach
    public void setUp() throws SQLException {
        userDao = new UserDao();

        // Create test users
        testUser1 = new User("John", "Doe", "john@test.com", "1234", 70.0f, 180.0f);
        testUser2 = new User("Jane", "Smith", "jane@test.com", "5678", 65.0f, 140.0f);

        // Clear the users table before each test
        clearUsersTable();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up after each test
        clearUsersTable();
    }

    /**
     * Helper method to clear all users from the database
     */
    private void clearUsersTable() throws SQLException {
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM users");
        }
    }

    // ========== INSERT Tests ==========

    @Test
    @Order(1)
    @DisplayName("Test insert - user is saved to database")
    public void testInsert_Success() throws SQLException {
        userDao.insert(testUser1);

        Optional<User> found = userDao.findById(testUser1.getId());
        assertTrue(found.isPresent(), "User should be found in database after insert");

        User retrievedUser = found.get();
        assertEquals(testUser1.getId(), retrievedUser.getId());
        assertEquals("John", retrievedUser.getFirstName());
        assertEquals("Doe", retrievedUser.getLastName());
        assertEquals("john@test.com", retrievedUser.getEmail());
        assertEquals("1234", retrievedUser.getPin());
        assertEquals(70.0f, retrievedUser.getHeight());
        assertEquals(180.0f, retrievedUser.getWeight());
    }

    @Test
    @Order(2)
    @DisplayName("Test insert - multiple users")
    public void testInsert_MultipleUsers() throws SQLException {
        userDao.insert(testUser1);
        userDao.insert(testUser2);

        List<User> allUsers = userDao.findAll();
        assertEquals(2, allUsers.size(), "Database should contain 2 users");
    }

    @Test
    @Order(3)
    @DisplayName("Test insert - user with weight goal")
    public void testInsert_WithWeightGoal() throws SQLException {
        testUser1.setWeightGoal(175.0f);
        userDao.insert(testUser1);

        Optional<User> found = userDao.findById(testUser1.getId());
        assertTrue(found.isPresent());
        assertEquals(175.0f, found.get().getWeightGoal(), "Weight goal should be saved");
    }

    @Test
    @Order(4)
    @DisplayName("Test insert - duplicate email should fail")
    public void testInsert_DuplicateEmail() throws SQLException {
        userDao.insert(testUser1);

        User duplicateUser = new User("Bob", "Johnson", "john@test.com", "9999", 72.0f, 200.0f);

        assertThrows(SQLException.class, () -> {
            userDao.insert(duplicateUser);
        }, "Inserting duplicate email should throw SQLException");
    }

    // ========== FIND BY ID Tests ==========

    @Test
    @Order(5)
    @DisplayName("Test findById - existing user")
    public void testFindById_ExistingUser() throws SQLException {
        userDao.insert(testUser1);

        Optional<User> found = userDao.findById(testUser1.getId());
        assertTrue(found.isPresent(), "User should be found");
        assertEquals(testUser1.getId(), found.get().getId());
    }

    @Test
    @Order(6)
    @DisplayName("Test findById - non-existing user")
    public void testFindById_NonExistingUser() throws SQLException {
        UUID randomId = UUID.randomUUID();

        Optional<User> found = userDao.findById(randomId);
        assertFalse(found.isPresent(), "Non-existing user should return empty Optional");
    }

    @Test
    @Order(7)
    @DisplayName("Test findById - returns correct user data")
    public void testFindById_CorrectData() throws SQLException {
        userDao.insert(testUser1);

        Optional<User> found = userDao.findById(testUser1.getId());
        assertTrue(found.isPresent());

        User user = found.get();
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john@test.com", user.getEmail());
        assertEquals("1234", user.getPin());
        assertEquals(70.0f, user.getHeight());
        assertEquals(180.0f, user.getWeight());
    }

    // ========== FIND BY EMAIL Tests ==========

    @Test
    @Order(8)
    @DisplayName("Test findByEmail - existing user")
    public void testFindByEmail_ExistingUser() throws SQLException {
        userDao.insert(testUser1);

        Optional<User> found = userDao.findByEmail("john@test.com");
        assertTrue(found.isPresent(), "User should be found by email");
        assertEquals(testUser1.getId(), found.get().getId());
    }

    @Test
    @Order(9)
    @DisplayName("Test findByEmail - non-existing email")
    public void testFindByEmail_NonExistingEmail() throws SQLException {
        Optional<User> found = userDao.findByEmail("nonexistent@test.com");
        assertFalse(found.isPresent(), "Non-existing email should return empty Optional");
    }

    @Test
    @Order(10)
    @DisplayName("Test findByEmail - case sensitivity")
    public void testFindByEmail_CaseSensitive() throws SQLException {
        userDao.insert(testUser1);

        Optional<User> found = userDao.findByEmail("JOHN@TEST.COM");
        assertFalse(found.isPresent(), "Email search should be case-sensitive");
    }

    @Test
    @Order(11)
    @DisplayName("Test findByEmail - returns correct user")
    public void testFindByEmail_CorrectUser() throws SQLException {
        userDao.insert(testUser1);
        userDao.insert(testUser2);

        Optional<User> found = userDao.findByEmail("jane@test.com");
        assertTrue(found.isPresent());
        assertEquals("Jane", found.get().getFirstName());
        assertEquals("Smith", found.get().getLastName());
    }

    // ========== FIND ALL Tests ==========

    @Test
    @Order(12)
    @DisplayName("Test findAll - empty database")
    public void testFindAll_EmptyDatabase() throws SQLException {
        List<User> users = userDao.findAll();
        assertTrue(users.isEmpty(), "Empty database should return empty list");
    }

    @Test
    @Order(13)
    @DisplayName("Test findAll - single user")
    public void testFindAll_SingleUser() throws SQLException {
        userDao.insert(testUser1);

        List<User> users = userDao.findAll();
        assertEquals(1, users.size());
        assertEquals(testUser1.getId(), users.get(0).getId());
    }

    @Test
    @Order(14)
    @DisplayName("Test findAll - multiple users")
    public void testFindAll_MultipleUsers() throws SQLException {
        userDao.insert(testUser1);
        userDao.insert(testUser2);

        List<User> users = userDao.findAll();
        assertEquals(2, users.size());
    }

    @Test
    @Order(15)
    @DisplayName("Test findAll - ordered by first and last name")
    public void testFindAll_OrderedByName() throws SQLException {
        User alice = new User("Alice", "Williams", "alice@test.com", "1111", 68.0f, 150.0f);
        User bob = new User("Bob", "Johnson", "bob@test.com", "2222", 72.0f, 200.0f);

        userDao.insert(bob);
        userDao.insert(alice);

        List<User> users = userDao.findAll();
        assertEquals(2, users.size());
        assertEquals("Alice", users.get(0).getFirstName(), "First user should be Alice");
        assertEquals("Bob", users.get(1).getFirstName(), "Second user should be Bob");
    }

    // ========== UPDATE Tests ==========

    @Test
    @Order(16)
    @DisplayName("Test update - first name")
    public void testUpdate_FirstName() throws SQLException {
        userDao.insert(testUser1);

        testUser1.setFirstName("Johnny");
        userDao.update(testUser1);

        Optional<User> found = userDao.findById(testUser1.getId());
        assertTrue(found.isPresent());
        assertEquals("Johnny", found.get().getFirstName());
    }

    @Test
    @Order(17)
    @DisplayName("Test update - last name")
    public void testUpdate_LastName() throws SQLException {
        userDao.insert(testUser1);

        testUser1.setLastName("Smith");
        userDao.update(testUser1);

        Optional<User> found = userDao.findById(testUser1.getId());
        assertTrue(found.isPresent());
        assertEquals("Smith", found.get().getLastName());
    }

    @Test
    @Order(18)
    @DisplayName("Test update - email")
    public void testUpdate_Email() throws SQLException {
        userDao.insert(testUser1);

        testUser1.setEmail("newemail@test.com");
        userDao.update(testUser1);

        Optional<User> found = userDao.findById(testUser1.getId());
        assertTrue(found.isPresent());
        assertEquals("newemail@test.com", found.get().getEmail());
    }

    @Test
    @Order(19)
    @DisplayName("Test update - height and weight")
    public void testUpdate_HeightAndWeight() throws SQLException {
        userDao.insert(testUser1);

        testUser1.setHeight(72.0f);
        testUser1.setWeight(190.0f);
        userDao.update(testUser1);

        Optional<User> found = userDao.findById(testUser1.getId());
        assertTrue(found.isPresent());
        assertEquals(72.0f, found.get().getHeight());
        assertEquals(190.0f, found.get().getWeight());
    }

    @Test
    @Order(20)
    @DisplayName("Test update - weight goal")
    public void testUpdate_WeightGoal() throws SQLException {
        userDao.insert(testUser1);

        testUser1.setWeightGoal(175.0f);
        userDao.update(testUser1);

        Optional<User> found = userDao.findById(testUser1.getId());
        assertTrue(found.isPresent());
        assertEquals(175.0f, found.get().getWeightGoal());
    }

    @Test
    @Order(21)
    @DisplayName("Test update - PIN")
    public void testUpdate_Pin() throws SQLException {
        userDao.insert(testUser1);

        testUser1.setPin("9999");
        userDao.update(testUser1);

        Optional<User> found = userDao.findById(testUser1.getId());
        assertTrue(found.isPresent());
        assertEquals("9999", found.get().getPin());
    }

    @Test
    @Order(22)
    @DisplayName("Test update - multiple fields at once")
    public void testUpdate_MultipleFields() throws SQLException {
        userDao.insert(testUser1);

        testUser1.setFirstName("Johnny");
        testUser1.setLastName("Smith");
        testUser1.setHeight(75.0f);
        testUser1.setWeight(200.0f);
        testUser1.setWeightGoal(190.0f);
        userDao.update(testUser1);

        Optional<User> found = userDao.findById(testUser1.getId());
        assertTrue(found.isPresent());
        User updated = found.get();
        assertEquals("Johnny", updated.getFirstName());
        assertEquals("Smith", updated.getLastName());
        assertEquals(75.0f, updated.getHeight());
        assertEquals(200.0f, updated.getWeight());
        assertEquals(190.0f, updated.getWeightGoal());
    }

    @Test
    @Order(23)
    @DisplayName("Test update - non-existing user")
    public void testUpdate_NonExistingUser() throws SQLException {
        User nonExistingUser = new User("Ghost", "User", "ghost@test.com", "0000", 70.0f, 180.0f);

        // Update should not throw exception, but won't affect any rows
        assertDoesNotThrow(() -> userDao.update(nonExistingUser));

        Optional<User> found = userDao.findById(nonExistingUser.getId());
        assertFalse(found.isPresent(), "Non-existing user should not be in database");
    }

    // ========== DELETE Tests ==========

    @Test
    @Order(24)
    @DisplayName("Test delete - existing user")
    public void testDelete_ExistingUser() throws SQLException {
        userDao.insert(testUser1);

        Optional<User> foundBefore = userDao.findById(testUser1.getId());
        assertTrue(foundBefore.isPresent(), "User should exist before delete");

        userDao.delete(testUser1.getId());

        Optional<User> foundAfter = userDao.findById(testUser1.getId());
        assertFalse(foundAfter.isPresent(), "User should not exist after delete");
    }

    @Test
    @Order(25)
    @DisplayName("Test delete - non-existing user")
    public void testDelete_NonExistingUser() throws SQLException {
        UUID randomId = UUID.randomUUID();

        // Delete should not throw exception even if user doesn't exist
        assertDoesNotThrow(() -> userDao.delete(randomId));
    }

    @Test
    @Order(26)
    @DisplayName("Test delete - verify count decreases")
    public void testDelete_CountDecreases() throws SQLException {
        userDao.insert(testUser1);
        userDao.insert(testUser2);

        List<User> beforeDelete = userDao.findAll();
        assertEquals(2, beforeDelete.size());

        userDao.delete(testUser1.getId());

        List<User> afterDelete = userDao.findAll();
        assertEquals(1, afterDelete.size());
    }

    @Test
    @Order(27)
    @DisplayName("Test delete - correct user is deleted")
    public void testDelete_CorrectUser() throws SQLException {
        userDao.insert(testUser1);
        userDao.insert(testUser2);

        userDao.delete(testUser1.getId());

        Optional<User> user1 = userDao.findById(testUser1.getId());
        Optional<User> user2 = userDao.findById(testUser2.getId());

        assertFalse(user1.isPresent(), "User1 should be deleted");
        assertTrue(user2.isPresent(), "User2 should still exist");
    }

    // ========== VALIDATE PIN Tests ==========

    @Test
    @Order(28)
    @DisplayName("Test validatePin - correct PIN")
    public void testValidatePin_CorrectPin() throws SQLException {
        userDao.insert(testUser1);

        boolean isValid = userDao.validatePin(testUser1.getId(), "1234");
        assertTrue(isValid, "Correct PIN should return true");
    }

    @Test
    @Order(29)
    @DisplayName("Test validatePin - incorrect PIN")
    public void testValidatePin_IncorrectPin() throws SQLException {
        userDao.insert(testUser1);

        boolean isValid = userDao.validatePin(testUser1.getId(), "9999");
        assertFalse(isValid, "Incorrect PIN should return false");
    }

    @Test
    @Order(30)
    @DisplayName("Test validatePin - non-existing user")
    public void testValidatePin_NonExistingUser() throws SQLException {
        UUID randomId = UUID.randomUUID();

        boolean isValid = userDao.validatePin(randomId, "1234");
        assertFalse(isValid, "Non-existing user should return false");
    }

    @Test
    @Order(31)
    @DisplayName("Test validatePin - empty PIN")
    public void testValidatePin_EmptyPin() throws SQLException {
        userDao.insert(testUser1);

        boolean isValid = userDao.validatePin(testUser1.getId(), "");
        assertFalse(isValid, "Empty PIN should return false");
    }

    @Test
    @Order(32)
    @DisplayName("Test validatePin - after PIN update")
    public void testValidatePin_AfterUpdate() throws SQLException {
        userDao.insert(testUser1);

        testUser1.setPin("5555");
        userDao.update(testUser1);

        boolean oldPinValid = userDao.validatePin(testUser1.getId(), "1234");
        boolean newPinValid = userDao.validatePin(testUser1.getId(), "5555");

        assertFalse(oldPinValid, "Old PIN should not be valid");
        assertTrue(newPinValid, "New PIN should be valid");
    }

    // ========== Integration Tests ==========

    @Test
    @Order(33)
    @DisplayName("Integration - Insert, Find, Update, Delete workflow")
    public void testIntegration_FullWorkflow() throws SQLException {
        // Insert
        userDao.insert(testUser1);
        Optional<User> found = userDao.findById(testUser1.getId());
        assertTrue(found.isPresent());

        // Update
        testUser1.setWeight(200.0f);
        userDao.update(testUser1);
        found = userDao.findById(testUser1.getId());
        assertEquals(200.0f, found.get().getWeight());

        // Delete
        userDao.delete(testUser1.getId());
        found = userDao.findById(testUser1.getId());
        assertFalse(found.isPresent());
    }

    @Test
    @Order(34)
    @DisplayName("Integration - Multiple users workflow")
    public void testIntegration_MultipleUsers() throws SQLException {
        userDao.insert(testUser1);
        userDao.insert(testUser2);

        List<User> allUsers = userDao.findAll();
        assertEquals(2, allUsers.size());

        Optional<User> user1 = userDao.findByEmail("john@test.com");
        Optional<User> user2 = userDao.findByEmail("jane@test.com");

        assertTrue(user1.isPresent());
        assertTrue(user2.isPresent());

        userDao.delete(user1.get().getId());
        allUsers = userDao.findAll();
        assertEquals(1, allUsers.size());
    }
}