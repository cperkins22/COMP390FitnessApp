package com.fitnessapp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TrackedExercise {
    private UUID id;
    private String name;
    private List<TrackedExerciseSet> sets;

    public TrackedExercise() {
        this.id = UUID.randomUUID();
        this.sets = new ArrayList<>();
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<TrackedExerciseSet> getSets() { return sets; }
    public void addSet(TrackedExerciseSet set) { sets.add(set); }

    @Override
    public String toString(){
        return this.getName();
    }
}


