package com.fitnessapp;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Represents a workout session containing multiple exercises.
 */
public class Workout {
    /** Unique identifier for the workout. */
    private UUID id;

    /** Date of the workout. */
    private Date date;

    /** Optional notes about the workout. */
    private String notes;

    /** List of exercises included in the workout. */
    private List<Exercise> exercises;

    /** Default constructor. */
    public Workout() {
    }

    /**
     * Gets the workout ID.
     * @return the workout ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the workout date.
     * @return the workout date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the workout date.
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Gets any notes associated with the workout.
     * @return the workout notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets notes for the workout.
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Gets the list of exercises in this workout.
     * @return the list of exercises
     */
    public List<Exercise> getExercises() {
        return exercises;
    }

    /**
     * Sets the list of exercises for this workout.
     * @param exercises the list of exercises to set
     */
    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    /**
     * Adds a single exercise to the workout.
     * @param exercise the exercise to add
     */
    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    /**
     * Calculates the total workout volume.
     * The total volume is the sum of (reps × weight) for all sets in all exercises.
     * @return the total volume of the workout
     */
    public float getTotalVolume() {
        float total = 0;
        for (Exercise exercise : exercises) {
            for (ExerciseSet set : exercise.getSetList()) {
                total += set.getReps() * set.getWeight();
            }
        }
        return total;
    }
}
