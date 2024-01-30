package com.exe.fitlifetaskmanager;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

    @Entity(tableName = "exercises")
    public class Exercise {
        @PrimaryKey(autoGenerate = true)
        private int id;

        @ColumnInfo(name = "exercise_name")
        private String exerciseName;
        @ColumnInfo(name = "repetitions")
        private int repetitions;

        @ColumnInfo(name = "series")
        private int series;

        @ColumnInfo(name = "training_id")
        private long trainingId; // Klucz obcy do powiązania z treningiem

        public void setRepetitions(int repetitions) {
            this.repetitions = repetitions;
        }

        public long getTrainingId() {
            return trainingId;
        }

        public void setTrainingId(int trainingId) {
            this.trainingId = trainingId;
        }

        public Exercise(String exerciseName, int repetitions, int series, long trainingId) {
            this.exerciseName = exerciseName;
            this.repetitions = repetitions;
            this.series = series;
            this.trainingId = trainingId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getExerciseName() {
            return exerciseName;
        }

        public void setExerciseName(String exerciseName) {
            this.exerciseName = exerciseName;
        }

        public int getRepetitions() {
            return repetitions;
        }

        public int getSeries() {
            return series;
        }

        public void setSeries(int series) {
            this.series = series;
        }
    }
