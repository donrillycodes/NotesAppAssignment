package com.adedeji.notesapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddNoteActivity extends AppCompatActivity {

    private static final String TAG = "AddNoteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // Adjust UI for safe areas
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.guidelineTop), (v, insets) -> {
            int topInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;

            Guideline guidelineTop = findViewById(R.id.guidelineTop);
            Guideline guidelineBottom = findViewById(R.id.guidelineBottom);

            ConstraintLayout.LayoutParams paramsTop = (ConstraintLayout.LayoutParams) guidelineTop.getLayoutParams();
            paramsTop.guideBegin = topInset;
            guidelineTop.setLayoutParams(paramsTop);

            ConstraintLayout.LayoutParams paramsBottom = (ConstraintLayout.LayoutParams) guidelineBottom.getLayoutParams();
            paramsBottom.guideEnd = bottomInset;
            guidelineBottom.setLayoutParams(paramsBottom);

            return insets;
        });

        EditText etTitle = findViewById(R.id.etTitle);
        EditText etContent = findViewById(R.id.etContent);
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();

            // Validate input fields
            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(AddNoteActivity.this, "Please enter both title and content", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save note to Firebase
            DatabaseReference databaseNotes = FirebaseDatabase.getInstance().getReference("notes");
            String noteId = databaseNotes.push().getKey();

            if (noteId == null) {
                Toast.makeText(AddNoteActivity.this, "Error generating note ID", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to generate note ID from Firebase");
                return;
            }

            HashMap<String, Object> note = new HashMap<>();
            note.put("title", title);
            note.put("content", content);

            databaseNotes.child(noteId).setValue(note)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddNoteActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                        finish();  // Close this activity and return to the notes list
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error saving note", e);
                        Toast.makeText(AddNoteActivity.this, "Failed to save note: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
