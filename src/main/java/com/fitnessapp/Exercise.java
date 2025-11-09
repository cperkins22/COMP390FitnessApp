package com.fitnessapp;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

/**
 * Cashel Stuger 10/2025
 * Exercise class:
 * Represents a single exercise performed during a workout.
 * Each Exercise contains info such as its name, description,
 * and a list of ExerciseSet objects that record the repetitions
 * and weight lifted for each set.
 *
 * Attributes:
 * - id (UUID): Unique identifier automatically generated for each exercise.
 * - name (String): The name of the exercise (e.g., "Bench Press").
 * - description (String): A short summary of the exercise.
 * - sets (int): The total number of sets performed.
 * - repsPerSet (int): Default or planned number of repetitions per set.
 * - setList (List<ExerciseSet>): Stores individual sets with reps and weight.
 *
 * Methods:
 * - addSet(ExerciseSet set): Adds a new set to the exercise.
 * - getTotalWeightLifted(): Calculates total (reps × weight) for all sets.
 * - Getters and Setters: Access or modify exercise data safely.
 */

public class Exercise {

    /** Unique identifier for the exercise. */
    private final UUID id;

    /** Name of the exercise (e.g., Bench Press, Squat). */
    private String name;

    /** Description or notes about the exercise. */
    private String description;

    /** Number of sets for this exercise. */
    private int sets;

    /** Number of repetitions per set (default, not enforced). */
    private int repsPerSet;

    /** List of all sets associated with this exercise. */
    private List<ExerciseSet> setList;

    /**
     * Constructs a new Exercise with the given name and description.
     * Generates a unique ID and initializes an empty list of sets.
     *
     * @param name the name of the exercise
     * @param description a short description of the exercise
     */
    public Exercise(String name, String description) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.sets = 0;
        this.repsPerSet = 0;
        this.setList = new ArrayList<>();
    }

    /**
     * Adds a set to this exercise.
     * Ignores invalid or null sets.
     *
     * @param set the set to add
     */
    public void addSet(ExerciseSet set) {
        if (set == null) return;
        if (set.getReps() < 0 || set.getWeight() < 0) return;

        setList.add(set);
        this.sets = setList.size();
    }

    /**
     * Calculates the total weight lifted across all sets.
     * The total is the sum of (reps × weight) for each set.
     *
     * @return the total weight lifted
     */
    public float getTotalWeightLifted() {
        float total = 0.0f;
        for (ExerciseSet s : setList) {
            total += (s.getReps() * s.getWeight());
        }
        return total;
    }

    /**
     * Gets the unique ID of the exercise.
     * @return the exercise ID
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
     * @param name the exercise name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the exercise.
     * @return the exercise description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the exercise.
     * @param description the exercise description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the number of sets for this exercise.
     * @return the number of sets
     */
    public int getSets() {
        return sets;
    }

    /**
     * Gets the number of repetitions per set.
     * @return the number of reps per set
     */
    public int getRepsPerSet() {
        return repsPerSet;
    }

    /**
     * Sets the number of repetitions per set.
     * @param repsPerSet the reps per set value
     */
    public void setRepsPerSet(int repsPerSet) {
        this.repsPerSet = repsPerSet;
    }

    /**
     * Gets the list of sets for this exercise.
     * @return the list of exercise sets
     */
    public List<ExerciseSet> getSetList() {
        return setList;
    }
}
