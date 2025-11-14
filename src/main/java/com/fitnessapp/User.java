package com.fitnessapp;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the fitness app
 * Stores users information including physical stats and provides
 * methods for BMI calculation and stat tracking.
 */
public class User {

    // Attributes
    private final UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private float height;
    private float weight;
    private String pin;

    /**
     * Creates a User with auto-generated UUID.
     * Initializes all fields to default values.
     */
    public User() {
        this.id = UUID.randomUUID();
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.height = 0.0f;
        this.weight = 0.0f;
        this.pin = "";
    }

    /**
     * Constructor that creates a User with specified details.
     * Automatically generates a unique UUID for the user.
     *
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @param email The email address for login
     * @param pin The 4-digit PIN for login
     * @param height The height of the user
     * @param weight The weight of the user
     */
    public User(String firstName, String lastName, String email, String pin, float height, float weight) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pin = pin;
        this.height = height;
        this.weight = weight;
    }

    /**
     * Constructor that recreates a User from stored data.
     * Uses the provided UUID instead of generating a new one.
     */
    public User(UUID id, String firstName, String lastName,
                String email, String pin, float height, float weight) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pin = pin;
        this.height = height;
        this.weight = weight;
    }

    /**
     * Updates the user's height and weight statistics.
     * This method allows modification of physical stats for tracking fitness progress.
     * Returns an integer status code (0 for success, -1 for invalid input).
     *
     * @param height The new height value in inches (has to be positive)
     * @param weight The new weight value in pounds (has to be positive)
     * @return int status code: 0 if successful, -1 if input validation fails
     */
    public int updateStats(float height, float weight) {
        if (height <= 0 || weight <= 0) {
            return -1; // Invalid input
        }
        this.height = height;
        this.weight = weight;
        return 0; // Success
    }

    /**
     * Calculates and returns the user's BMI
     * BMI is calculated using the formula: (weight / (height * height)) * 703
     * Uses imperial units: weight in pounds and height in inches.
     * Returns 0.0 if height is invalid
     *
     * @return The calculated BMI value as a float, or 0.0 if height is invalid
     */
    public float getBMI() {
        if (height <= 0) {
            return 0.0f;
        }
        return (weight / (height * height)) * 703;
    }

    // Getters

    public String getPin(){
        return pin;
    }
    /**
     * Retrieves the unique identifier for this user.
     * This UUID is used to associate the user with DailyLog entries.
     *
     * @return The UUID of the user
     */
    public UUID getId() {
        return id;
    }

    /**
     * Retrieves the first name of the user.
     *
     * @return The user's first name as a String
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Retrieves the last name of the user.
     *
     * @return The user's last name as a String
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Retrieves the full name of the user.
     * Concatenates first name and last name with a space.
     *
     * @return The user's full name as a String
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Retrieves the email address of the user.
     *
     * @return The user's email as a String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retrieves the height of the user.
     *
     * @return The user's height in inches as a float
     */
    public float getHeight() {
        return height;
    }

    /**
     * Retrieves the weight of the user.
     *
     * @return The user's weight in pounds as a float
     */
    public float getWeight() {
        return weight;
    }

    // Setters

    public void setPin(String pin){
        this.pin = pin;
    }
    /**
     * Sets the first name of the user.
     *
     * @param firstName The new first name to assign to the user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName The new last name to assign to the user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The new email address to assign to the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the height of the user.
     *
     * @param height The new height value in inches to assign to the user
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Sets the weight of the user.
     *
     * @param weight The new weight value in pounds to assign to the user
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     * Returns a string representation of the User object.
     * Includes all user information and calculated BMI for debugging purposes.
     *
     * @return A formatted string containing user information
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", BMI=" + String.format("%.2f", getBMI()) +
                '}';
    }

    /**
     * Compares this User to another object for equality.
     * Two users are considered equal if they have the same UUID.
     *
     * @param obj The object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id.equals(user.id);
    }

    /**
     * Generates a hash code for the User object.
     * Based on the unique UUID identifier.
     *
     * @return The hash code as an integer
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}