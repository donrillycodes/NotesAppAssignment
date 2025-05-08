package com.adedeji.notesapp;

import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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

        Switch switchDarkMode = findViewById(R.id.switchDarkMode);
        // Add your settings logic here

        // Enable back button
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