package com.adedeji.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handle notch/safe area
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

        Button btnViewNotes = findViewById(R.id.btnViewNotes);
        Button btnSettings = findViewById(R.id.btnSettings);

        btnViewNotes.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, NotesListActivity.class));
        });

        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });
    }
}