package com.exe.fitlifetaskmanager;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TrainingExerciseCrossRef {
    @Embedded
    public Training training;

    @Relation(parentColumn = "id", entityColumn = "training_id")
    public List<Exercise> exercises;
}
