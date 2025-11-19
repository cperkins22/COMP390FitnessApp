package com.fitnessapp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a tracked workout session.
 * Each tracked workout stores its unique ID, name, date completed,
 * and a list of exercises performed during the workout.
 */
public class TrackedWorkout {

    /** Unique identifier for this workout. */
    private final UUID id;
    /** Name of the workout (e.g., "Leg Day", "Chest & Back"). */
    private String name;
    /** Date and time when the workout was completed. */
    private final LocalDateTime dateCompleted;
    /** List of exercises performed in this workout. */
    private final List<TrackedExercise> exercises;

    /**
     * Constructs a new TrackedWorkout with a unique ID, current timestamp,
     * and an empty list of exercises.
     */
    public TrackedWorkout() {
        this.id = UUID.randomUUID();
        this.dateCompleted = LocalDateTime.now();
        this.exercises = new ArrayList<>();
    }

    /**
     * Returns the unique ID of the workout.
     * @return the workout UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the name of the workout.
     * @return the workout name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the workout.
     * @param name the new workout name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the date and time when the workout was completed.
     * @return the completion timestamp
     */
    public LocalDateTime getDateCompleted() {
        return dateCompleted;
    }

    /**
     * Returns the list of exercises performed in this workout.
     * @return list of TrackedExercise objects
     */
    public List<TrackedExercise> getExercises() {
        return exercises;
    }

    /**
     * Adds an exercise to the workout.
     * @param exercise the exercise to add
     */
    public void addExercise(TrackedExercise exercise) {
        exercises.add(exercise);
    }

    @Override
    public String toString() {
        return name + " (" + dateCompleted.getDayOfMonth() + " " + dateCompleted.getMonth() + ")";
    }
} //class end
