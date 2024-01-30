package com.exe.fitlifetaskmanager;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "trainings")
public class Training {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String dayOfWeek; // Dzień tygodnia, np. "Monday", "Tuesday", ...

    @ColumnInfo(name = "training_name")
    private String trainingName;

    public Training(String dayOfWeek, String trainingName) {
        this.dayOfWeek = dayOfWeek;
        this.trainingName = trainingName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }
}