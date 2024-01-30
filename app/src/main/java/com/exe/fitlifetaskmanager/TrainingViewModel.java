package com.exe.fitlifetaskmanager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TrainingViewModel extends AndroidViewModel {
    private final TrainingRepository trainingRepository;
    private final LiveData<List<Training>> trainings;

    private final LiveData<List<TrainingExerciseCrossRef>> trainingsWithExercises;

    public TrainingViewModel(@NonNull Application application) {
        super(application);
        trainingRepository = new TrainingRepository(application);
        trainings = trainingRepository.findAllTrainings();
        trainingsWithExercises = trainingRepository.getTrainingsWithExercises();

    }

    public LiveData<List<Training>> getTrainingsForDay(String dayOfWeek) {
        return trainingRepository.getTrainingsForDay(dayOfWeek);
    }
    public LiveData<List<Training>> findAllTrainings() {
        return trainings;
    }

    public LiveData<List<TrainingExerciseCrossRef>> getTrainingsWithExercises() {
        return trainingRepository.getTrainingsWithExercises();
    }
    public void insertTraining(Training training) {
        trainingRepository.insertTraining(training);
    }

    public void updateTraining(Training training) {
        trainingRepository.updateTraining(training);
    }

    public void deleteTraining(Training training) {
        trainingRepository.deleteTraining(training);
    }
}