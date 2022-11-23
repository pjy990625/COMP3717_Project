package org.techtown.comp3717_project;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import org.techtown.comp3717_project.databinding.ActivityMainBinding;
import org.techtown.comp3717_project.ui.history.HistoryFragment;
import org.techtown.comp3717_project.ui.home.HomeFragment;
import org.techtown.comp3717_project.ui.setting.SettingFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    HistoryFragment historyFragment = new HistoryFragment();
    SettingFragment settingFragment = new SettingFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_history, R.id.navigation_setting)
                .build();
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //NavigationUI.setupWithNavController(binding.navView, navController);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.navigation_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
            return true;
        } else if (item.getItemId() == R.id.navigation_history) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, historyFragment).commit();
            return true;
        } else if (item.getItemId() == R.id.navigation_setting) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, settingFragment).commit();
            return true;
        }
        return false;
    }
}