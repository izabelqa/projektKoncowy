package com.exe.fitlifetaskmanager;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.exe.fitlifetaskmanager.dao.ExerciseDao;

import java.util.List;
import java.util.Map;

public class ExerciseRepository {

    private final ExerciseDao exerciseDao;
    private final LiveData<List<Exercise>> exercises;

    public ExerciseRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        exerciseDao = database.exerciseDao();
        exercises = exerciseDao.getAllExercises();
    }

    LiveData<List<Exercise>> getAllExercises() {
        return exercises;
    }

    public LiveData<List<Exercise>> getExercisesForTraining(int trainingId) {
        return exerciseDao.getExercisesForTraining(trainingId);
    }



    // Dodaj inne metody manipulacji danymi dla ExerciseDao, jeśli są potrzebne
}
