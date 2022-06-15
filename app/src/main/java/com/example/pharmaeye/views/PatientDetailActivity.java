package com.example.pharmaeye.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pharmaeye.R;
import com.example.pharmaeye.databinding.ActivityAddPatientBinding;
import com.example.pharmaeye.databinding.ActivityPatientDetailBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PatientDetailActivity extends AppCompatActivity {

    ActivityPatientDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("TAG","---------------- PatientDetailActivity Loaded ----------------");

        // 1. configure bindings
        binding = ActivityPatientDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        // setup the bottom navigation menu
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = binding.bottomNavView;
        NavigationUI.setupWithNavController(bottomNav, navController);

    }

    // TODO: Configuring the Navigation-Menu for adding LOGOUT Button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_navigation_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_button:{
                Intent signInIntent = new Intent(this, SignInActivity.class);
                startActivity(signInIntent);
                this.finishAffinity();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}