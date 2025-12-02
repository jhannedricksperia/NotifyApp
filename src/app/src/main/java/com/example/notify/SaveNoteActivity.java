package com.example.notify;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notify.entity.Notify;
import com.example.notify.helper.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaveNoteActivity extends AppCompatActivity {

    EditText txtTitle, txtContent;
    TextView lblDate, lblTime;
    ImageView imgSave, imgBack;
    DatabaseHelper dbHelper;
    String dateTimeCreated, dateTimeModified;
    boolean isSaved = false;
    boolean hasShownLimitToast = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_note);

        txtTitle = findViewById(R.id.txtTitle);
        txtContent = findViewById(R.id.txtContent);
        lblDate = findViewById(R.id.lblCreatedDate);
        lblTime = findViewById(R.id.lblCreatedTime);
        imgSave = findViewById(R.id.imgSave);
        imgBack = findViewById(R.id.imgBack);

        dbHelper = new DatabaseHelper(this);

        String currentDate = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault()).format(new Date());
        String currentDateDatabase = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        lblDate.setText(currentDate);
        lblTime.setText(currentTime);

        dateTimeCreated = currentDateDatabase + " " + currentTime;
        dateTimeModified = dateTimeCreated;

        txtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 50 && !hasShownLimitToast) {
                    Toast.makeText(SaveNoteActivity.this, "Character Limit has been Reached", Toast.LENGTH_SHORT).show();
                    hasShownLimitToast = true;
                } else if (s.length() < 50) {
                    hasShownLimitToast = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 50) {
                    s.delete(50, s.length());
                }
            }
        });

        imgSave.setOnClickListener(v -> {
            saveNote();
        });

        imgBack.setOnClickListener(v -> {
            Intent i = new Intent(SaveNoteActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void saveNote() {
        String title = txtTitle.getText().toString().trim();
        String content = txtContent.getText().toString().trim();

        if (title.isEmpty() && content.isEmpty()) {
            Toast.makeText(this, "Cannot save an empty note", Toast.LENGTH_SHORT).show();
            return;
        }

        if (title.isEmpty()) {
            String firstLine = content.split("\n")[0].trim();
            title = firstLine.length() > 10 ? firstLine.substring(0, 10) + "..." : firstLine;
        }

        dateTimeModified = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).format(new Date());

        Notify notify = new Notify(title, content, dateTimeCreated, dateTimeModified);
        long result = dbHelper.addNote(notify);

        if (result != -1) {
            Toast.makeText(this, "Note Saved Successfully", Toast.LENGTH_SHORT).show();
            isSaved = true;
        }

        Intent i = new Intent(SaveNoteActivity.this, HomeActivity.class);
        startActivity(i);
        finish();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (!isSaved) saveNote();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isSaved) saveNote();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isSaved) saveNote();
    }
}