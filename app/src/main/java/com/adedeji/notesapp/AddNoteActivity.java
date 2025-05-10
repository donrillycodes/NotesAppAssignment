package com.adedeji.notesapp;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Objects;

public class AddNoteActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("notes");

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText etTitle = findViewById(R.id.etNoteTitle);
        EditText etContent = findViewById(R.id.etNoteContent);
        Button btnSave = findViewById(R.id.btnSaveNote);

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();

            if (title.isEmpty()) {
                etTitle.setError("Title is required");
                return;
            }

            if (content.isEmpty()) {
                etContent.setError("Content is required");
                return;
            }

            // Disable button to prevent multiple clicks
            btnSave.setEnabled(false);
            btnSave.setText("Saving...");

            // Create new note
            Note note = new Note(title, content);

            // Push to Firebase
            databaseReference.push().setValue(note)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddNoteActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK); // Optional: Signal success to MainActivity
                            finish();
                        } else {
                            Toast.makeText(AddNoteActivity.this, "Failed to save note", Toast.LENGTH_SHORT).show();
                            Log.e("AddNoteActivity", "Error saving note", task.getException());
                            btnSave.setEnabled(true); // Re-enable button on failure
                        }
                    });
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}