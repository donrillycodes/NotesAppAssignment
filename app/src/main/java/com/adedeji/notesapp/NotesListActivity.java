package com.adedeji.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotesListActivity extends AppCompatActivity {

    private static final String TAG = "NotesListActivity";
    private ListView listViewNotes;
    private ArrayAdapter<String> adapter;
    private List<String> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        // Handle safe area adjustments
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


        // Set up the ListView for displaying notes
        listViewNotes = findViewById(R.id.listViewNotes);
        noteList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noteList);
        listViewNotes.setAdapter(adapter);

        Button btnAddNote = findViewById(R.id.btnAddNote);
        btnAddNote.setOnClickListener(v -> {
            Log.d(TAG, "Add Note button clicked");
            startActivity(new Intent(NotesListActivity.this, AddNoteActivity.class));
        });

        // Firebase: Listen for saved notes
        DatabaseReference databaseNotes = FirebaseDatabase.getInstance().getReference("notes");
        databaseNotes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                noteList.clear();
                // Iterate through each note saved in Firebase
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    String title = noteSnapshot.child("title").getValue(String.class);
                    String content = noteSnapshot.child("content").getValue(String.class);
                    if (title != null && content != null) {
                        noteList.add(title + "\n" + content);
                    }
                }
                adapter.notifyDataSetChanged();
                Log.d(TAG, "Notes updated: " + noteList.size() + " notes found.");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(NotesListActivity.this, "Failed to load notes", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Database error: " + error.getMessage());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
