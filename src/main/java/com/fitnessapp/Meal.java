package com.fitnessapp;

import java.util.UUID;
import java.util.Date;

/**
 * Represents a meal with nutritional information such as calories and macronutrients.
 * Each meal has a unique ID, name, date, and macros for tracking.
 */
public class Meal {

    /** Unique identifier for the meal. */
    private final UUID id;

    /** Date when the meal was created or logged. */
    private Date date;

    /** Name of the meal (e.g., "Scrambled Eggs", "Lunch"). */
    private String name;

    /** Total calories in the meal. */
    private int calories;

    /** Total protein in grams. */
    private double protein;

    /** Total carbohydrates in grams. */
    private double carbs;

    /** Total fat in grams. */
    private double fat;

    /**
     * Constructs a new Meal with the specified name.
     * Automatically assigns a unique ID and sets the current date.
     * All nutrition values are initialized to zero by default.
     *
     * @param name the name of the meal
     */
    public Meal(String name) {
        this.id = UUID.randomUUID();
        this.date = new Date();
        this.name = name;
        this.calories = 0;
        this.protein = 0.0;
        this.carbs = 0.0;
        this.fat = 0.0;
    }

    /**
     * Constructor for loading from database.
     * @param id the unique ID
     * @param date the meal date
     * @param name the meal name
     * @param calories total calories
     * @param protein protein in grams
     * @param carbs carbs in grams
     * @param fat fat in grams
     */
    public Meal(UUID id, Date date, String name, int calories, double protein, double carbs, double fat) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    /**
     * Returns a formatted string containing all macronutrient values.
     *
     * @return a string representation of protein, fat, and carbs
     */
    public String getTotalMacros() {
        return "Protein: " + protein + "g, Fat: " + fat + "g, Carbs: " + carbs + "g";
    }

    /**
     * Returns a readable summary of the meal.
     * Automatically called when printing the object.
     *
     * @return a formatted string with meal name, calories, and macros
     */
    @Override
    public String toString() {
        return name + " - " + calories + " calories (" +
                protein + "g protein, " + carbs + "g carbs, " + fat + "g fat)";
    }

    /**
     * Gets the unique ID of the meal.
     * @return the meal ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the date when the meal was created or logged.
     * @return the meal date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date for this meal.
     * @param date the new date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Gets the name of the meal.
     * @return the meal name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the meal.
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the total calories of the meal.
     * @return the calorie count
     */
    public int getCalories() {
        return calories;
    }

    /**
     * Sets the total calories for the meal.
     * @param calories the calorie count
     */
    public void setCalories(int calories) {
        this.calories = calories;
    }

    /**
     * Gets the amount of protein in grams.
     * @return the protein value
     */
    public double getProtein() {
        return protein;
    }

    /**
     * Sets the amount of protein in grams.
     * @param protein the protein value
     */
    public void setProtein(double protein) {
        this.protein = protein;
    }

    /**
     * Gets the amount of carbohydrates in grams.
     * @return the carbohydrate value
     */
    public double getCarbs() {
        return carbs;
    }

    /**
     * Sets the amount of carbohydrates in grams.
     * @param carbs the carbohydrate value
     */
    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    /**
     * Gets the amount of fat in grams.
     * @return the fat value
     */
    public double getFat() {
        return fat;
    }

    /**
     * Sets the amount of fat in grams.
     * @param fat the fat value
     */
    public void setFat(double fat) {
        this.fat = fat;
    }
}
