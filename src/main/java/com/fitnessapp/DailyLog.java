package com.fitnessapp;

import java.util.Date;
import java.util.UUID;

/**
 * Represents a daily summary log for a user.
 * Tracks aggregate information like total calories and workouts for a day.
 */
public class DailyLog {

    /** Unique identifier for the daily log. */
    private final UUID id;

    /** Date for this daily log entry. */
    private Date date;

    /** Total calories consumed for the day. */
    private int totalCalories;

    /** Total number of workouts completed for the day. */
    private int totalWorkouts;

    /** Optional notes for the day. */
    private String notes;

    /**
     * Default constructor - creates a new daily log with current date.
     */
    public DailyLog() {
        this.id = UUID.randomUUID();
        this.date = new Date();
        this.totalCalories = 0;
        this.totalWorkouts = 0;
    }

    /**
     * Constructor for loading from database.
     * @param id the unique ID
     * @param date the date
     * @param totalCalories total calories for the day
     * @param totalWorkouts total workouts for the day
     * @param notes optional notes
     */
    public DailyLog(UUID id, Date date, int totalCalories, int totalWorkouts, String notes) {
        this.id = id;
        this.date = date;
        this.totalCalories = totalCalories;
        this.totalWorkouts = totalWorkouts;
        this.notes = notes;
    }

    /**
     * Gets the unique ID of this daily log.
     * @return the daily log ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the date for this daily log.
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date for this daily log.
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Gets the total calories for the day.
     * @return total calories
     */
    public int getTotalCalories() {
        return totalCalories;
    }

    /**
     * Sets the total calories for the day.
     * @param totalCalories the total calories
     */
    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
    }

    /**
     * Gets the total number of workouts for the day.
     * @return total workouts
     */
    public int getTotalWorkouts() {
        return totalWorkouts;
    }

    /**
     * Sets the total number of workouts for the day.
     * @param totalWorkouts the total workouts
     */
    public void setTotalWorkouts(int totalWorkouts) {
        this.totalWorkouts = totalWorkouts;
    }

    /**
     * Gets the notes for this daily log.
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the notes for this daily log.
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
