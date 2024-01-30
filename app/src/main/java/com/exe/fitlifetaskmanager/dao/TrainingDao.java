package com.exe.fitlifetaskmanager.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.exe.fitlifetaskmanager.Training;
import com.exe.fitlifetaskmanager.TrainingExerciseCrossRef;

import java.util.List;

@Dao
public interface TrainingDao {
    @Insert
    long insertTraining(Training training);

    @Update
    void updateTraining(Training training);

    @Delete
    void deleteTraining(Training training);

    @Query("DELETE FROM trainings")
    void deleteAllTrainings();

    @Query("SELECT * FROM trainings ORDER BY training_name")
    LiveData<List<Training>> findAllTrainings();

    @Query("SELECT * FROM trainings WHERE training_name LIKE :trainingName")
    List<Training> findTrainingsWithName(String trainingName);
    @Query("SELECT * FROM trainings WHERE dayOfWeek = :dayOfWeek")
    LiveData<List<Training>> getTrainingsForDay(String dayOfWeek);

    @Transaction
    @Query("SELECT * FROM trainings")
    LiveData<List<TrainingExerciseCrossRef>> getTrainingsWithExercises();
}