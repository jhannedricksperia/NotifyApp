package com.example.notify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FirstLaunchActivity extends AppCompatActivity {
    Button btnStartNow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_launch);

        btnStartNow = findViewById(R.id.btnStartNow);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isFirstLaunch = prefs.getBoolean("isFirstLaunch", true);

        if (isFirstLaunch) {
            Intent i = new Intent(this, FirstLaunchActivity.class);
            startActivity(i);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isFirstLaunch", false);
            editor.apply();
        }

        btnStartNow.setOnClickListener(v->{
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
            finish();
        });



    }
}