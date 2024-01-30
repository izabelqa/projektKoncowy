package com.exe.fitlifetaskmanager.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.exe.fitlifetaskmanager.Exercise;
import com.exe.fitlifetaskmanager.ExerciseViewModel;
import com.exe.fitlifetaskmanager.R;
import com.exe.fitlifetaskmanager.TrainingExerciseCrossRef;
import com.exe.fitlifetaskmanager.TrainingViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TrainingPlanFragment extends Fragment {

    private TrainingViewModel trainingViewModel;
    private ExerciseViewModel exerciseViewModel;
    private FloatingActionButton fab;
    private RecyclerView trainingRecyclerView;
    private TextView tableTrainingTextView;
    private SearchView searchView;
    private TrainingExerciseAdapter trainingExerciseAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training_plan, container, false);

        //tableTrainingTextView = view.findViewById(R.id.tableTrainingTextView);

        trainingRecyclerView = view.findViewById(R.id.trainingRecyclerView);
        trainingExerciseAdapter = new TrainingExerciseAdapter();
        trainingRecyclerView.setAdapter(trainingExerciseAdapter);
        trainingRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        trainingViewModel = new ViewModelProvider(this).get(TrainingViewModel.class);
        trainingViewModel.getTrainingsWithExercises().observe(getViewLifecycleOwner(), new Observer<List<TrainingExerciseCrossRef>>() {
            @Override
            public void onChanged(@Nullable final List<TrainingExerciseCrossRef> trainingsWithExercises) {
                // Przekazanie danych do adaptera
                trainingExerciseAdapter.setTrainingsWithExercises(trainingsWithExercises);
            }
        });

        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);

        fab = view.findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

        // Inicjalizacja SearchView
        searchView = view.findViewById(R.id.searchView);
        searchView.setQueryHint(getString(R.string.search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Obsługa zatwierdzenia wyszukiwania (jeśli potrzebna)
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Obsługa zmiany tekstu w SearchView
                filter(newText);
                return true;
            }
        });

        return view;
    }

    private void filter(String query) {
        trainingViewModel.getTrainingsWithExercises().observe(getViewLifecycleOwner(), new Observer<List<TrainingExerciseCrossRef>>() {
            @Override
            public void onChanged(@Nullable final List<TrainingExerciseCrossRef> trainingsWithExercises) {
                // Wykonaj filtrowanie na bazie wprowadzonego tekstu
                List<TrainingExerciseCrossRef> filteredTrainings = new ArrayList<>();
                if (trainingsWithExercises != null) {
                    for (TrainingExerciseCrossRef trainingWithExercises : trainingsWithExercises) {
                        if (trainingWithExercises.training.getTrainingName().toLowerCase().contains(query.toLowerCase())) {
                            filteredTrainings.add(trainingWithExercises);
                        }
                    }
                }

                // Aktualizuj adapter z wynikami filtrowania
                trainingExerciseAdapter.setTrainingsWithExercises(filteredTrainings);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_floating_btn, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.addTraining) {
                    // Obsługa pierwszej opcji
                    showToast("add new training");
                    return true;
                } else if (itemId == R.id.addExercise) {
                    // Obsługa drugiej opcji
                    showToast("add new exercise");
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show();
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Adapter dla treningów z ćwiczeniami
    public class TrainingExerciseAdapter extends RecyclerView.Adapter<TrainingExerciseAdapter.TrainingExerciseViewHolder> {

        private List<TrainingExerciseCrossRef> trainingsWithExercises;

        public TrainingExerciseAdapter() {
            this.trainingsWithExercises = new ArrayList<>();
        }

        public void setTrainingsWithExercises(List<TrainingExerciseCrossRef> trainingsWithExercises) {
            this.trainingsWithExercises = trainingsWithExercises;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public TrainingExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_training, parent, false);
            return new TrainingExerciseViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull TrainingExerciseViewHolder holder, int position) {
            TrainingExerciseCrossRef trainingWithExercises = trainingsWithExercises.get(position);
            holder.bind(trainingWithExercises);
        }

        @Override
        public int getItemCount() {
            return trainingsWithExercises.size();
        }

        class TrainingExerciseViewHolder extends RecyclerView.ViewHolder {

            private TextView trainingDayTextView;
            private TextView trainingTypeTextView;
            private RecyclerView exerciseRecyclerView;
            private ExerciseAdapter exerciseAdapter;

            public TrainingExerciseViewHolder(@NonNull View itemView) {
                super(itemView);
                // Inicjalizacja elementów widoku dla treningu z ćwiczeniami
                trainingDayTextView = itemView.findViewById(R.id.trainingDayTextView);
                trainingTypeTextView = itemView.findViewById(R.id.trainingTypeTextView);
                exerciseRecyclerView = itemView.findViewById(R.id.exerciseRecyclerView);

                exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
                exerciseAdapter = new ExerciseAdapter();
                exerciseRecyclerView.setAdapter(exerciseAdapter);
            }

            public void bind(TrainingExerciseCrossRef trainingWithExercises) {
                if (trainingWithExercises != null) {
                    // Ustawianie danych treningu z ćwiczeniami w widoku
                    trainingDayTextView.setText(trainingWithExercises.training.getDayOfWeek());
                    trainingTypeTextView.setText("Type: " + trainingWithExercises.training.getTrainingName());

                    // Przekazanie ćwiczeń do adaptera dla ćwiczeń
                    exerciseAdapter.setExercises(trainingWithExercises.exercises);
                }
            }
        }
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
            // Zmieniono układ XML na item_exercise
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

            public ExerciseViewHolder(@NonNull View itemView) {
                super(itemView);
                // Inicjalizacja elementów widoku dla ćwiczenia
                exerciseNameTextView = itemView.findViewById(R.id.exerciseNameTextView);
                repetitionsTextView = itemView.findViewById(R.id.repetitionsTextView);
                setsTextView = itemView.findViewById(R.id.setsTextView);
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
}