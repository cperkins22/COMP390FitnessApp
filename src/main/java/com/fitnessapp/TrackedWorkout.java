package com.fitnessapp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Class intended to manage workouts
 */

/**
 * Metrics such as the name and the date completed
 */
public class TrackedWorkout {
    private UUID id;
    private String name;
    private LocalDateTime dateCompleted;
    private List<TrackedExercise> exercises;

    public TrackedWorkout() {
        this.id = UUID.randomUUID();
        this.dateCompleted = LocalDateTime.now();
        this.exercises = new ArrayList<>();
    }

    /**
     * Makes sure tracked workout class works in tandem with the tracked exercise class
     */
    public UUID getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDateTime getDateCompleted() { return dateCompleted; }
    public List<TrackedExercise> getExercises() { return exercises; }
    public void addExercise(TrackedExercise exercise) { exercises.add(exercise); }

    @Override
    public String toString(){
        return this.getName() + "(" + dateCompleted.getDayOfMonth() + " " + dateCompleted.getMonth() + ")";
    }
}


