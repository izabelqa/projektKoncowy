package com.exe.fitlifetaskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.exe.fitlifetaskmanager.fragments.CalendarFragment;
import com.exe.fitlifetaskmanager.fragments.HomeFragment;
import com.exe.fitlifetaskmanager.fragments.SettingsFragment;
import com.exe.fitlifetaskmanager.fragments.TrainingPlanFragment;
import com.exe.fitlifetaskmanager.shoppinglist.ListActivity;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    // zmienne związane z czujnikiem zbliżeniowym
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    //zmienne związane z czujnikiem jasności
    private Sensor lightSensor;

    // Klucze do zapisywania i przywracania stanu
    private static final String KEY_CURRENT_FRAGMENT = "currentFragment";
    private static final String KEY_SELECTED_ITEM = "selectedItem";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            viewHomeFragment();
        } else {
            // Przywróć stan po obróceniu ekranu
            int currentFragment = savedInstanceState.getInt(KEY_CURRENT_FRAGMENT);
            navigationView.setCheckedItem(savedInstanceState.getInt(KEY_SELECTED_ITEM));

            if (currentFragment == R.id.nav_home) {
                viewHomeFragment();
            } else if (currentFragment == R.id.nav_calendar) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CalendarFragment()).commit();
            } else if (currentFragment == R.id.nav_shoppingList) {
                Intent intent = new Intent(this, ListActivity.class);
                startActivity(intent);
            } else if (currentFragment == R.id.nav_settings) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
            }else if(currentFragment == R.id.nav_trainings){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TrainingPlanFragment()).commit();
            } else if (currentFragment == R.id.nav_logout) {
                Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
            }
        }

        // Inicjalizacja czujnika zbliżeniowego
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        // Inicjalizacja czujnika jasności
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // Sprawdź, czy urządzenie obsługuje czujnik zbliżeniowy
        if (proximitySensor == null) {
            Toast.makeText(this, "This device does not have a proximity sensor.", Toast.LENGTH_SHORT).show();
        }
        // Sprawdź, czy urządzenie obsługuje czujnik jasności
        if (lightSensor == null) {
            Toast.makeText(this, "This device does not have a light sensor.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Rejestracja nasłuchiwania czujnika zbliżeniowego
        if (proximitySensor != null) {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        // Rejestracja nasłuchiwania czujnika jasności
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Wyrejestrowanie nasłuchiwania czujnika zbliżeniowego
        if (proximitySensor != null) {
            sensorManager.unregisterListener(this);
        }
        // Wyrejestrowanie nasłuchiwania czujnika jasności
        if (lightSensor != null) {
            sensorManager.unregisterListener(this, lightSensor);
        }

    }

    // Metoda z interfejsu SensorEventListener
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nieistotne
    }

    // Metoda z interfejsu SensorEventListener
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            handleProximitySensor(event);
        } else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            handleLightSensor(event);
        }
    }

    // Obsługa czujnika zbliżeniowego
    private void handleProximitySensor(SensorEvent event) {
        float distance = event.values[0];

        if (distance < proximitySensor.getMaximumRange()) {
            // Telefon jest blisko ucha, ekran powinien się wygaszać
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            // Telefon jest daleko od ucha, ekran może być aktywny
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    // Obsługa czujnika jasności
    private void handleLightSensor(SensorEvent event) {
        float lightIntensity = event.values[0];

        // Dostosuj jasność ekranu w zależności od intensywności światła
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = lightIntensity / SensorManager.LIGHT_SUNLIGHT_MAX;
        getWindow().setAttributes(layoutParams);
    }


    public void viewHomeFragment() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        String currentDayOfWeek = dayOfWeekFormat.format(currentDate);
        SimpleDateFormat dayNumberFormat = new SimpleDateFormat("dd", Locale.getDefault());
        String currentDayNumber = dayNumberFormat.format(currentDate);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.getDefault());
        String currentMonth = monthFormat.format(currentDate);

        // Przygotuj Bundle i przekaż dane do fragmentu
        Bundle bundle = new Bundle();
        bundle.putString("currentDayOfWeek", currentDayOfWeek);
        bundle.putString("currentDayNumber", currentDayNumber);
        bundle.putString("currentMonth", currentMonth);

        // Dodaj fragment do kontenera
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);

        // Zamień fragment w kontenerze
        replaceFragment(homeFragment);

        // Zaznacz element nawigacji "Home" jako aktywny
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            viewHomeFragment();
        } else if (itemId == R.id.nav_calendar) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CalendarFragment()).commit();
        } else if (itemId == R.id.nav_shoppingList) {
            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
        }else if(itemId == R.id.nav_trainings){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TrainingPlanFragment()).commit();
        }else if (itemId == R.id.nav_logout) {
            Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

}