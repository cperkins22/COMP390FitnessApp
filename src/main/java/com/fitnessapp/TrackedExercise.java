package com.fitnessapp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a tracked exercise for a workout.
 * Each exercise has a unique ID, a name, and a list of sets completed.
 */
public class TrackedExercise {

    /** Unique identifier for this tracked exercise. */
    private final UUID id;

    /** Name of the exercise (e.g., "Bench Press"). */
    private String name;

    /** List of sets performed for this exercise. */
    private final List<TrackedExerciseSet> sets;

    /**
     * Constructs a new TrackedExercise with a unique ID and an empty set list.
     */
    public TrackedExercise() {
        this.id = UUID.randomUUID();
        this.sets = new ArrayList<>();
    }

    /**
     * Gets the unique ID of this exercise.
     * @return the UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the name of the exercise.
     * @return the exercise name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the exercise.
     * @param name the exercise name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the list of sets performed for this exercise.
     * @return the list of TrackedExerciseSet
     */
    public List<TrackedExerciseSet> getSets() {
        return sets;
    }

    /**
     * Adds a set to this exercise.
     * @param set the TrackedExerciseSet to add
     */
    public void addSet(TrackedExerciseSet set) {
        sets.add(set);
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
