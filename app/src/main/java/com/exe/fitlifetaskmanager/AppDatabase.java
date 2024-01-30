package com.exe.fitlifetaskmanager;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.exe.fitlifetaskmanager.dao.ExerciseDao;
import com.exe.fitlifetaskmanager.dao.ItemDao;
import com.exe.fitlifetaskmanager.dao.TrainingDao;

@Database(entities = {ItemToBuy.class, Training.class, Exercise.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase databaseInstance;
    static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

    public abstract ItemDao itemDao();

    public abstract TrainingDao trainingDao();

    public abstract ExerciseDao exerciseDao();

    static synchronized AppDatabase getDatabase(final Context context) {
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                    .addCallback(roomDatabaseCallback)
                    .build();
        }
        return databaseInstance;
    }

    /*private static final Callback roomDatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                ItemDao dao = databaseInstance.itemDao();
                dao.insert(new ItemToBuy("Chleb", "1", "szt."));
                dao.insert(new ItemToBuy("Mleko", "2", "l"));
                dao.insert(new ItemToBuy("Jajka", "12", "szt."));

                TrainingDao daoTr = databaseInstance.trainingDao();
                ExerciseDao exerciseDao = databaseInstance.exerciseDao();

                long trainingId1 = daoTr.insertTraining(new Training("Monday", "Strength Training"));
                long trainingId2 = daoTr.insertTraining(new Training("Tuesday", "Cardio Training"));
                long trainingId3 = daoTr.insertTraining(new Training("Wednesday", "CrossFit"));
                long trainingId4 = daoTr.insertTraining(new Training("Friday", "Athletics"));
                long trainingId5 = daoTr.insertTraining(new Training("Friday", "Yoga"));
                long trainingId6 = daoTr.insertTraining(new Training("Monday", "Yoga"));

                exerciseDao.insertExercise(new Exercise("Push-ups", 10, 3, trainingId1));
                exerciseDao.insertExercise(new Exercise("Sit-ups", 15, 3, trainingId1));
                exerciseDao.insertExercise(new Exercise("Running", 5, 3, trainingId2));
                exerciseDao.insertExercise(new Exercise("Burpees", 8, 4, trainingId2));
                exerciseDao.insertExercise(new Exercise("Dips", 5, 5, trainingId3));
                exerciseDao.insertExercise(new Exercise("Long jump", 3, 4, trainingId4));
                exerciseDao.insertExercise(new Exercise("High jump", 3, 4, trainingId4));
                exerciseDao.insertExercise(new Exercise("Plank", 3, 5, trainingId5));
                exerciseDao.insertExercise(new Exercise("Bridge", 2, 3, trainingId6));
                exerciseDao.insertExercise(new Exercise("Cat", 8, 5, trainingId6));


            });
        }
    };
}
*/
   private static final Callback roomDatabaseCallback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(() -> {
                ItemDao dao = databaseInstance.itemDao();
                dao.deleteAll();
                dao.insert(new ItemToBuy("Chleb", "1", "szt."));
                dao.insert(new ItemToBuy("Mleko", "2", "l"));
                dao.insert(new ItemToBuy("Jajka", "12", "szt."));

                TrainingDao daoTr = databaseInstance.trainingDao();
                ExerciseDao exerciseDao = databaseInstance.exerciseDao();
                daoTr.deleteAllTrainings();
                exerciseDao.deleteAllExercises();

                long trainingId1 = daoTr.insertTraining(new Training("Monday", "Strength Training"));
                long trainingId2 = daoTr.insertTraining(new Training("Tuesday", "Cardio Training"));
                long trainingId3 = daoTr.insertTraining(new Training("Wednesday", "CrossFit"));
                long trainingId4 = daoTr.insertTraining(new Training("Friday", "Athletics"));
                long trainingId5 = daoTr.insertTraining(new Training("Saturday", "Yoga"));
                long trainingId6 = daoTr.insertTraining(new Training("Monday", "Yoga"));
                long trainingId7 = daoTr.insertTraining(new Training("Thursday", "Cardio"));
                long trainingId8 = daoTr.insertTraining(new Training("Sunday", "FBW"));

                exerciseDao.insertExercise(new Exercise("Push-ups", 10, 3, trainingId1));
                exerciseDao.insertExercise(new Exercise("Sit-ups", 15, 3, trainingId1));
                exerciseDao.insertExercise(new Exercise("Running", 5, 3, trainingId2));
                exerciseDao.insertExercise(new Exercise("Burpees", 8, 4, trainingId2));
                exerciseDao.insertExercise(new Exercise("Dips", 5, 5, trainingId3));
                exerciseDao.insertExercise(new Exercise("Long jump", 3, 4, trainingId4));
                exerciseDao.insertExercise(new Exercise("High jump", 3, 4, trainingId4));
                exerciseDao.insertExercise(new Exercise("Plank", 3, 5, trainingId5));
                exerciseDao.insertExercise(new Exercise("Bridge", 2, 3, trainingId6));
                exerciseDao.insertExercise(new Exercise("Cat", 8, 5, trainingId6));
                exerciseDao.insertExercise(new Exercise("elliptical trainer", 8, 5, trainingId7));
                exerciseDao.insertExercise(new Exercise("Burpees", 8, 5, trainingId8));

            });
        }
    };
}

