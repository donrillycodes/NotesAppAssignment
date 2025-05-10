package com.adedeji.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("notes");

        setupRecyclerView();
        setupFloatingActionButton();
        loadNotes();

        // Insert sample notes (only for testing - remove in production)
        insertSampleNotes();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(new ArrayList<>());
        recyclerView.setAdapter(noteAdapter);
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fabAddNote);
        fab.setOnClickListener(view ->
                startActivity(new Intent(this, AddNoteActivity.class)));
    }

    private void loadNotes() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Note> notes = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Note note = dataSnapshot.getValue(Note.class);
                    if (note != null) {
                        note.setId(dataSnapshot.getKey());
                        notes.add(note);
                    }
                }
                noteAdapter.updateNotes(notes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivity", "Database error", error.toException());
                Toast.makeText(MainActivity.this, "Failed to load notes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertSampleNotes() {
        // Only insert if the database is empty (for demo purposes)
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    // Create sample notes
                    Note note1 = new Note("Sample Note 1", "This is the content of sample note 1");
                    Note note2 = new Note("Sample Note 2", "This is the content of sample note 2");
                    Note note3 = new Note("Sample Note 3", "This is the content of sample note 3");

                    // Push to Firebase with specific keys
                    databaseReference.child("note1").setValue(note1);
                    databaseReference.child("note2").setValue(note2);
                    databaseReference.child("note3").setValue(note3);

                    Toast.makeText(MainActivity.this, "Sample notes added", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivity", "Error checking for existing notes", error.toException());
            }
        });
    }
}