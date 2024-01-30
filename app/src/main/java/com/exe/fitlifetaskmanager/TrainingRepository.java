package com.exe.fitlifetaskmanager;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.exe.fitlifetaskmanager.dao.TrainingDao;

import java.util.List;

public class TrainingRepository {
    private final TrainingDao trainingDao;

    private final LiveData<List<Training>> trainings;


    TrainingRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        trainingDao = database.trainingDao();
        trainings = trainingDao.findAllTrainings();
    }

    LiveData<List<Training>> findAllTrainings() {
        return trainings;
    }
    public List<Training> findTrainingWithName(String trainingName) {
        return trainingDao.findTrainingsWithName(trainingName);
    }

    public LiveData<List<Training>> getTrainingsForDay(String dayOfWeek) {
        return trainingDao.getTrainingsForDay(dayOfWeek);
    }

    void insertTraining(Training training) {
        AppDatabase.databaseWriteExecutor.execute(() -> trainingDao.insertTraining(training));
    }

    void updateTraining(Training training) {
        AppDatabase.databaseWriteExecutor.execute(() -> trainingDao.updateTraining(training));
    }

    void deleteTraining(Training training) {
        AppDatabase.databaseWriteExecutor.execute(() -> trainingDao.deleteTraining(training));
    }

    public LiveData<List<TrainingExerciseCrossRef>> getTrainingsWithExercises() {
        return trainingDao.getTrainingsWithExercises();
    }


}
