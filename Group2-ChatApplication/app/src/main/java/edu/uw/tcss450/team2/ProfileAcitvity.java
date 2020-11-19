package edu.uw.tcss450.team2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileAcitvity extends AppCompatActivity {
    private TextView proFName, proLName, proUName, proEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_acitvity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        displayProfile();
    }

    private void displayProfile() {

        try {
            proFName = findViewById(R.id.profile_fname);
            proLName = findViewById(R.id.profile_lname);
            proUName = findViewById(R.id.profile_usernanme);
            proEmail = findViewById(R.id.profile_email);

        } catch (Exception e) {
            Toast.makeText(this, "Profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}