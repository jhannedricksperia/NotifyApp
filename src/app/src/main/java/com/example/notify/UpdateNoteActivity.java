package com.example.notify;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notify.entity.Notify;
import com.example.notify.helper.DatabaseHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UpdateNoteActivity extends AppCompatActivity {

    ImageView imgBack, imgUpdate;
    TextView lblId, lblCreatedDate, lblCreatedTime, lblModifiedDate, lblModifiedTime;
    EditText txtTitle, txtContent;

    boolean hasShownLimitToast = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_note);
        imgBack = findViewById(R.id.imgBack);
        imgUpdate = findViewById(R.id.imgUpdate);
        lblId = findViewById(R.id.lblId);
        lblCreatedDate = findViewById(R.id.lblCreatedDate);
        lblCreatedTime = findViewById(R.id.lblCreatedTime);
        lblModifiedDate = findViewById(R.id.lblModifiedDate);
        lblModifiedTime = findViewById(R.id.lblModifiedTime);
        txtTitle = findViewById(R.id.txtTitle);
        txtContent = findViewById(R.id.txtContent);

        Intent i = getIntent();
        String id = i.getStringExtra("id");
        String titleR = i.getStringExtra("title");
        String contentR = i.getStringExtra("content");
        String dateTimeCreatedR = i.getStringExtra("dateTimeCreated");
        String dateTimeModifiedR = i.getStringExtra("dateTimeModified");
        lblId.setText(id);
        txtTitle.setText(titleR);
        txtContent.setText(contentR);


        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        LocalDateTime createdDT = LocalDateTime.parse(dateTimeCreatedR, inputFormatter);
        LocalDateTime modifiedDT = LocalDateTime.parse(dateTimeModifiedR, inputFormatter);

        lblCreatedDate.setText(createdDT.format(dateFormatter));
        lblCreatedTime.setText(createdDT.format(timeFormatter));
        lblModifiedDate.setText(modifiedDT.format(dateFormatter));
        lblModifiedTime.setText(modifiedDT.format(timeFormatter));

        txtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 50 && !hasShownLimitToast) {
                    Toast.makeText(UpdateNoteActivity.this, "Character Limit has been Reached", Toast.LENGTH_SHORT).show();
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

        DatabaseHelper dh = new DatabaseHelper(UpdateNoteActivity.this);

        imgUpdate.setOnClickListener(v -> {

            int ID = Integer.parseInt(i.getStringExtra("id"));
            String title = txtTitle.getText().toString().trim();
            String content = txtContent.getText().toString().trim();

            if (title.isEmpty() && content.isEmpty()) {
                Toast.makeText(this, "Cannot update an empty note", Toast.LENGTH_SHORT).show();
                return;
            }

            if (title.isEmpty()) {
                String firstLine = content.split("\n")[0].trim();
                title = firstLine.length() > 10 ? firstLine.substring(0, 10) + "..." : firstLine;
            }

            String dateTimeModified = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"));

            Notify note = new Notify(ID, title, content, dateTimeCreatedR, dateTimeModified);
            long result = dh.updateNoteById(note);

            if (result != -1) {
                Toast.makeText(this, "Note Updated Successfully", Toast.LENGTH_SHORT).show();
                getOnBackPressedDispatcher().onBackPressed();
            } else {
                Toast.makeText(this, "Error Updating", Toast.LENGTH_SHORT).show();
            }
        });


        imgBack.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
        });


    }
}