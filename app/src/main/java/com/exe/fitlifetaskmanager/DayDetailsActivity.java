package com.exe.fitlifetaskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DayDetailsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefsFile";
    private ExerciseAdapter exerciseAdapter;
    private TrainingViewModel trainingViewModel;
    private ExerciseViewModel exerciseViewModel;
    private List<Training> trainingsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_details);

        Intent intent = getIntent();
        if (intent != null) {
            String selectedDate = intent.getStringExtra("selectedDate");
            String selectedDayOfWeek = intent.getStringExtra("selectedDayOfWeek");
            String selectedMonth = intent.getStringExtra("selectedMonth");
            String selectedDateToKey = intent.getStringExtra("selectedDateToKey");

            TextView dateTextView = findViewById(R.id.dayNumberTextView);
            dateTextView.setText(selectedDate);

            TextView dayOfWeekTextView = findViewById(R.id.dayOfWeekTextView);
            dayOfWeekTextView.setText(selectedDayOfWeek);

            TextView monthTextView = findViewById(R.id.monthTextView);
            monthTextView.setText(selectedMonth);

            Spinner trainingSpinner = findViewById(R.id.trainingSpinner);
            RecyclerView exerciseRecyclerView = findViewById(R.id.exercisesRecyclerView);

            // Inicjalizacja ExerciseViewModel
            exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);

            // Inicjalizacja adaptera dla Exercise RecyclerView
            exerciseAdapter = new ExerciseAdapter();
            exerciseRecyclerView.setAdapter(exerciseAdapter);
            exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Obserwuj zmiany w liście ćwiczeń
            exerciseViewModel.getAllExercises().observe(this, exercises -> {
                // Aktualizuj RecyclerView z nową listą ćwiczeń
                exerciseAdapter.setExercises(exercises);
            });

            trainingViewModel = new ViewModelProvider(this).get(TrainingViewModel.class);

            // Pobierz treningi z ViewModel i zaktualizuj spinner
            trainingViewModel.getTrainingsForDay(selectedDayOfWeek).observe(this, trainingsList -> {
                this.trainingsList = trainingsList;

                // Pobierz tylko nazwy treningów i zaktualizuj spinner
                List<String> trainingNames = new ArrayList<>();
                for (Training training : trainingsList) {
                    trainingNames.add(training.getTrainingName());
                }

                ArrayAdapter<String> trainingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, trainingNames);
                trainingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                trainingSpinner.setAdapter(trainingAdapter);
            });

            setClickListeners(findViewById(R.id.glass1), selectedDateToKey + "_glass1");
            setClickListeners(findViewById(R.id.glass2), selectedDateToKey + "_glass2");
            setClickListeners(findViewById(R.id.glass3), selectedDateToKey + "_glass3");
            setClickListeners(findViewById(R.id.glass4), selectedDateToKey + "_glass4");
            setClickListeners(findViewById(R.id.glass5), selectedDateToKey + "_glass5");
            setClickListeners(findViewById(R.id.glass6), selectedDateToKey + "_glass6");

            // Obsługa zdarzenia wyboru treningu w Spinnerze
            trainingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // Pobierz wybrany trening
                    String selectedTrainingName = parentView.getItemAtPosition(position).toString();
                    // Znajdź trening o zadanej nazwie
                    Training selectedTraining = null;
                    for (Training training : trainingsList) {
                        if (training.getTrainingName().equals(selectedTrainingName)) {
                            selectedTraining = training;
                            break;
                        }
                    }

                    // Sprawdź, czy trening został znaleziony
                    if (selectedTraining != null) {
                        // Pobierz ćwiczenia przypisane do wybranego treningu po ID
                        exerciseViewModel.getExercisesForTraining(selectedTraining.getId()).observe(DayDetailsActivity.this, exercises -> {
                            // Aktualizuj RecyclerView z nową listą ćwiczeń
                            exerciseAdapter.setExercises(exercises);
                        });
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Obsługa braku wyboru treningu
                }
            });
        }
    }

    private void setClickListeners(final ImageView imageView, final String key) {
        final boolean[] isBlue = {isBlueColor(this, key)};

        // Ustaw kolor obrazka zgodnie ze stanem
        if (isBlue[0]) {
            imageView.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Odwróć stan koloru obrazka
                isBlue[0] = !isBlue[0];

                // Zmień kolor obrazka na niebieski, jeśli aktualnie niebieski to zmień z powrotem
                if (isBlue[0]) {
                    imageView.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
                } else {
                    imageView.setColorFilter(null); // Usuń filtr koloru
                }

                // Zapisz stan koloru w SharedPreferences
                saveBlueColorState(DayDetailsActivity.this, key, isBlue[0]);
            }
        });
    }

    private boolean isBlueColor(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean(key, false);
    }

    private void saveBlueColorState(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private static class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

        private List<Exercise> exercises;

        void setExercises(List<Exercise> exercises) {
            this.exercises = exercises;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
            return new ExerciseViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
            if (exercises != null) {
                Exercise currentExercise = exercises.get(position);
                holder.bind(currentExercise);
            }
        }

        @Override
        public int getItemCount() {
            return exercises != null ? exercises.size() : 0;
        }

        class ExerciseViewHolder extends RecyclerView.ViewHolder {
            private TextView exerciseNameTextView;
            private TextView repetitionsTextView;
            private TextView setsTextView;
            private TextView trainingTypeTextView; // Nowy element TextView

            public ExerciseViewHolder(@NonNull View itemView) {
                super(itemView);
                // Inicjalizacja elementów widoku dla ćwiczenia
                exerciseNameTextView = itemView.findViewById(R.id.exerciseNameTextView);
                repetitionsTextView = itemView.findViewById(R.id.repetitionsTextView);
                setsTextView = itemView.findViewById(R.id.setsTextView);
                trainingTypeTextView = itemView.findViewById(R.id.trainingTypeTextView); // Dodany TextView dla rodzaju treningu
            }

            public void bind(Exercise exercise) {
                if (exercise != null) {
                    // Ustawianie danych ćwiczenia w widoku
                    exerciseNameTextView.setText(exercise.getExerciseName());
                    repetitionsTextView.setText("Repetitions: " + exercise.getRepetitions());
                    setsTextView.setText("Sets: " + exercise.getSeries());
                }
        }
    }
}}