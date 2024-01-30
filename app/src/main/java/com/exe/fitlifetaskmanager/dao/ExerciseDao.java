package com.exe.fitlifetaskmanager.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.exe.fitlifetaskmanager.Exercise;
import com.exe.fitlifetaskmanager.Training;
import com.exe.fitlifetaskmanager.TrainingExerciseCrossRef;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Insert
    void insertExercise(Exercise exercise);

    @Update
    void updateExercise(Exercise exercise);

    @Delete
    void deleteExercise(Exercise exercise);

    @Query("DELETE FROM exercises")
    void deleteAllExercises();

    @Query("SELECT * FROM exercises ORDER BY exercise_name")
    LiveData<List<Exercise>> getAllExercises();

    @Query("SELECT * FROM exercises WHERE exercise_name LIKE :exerciseName")
    List<Exercise> findExerciseWithName(String exerciseName);
    @Query("SELECT * FROM exercises WHERE training_id = :trainingId")
    LiveData<List<Exercise>> getExercisesForTraining(int trainingId);


}
