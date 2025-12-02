package com.example.notify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notify.entity.Notify;
import com.example.notify.helper.DatabaseHelper;
import com.example.notify.helper.NotesAdapter;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ArrayList<Notify> notesList = new ArrayList<>();
    RecyclerView rvNotes;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isFirstLaunch = prefs.getBoolean("isFirstLaunch", true);

        if (isFirstLaunch) {
            Intent i = new Intent(this, FirstLaunchActivity.class);
            startActivity(i);
            prefs.edit().putBoolean("isFirstLaunch", false).apply();
            finish();
            return;
        }

        rvNotes = findViewById(R.id.rvNotes);
        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(v -> {
            Intent i = new Intent(this, SaveNoteActivity.class);
            startActivity(i);
            finish();
        });

    }

    public void displayNotes() {
        DatabaseHelper dh = new DatabaseHelper(HomeActivity.this);
        Cursor cursor = dh.readAllNotes();
        notesList.clear();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Notes", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                Notify n = new Notify(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                notesList.add(n);
            }
        }

        NotesAdapter notesAdapter = new NotesAdapter(HomeActivity.this, notesList);
        rvNotes.setAdapter(notesAdapter);
        rvNotes.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayNotes();
    }
}
