package com.fitnessapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.UUID;

/**
 * JUnit test class for Meal
 * Tests all methods including constructors, getters, setters, and macro calculations
 */
public class MealTest {

    private Meal testMeal;

    @BeforeEach
    public void setUp() {
        testMeal = new Meal("Breakfast");
    }

    // ========== Constructor Tests ==========

    @Test
    @DisplayName("Test simple constructor initializes fields correctly")
    public void testSimpleConstructor() {
        Meal meal = new Meal("Lunch");

        assertNotNull(meal.getId(), "ID should not be null");
        assertNotNull(meal.getDate(), "Date should not be null");
        assertEquals("Lunch", meal.getName());
        assertEquals(0, meal.getCalories(), "Calories should default to 0");
        assertEquals(0.0, meal.getProtein(), "Protein should default to 0.0");
        assertEquals(0.0, meal.getCarbs(), "Carbs should default to 0.0");
        assertEquals(0.0, meal.getFat(), "Fat should default to 0.0");
    }

    @Test
    @DisplayName("Test constructor with UUID auto-generates ID")
    public void testConstructor_GeneratesUUID() {
        Meal meal1 = new Meal("Meal1");
        Meal meal2 = new Meal("Meal2");

        assertNotEquals(meal1.getId(), meal2.getId(), "Each meal should have unique UUID");
    }

    @Test
    @DisplayName("Test constructor sets current date")
    public void testConstructor_SetsCurrentDate() {
        Date before = new Date();
        Meal meal = new Meal("Test Meal");
        Date after = new Date();

        assertNotNull(meal.getDate());
        assertTrue(meal.getDate().getTime() >= before.getTime(), "Date should be at or after creation start");
        assertTrue(meal.getDate().getTime() <= after.getTime(), "Date should be at or before creation end");
    }

    @Test
    @DisplayName("Test full constructor with all parameters")
    public void testFullConstructor() {
        UUID customId = UUID.randomUUID();
        Date customDate = new Date();

        Meal meal = new Meal(customId, customDate, "Dinner", 600, 40.0, 70.0, 20.0);

        assertEquals(customId, meal.getId());
        assertEquals(customDate, meal.getDate());
        assertEquals("Dinner", meal.getName());
        assertEquals(600, meal.getCalories());
        assertEquals(40.0, meal.getProtein());
        assertEquals(70.0, meal.getCarbs());
        assertEquals(20.0, meal.getFat());
    }

    // ========== Getter Tests ==========

    @Test
    @DisplayName("Test getId returns UUID")
    public void testGetId() {
        assertNotNull(testMeal.getId());
        assertTrue(testMeal.getId() instanceof UUID);
    }

    @Test
    @DisplayName("Test getDate returns Date object")
    public void testGetDate() {
        assertNotNull(testMeal.getDate());
        assertTrue(testMeal.getDate() instanceof Date);
    }

    @Test
    @DisplayName("Test getName")
    public void testGetName() {
        assertEquals("Breakfast", testMeal.getName());
    }

    @Test
    @DisplayName("Test getCalories default value")
    public void testGetCalories_Default() {
        assertEquals(0, testMeal.getCalories());
    }

    @Test
    @DisplayName("Test getProtein default value")
    public void testGetProtein_Default() {
        assertEquals(0.0, testMeal.getProtein());
    }

    @Test
    @DisplayName("Test getCarbs default value")
    public void testGetCarbs_Default() {
        assertEquals(0.0, testMeal.getCarbs());
    }

    @Test
    @DisplayName("Test getFat default value")
    public void testGetFat_Default() {
        assertEquals(0.0, testMeal.getFat());
    }

    // ========== Setter Tests ==========

    @Test
    @DisplayName("Test setDate")
    public void testSetDate() {
        Date newDate = new Date(System.currentTimeMillis() - 86400000); // Yesterday
        testMeal.setDate(newDate);
        assertEquals(newDate, testMeal.getDate());
    }

    @Test
    @DisplayName("Test setName")
    public void testSetName() {
        testMeal.setName("Brunch");
        assertEquals("Brunch", testMeal.getName());
    }

    @Test
    @DisplayName("Test setCalories")
    public void testSetCalories() {
        testMeal.setCalories(500);
        assertEquals(500, testMeal.getCalories());
    }

    @Test
    @DisplayName("Test setCalories with zero")
    public void testSetCalories_Zero() {
        testMeal.setCalories(100);
        testMeal.setCalories(0);
        assertEquals(0, testMeal.getCalories());
    }

    @Test
    @DisplayName("Test setCalories with negative value")
    public void testSetCalories_Negative() {
        testMeal.setCalories(-50);
        assertEquals(-50, testMeal.getCalories(), "Setter allows negative (validation should be in UI/service layer)");
    }

    @Test
    @DisplayName("Test setProtein")
    public void testSetProtein() {
        testMeal.setProtein(30.5);
        assertEquals(30.5, testMeal.getProtein());
    }

    @Test
    @DisplayName("Test setProtein with zero")
    public void testSetProtein_Zero() {
        testMeal.setProtein(0.0);
        assertEquals(0.0, testMeal.getProtein());
    }

    @Test
    @DisplayName("Test setProtein with decimal values")
    public void testSetProtein_Decimal() {
        testMeal.setProtein(25.75);
        assertEquals(25.75, testMeal.getProtein(), 0.001);
    }

    @Test
    @DisplayName("Test setCarbs")
    public void testSetCarbs() {
        testMeal.setCarbs(45.0);
        assertEquals(45.0, testMeal.getCarbs());
    }

    @Test
    @DisplayName("Test setCarbs with decimal values")
    public void testSetCarbs_Decimal() {
        testMeal.setCarbs(52.3);
        assertEquals(52.3, testMeal.getCarbs(), 0.001);
    }

    @Test
    @DisplayName("Test setFat")
    public void testSetFat() {
        testMeal.setFat(15.5);
        assertEquals(15.5, testMeal.getFat());
    }

    @Test
    @DisplayName("Test setFat with decimal values")
    public void testSetFat_Decimal() {
        testMeal.setFat(18.25);
        assertEquals(18.25, testMeal.getFat(), 0.001);
    }

    // ========== getTotalMacros Tests ==========

    @Test
    @DisplayName("Test getTotalMacros with default values")
    public void testGetTotalMacros_DefaultValues() {
        String macros = testMeal.getTotalMacros();
        assertEquals("Protein: 0.0g, Fat: 0.0g, Carbs: 0.0g", macros);
    }

    @Test
    @DisplayName("Test getTotalMacros with set values")
    public void testGetTotalMacros_WithValues() {
        testMeal.setProtein(30.0);
        testMeal.setFat(20.0);
        testMeal.setCarbs(50.0);

        String macros = testMeal.getTotalMacros();
        assertEquals("Protein: 30.0g, Fat: 20.0g, Carbs: 50.0g", macros);
    }

    @Test
    @DisplayName("Test getTotalMacros format")
    public void testGetTotalMacros_Format() {
        testMeal.setProtein(25.5);
        testMeal.setFat(15.3);
        testMeal.setCarbs(45.7);

        String macros = testMeal.getTotalMacros();
        assertTrue(macros.contains("Protein: 25.5g"));
        assertTrue(macros.contains("Fat: 15.3g"));
        assertTrue(macros.contains("Carbs: 45.7g"));
    }

    @Test
    @DisplayName("Test getTotalMacros with decimal precision")
    public void testGetTotalMacros_DecimalPrecision() {
        testMeal.setProtein(33.33);
        testMeal.setFat(22.22);
        testMeal.setCarbs(44.44);

        String macros = testMeal.getTotalMacros();
        assertTrue(macros.contains("33.33"));
        assertTrue(macros.contains("22.22"));
        assertTrue(macros.contains("44.44"));
    }

    // ========== toString Tests ==========

    @Test
    @DisplayName("Test toString with default values")
    public void testToString_DefaultValues() {
        String result = testMeal.toString();
        assertEquals("Breakfast - 0 calories (0.0g protein, 0.0g carbs, 0.0g fat)", result);
    }

    @Test
    @DisplayName("Test toString with set values")
    public void testToString_WithValues() {
        testMeal.setCalories(500);
        testMeal.setProtein(30.0);
        testMeal.setCarbs(60.0);
        testMeal.setFat(15.0);

        String result = testMeal.toString();
        assertEquals("Breakfast - 500 calories (30.0g protein, 60.0g carbs, 15.0g fat)", result);
    }

    @Test
    @DisplayName("Test toString contains meal name")
    public void testToString_ContainsName() {
        testMeal.setName("Lunch");
        String result = testMeal.toString();
        assertTrue(result.startsWith("Lunch"));
    }

    @Test
    @DisplayName("Test toString contains calories")
    public void testToString_ContainsCalories() {
        testMeal.setCalories(350);
        String result = testMeal.toString();
        assertTrue(result.contains("350 calories"));
    }

    @Test
    @DisplayName("Test toString format")
    public void testToString_Format() {
        testMeal.setCalories(400);
        testMeal.setProtein(25.0);
        testMeal.setCarbs(50.0);
        testMeal.setFat(12.0);

        String result = testMeal.toString();
        assertTrue(result.contains(" - "), "Should contain separator");
        assertTrue(result.contains("calories"), "Should contain 'calories'");
        assertTrue(result.contains("protein"), "Should contain 'protein'");
        assertTrue(result.contains("carbs"), "Should contain 'carbs'");
        assertTrue(result.contains("fat"), "Should contain 'fat'");
    }

    // ========== Integration/Complex Tests ==========

    @Test
    @DisplayName("Test setting all nutritional values")
    public void testSetAllNutrition() {
        testMeal.setCalories(600);
        testMeal.setProtein(40.0);
        testMeal.setCarbs(70.0);
        testMeal.setFat(20.0);

        assertEquals(600, testMeal.getCalories());
        assertEquals(40.0, testMeal.getProtein());
        assertEquals(70.0, testMeal.getCarbs());
        assertEquals(20.0, testMeal.getFat());
    }

    @Test
    @DisplayName("Test modifying meal after creation")
    public void testModifyMealAfterCreation() {
        // Initially empty meal
        assertEquals(0, testMeal.getCalories());

        // Add nutrition info
        testMeal.setCalories(450);
        testMeal.setProtein(35.0);
        testMeal.setCarbs(55.0);
        testMeal.setFat(12.0);

        // Verify changes
        assertEquals(450, testMeal.getCalories());
        assertEquals(35.0, testMeal.getProtein());
        assertEquals(55.0, testMeal.getCarbs());
        assertEquals(12.0, testMeal.getFat());
    }

    @Test
    @DisplayName("Test meal with realistic values")
    public void testRealisticMeal() {
        Meal meal = new Meal("Chicken and Rice");
        meal.setCalories(650);
        meal.setProtein(45.0);
        meal.setCarbs(75.0);
        meal.setFat(18.0);

        assertEquals("Chicken and Rice", meal.getName());
        assertEquals(650, meal.getCalories());
        assertEquals(45.0, meal.getProtein());
        assertEquals(75.0, meal.getCarbs());
        assertEquals(18.0, meal.getFat());

        String macros = meal.getTotalMacros();
        assertTrue(macros.contains("45.0"));
        assertTrue(macros.contains("75.0"));
        assertTrue(macros.contains("18.0"));
    }

    @Test
    @DisplayName("Test updating meal name")
    public void testUpdateMealName() {
        assertEquals("Breakfast", testMeal.getName());

        testMeal.setName("Early Morning Snack");
        assertEquals("Early Morning Snack", testMeal.getName());
    }

    @Test
    @DisplayName("Test meal with zero macros but calories")
    public void testMealWithCaloriesButNoMacros() {
        testMeal.setCalories(100);
        testMeal.setProtein(0.0);
        testMeal.setCarbs(0.0);
        testMeal.setFat(0.0);

        assertEquals(100, testMeal.getCalories());
        String result = testMeal.toString();
        assertTrue(result.contains("100 calories"));
    }

    @Test
    @DisplayName("Test meal date persistence")
    public void testMealDatePersistence() {
        Date originalDate = testMeal.getDate();

        // Simulate some time passing
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // Ignore
        }

        Date retrievedDate = testMeal.getDate();
        assertEquals(originalDate, retrievedDate, "Date should remain the same");
    }

    @Test
    @DisplayName("Test multiple meals have unique IDs")
    public void testMultipleMealsUniqueIds() {
        Meal meal1 = new Meal("Meal 1");
        Meal meal2 = new Meal("Meal 2");
        Meal meal3 = new Meal("Meal 3");

        assertNotEquals(meal1.getId(), meal2.getId());
        assertNotEquals(meal1.getId(), meal3.getId());
        assertNotEquals(meal2.getId(), meal3.getId());
    }

    @Test
    @DisplayName("Test meal with high protein values")
    public void testHighProteinMeal() {
        testMeal.setProtein(100.0);
        assertEquals(100.0, testMeal.getProtein());

        String macros = testMeal.getTotalMacros();
        assertTrue(macros.contains("100.0g"));
    }

    @Test
    @DisplayName("Test meal with fractional macro values")
    public void testFractionalMacros() {
        testMeal.setProtein(33.33);
        testMeal.setCarbs(66.67);
        testMeal.setFat(11.11);

        assertEquals(33.33, testMeal.getProtein(), 0.001);
        assertEquals(66.67, testMeal.getCarbs(), 0.001);
        assertEquals(11.11, testMeal.getFat(), 0.001);
    }
}