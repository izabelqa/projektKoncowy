package com.exe.fitlifetaskmanager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Map;

public class ExerciseViewModel extends AndroidViewModel {

    private final ExerciseRepository exerciseRepository;
    private LiveData<List<Exercise>> exercisesForTraining;

    public ExerciseViewModel(Application application) {
        super(application);
        exerciseRepository = new ExerciseRepository(application);
    }

    public LiveData<List<Exercise>> getAllExercises() {
        return exerciseRepository.getAllExercises();
    }

    public LiveData<List<Exercise>> getExercisesForTraining(int trainingId) {
        if (exercisesForTraining == null) {
            exercisesForTraining = exerciseRepository.getExercisesForTraining(trainingId);
        }
        return exercisesForTraining;
    }
}
