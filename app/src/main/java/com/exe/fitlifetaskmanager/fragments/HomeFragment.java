package com.exe.fitlifetaskmanager.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.exe.fitlifetaskmanager.Exercise;
import com.exe.fitlifetaskmanager.ExerciseViewModel;
import com.exe.fitlifetaskmanager.R;
import com.exe.fitlifetaskmanager.Training;
import com.exe.fitlifetaskmanager.TrainingViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    private TextView dayOfWeekTextView;
    private TextView dayNumberTextView;
    private TextView monthTextView;
    private TrainingViewModel trainingViewModel;
    private ArrayAdapter<String> trainingAdapter;
    private List<Training> trainingsList;
    private ExerciseViewModel exerciseViewModel;
    String currentDayOfWeek;
    private static final String PREFS_NAME = "MyPrefsFile";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_day_details, container, false);

        // Inicjalizacja elementów interfejsu użytkownika
        dayOfWeekTextView = view.findViewById(R.id.dayOfWeekTextView);
        dayNumberTextView = view.findViewById(R.id.dayNumberTextView);
        monthTextView = view.findViewById(R.id.monthTextView);

        // Pobierz dane z argumentów przekazanych z MainActivity
        Bundle bundle = getArguments();
        if (bundle != null) {
            currentDayOfWeek = bundle.getString("currentDayOfWeek");
            String currentDayNumber = bundle.getString("currentDayNumber");
            String currentMonth = bundle.getString("currentMonth");
            String fullDate = bundle.getString("fullDate");

            // Ustaw dane na widoku
            dayOfWeekTextView.setText(currentDayOfWeek);
            dayNumberTextView.setText(currentDayNumber);
            monthTextView.setText(currentMonth);

            ImageView glass1 = view.findViewById(R.id.glass1);
            ImageView glass2 = view.findViewById(R.id.glass2);
            ImageView glass3 = view.findViewById(R.id.glass3);
            ImageView glass4 = view.findViewById(R.id.glass4);
            ImageView glass5 = view.findViewById(R.id.glass5);
            ImageView glass6 = view.findViewById(R.id.glass6);

            setClickListeners(requireContext(), glass1, fullDate + "_glass1");
            setClickListeners(requireContext(), glass2, fullDate + "_glass2");
            setClickListeners(requireContext(), glass3, fullDate + "_glass3");
            setClickListeners(requireContext(), glass4, fullDate + "_glass4");
            setClickListeners(requireContext(), glass5, fullDate + "_glass5");
            setClickListeners(requireContext(), glass6, fullDate + "_glass6");
        }

        // Inicjalizacja ExerciseViewModel
        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
        RecyclerView exerciseRecyclerView = view.findViewById(R.id.exercisesRecyclerView);
        ExerciseAdapter exerciseAdapter = new ExerciseAdapter();
        exerciseRecyclerView.setAdapter(exerciseAdapter);

        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        // Inicjalizacja ViewModel
        trainingViewModel = new ViewModelProvider(this).get(TrainingViewModel.class);

        // Inicjalizacja adaptera dla Spinnera
        trainingAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
        trainingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner trainingSpinner = view.findViewById(R.id.trainingSpinner);
        trainingSpinner.setAdapter(trainingAdapter);

        // Pobierz treningi z ViewModel i zaktualizuj spinner
        trainingViewModel.getTrainingsForDay(currentDayOfWeek).observe(getViewLifecycleOwner(), trainingsList -> {
            this.trainingsList = trainingsList;

            // Pobierz tylko nazwy treningów i zaktualizuj spinner
            List<String> trainingNames = new ArrayList<>();
            for (Training training : trainingsList) {
                trainingNames.add(training.getTrainingName());
            }
            trainingAdapter.clear();
            trainingAdapter.addAll(trainingNames);
            trainingAdapter.notifyDataSetChanged();
        });

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
                    exerciseViewModel.getExercisesForTraining(selectedTraining.getId()).observe(getViewLifecycleOwner(), exercises -> {
                        // Aktualizuj RecyclerView z nową listą ćwiczeń
                        exerciseAdapter.setExercises(exercises);
                        exerciseAdapter.notifyDataSetChanged();
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Obsługa braku wyboru treningu
            }
        });

        return view;
    }

    public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

        private List<Exercise> exercises;

        public ExerciseAdapter() {
            this.exercises = new ArrayList<>();
        }

        public void setExercises(List<Exercise> exercises) {
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
            Exercise currentExercise = exercises.get(position);
            holder.bind(currentExercise);
        }


        @Override
        public int getItemCount() {
            return exercises.size();
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
    }

        private void setClickListeners(final Context context, final ImageView imageView, final String key) {
            final boolean[] isBlue = {isBlueColor(context, key)};

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
                    saveBlueColorState(context, key, isBlue[0]);
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
    }