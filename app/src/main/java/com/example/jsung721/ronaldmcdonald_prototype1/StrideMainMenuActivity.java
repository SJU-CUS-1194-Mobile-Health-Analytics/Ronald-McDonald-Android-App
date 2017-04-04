package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class StrideMainMenuActivity extends AppCompatActivity {

    private String mUserName;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stride_main_menu);

        mUserName = "anonymous";

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
      if(mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUserName = mFirebaseUser.getDisplayName();
        }

        Toast.makeText(getApplicationContext(), "Logged in as: " + mUserName, Toast.LENGTH_LONG).show();

        Button menuToProfileButton = (Button) findViewById(R.id.button_menu_to_profile);
        Button menuToTrackMeButton = (Button) findViewById(R.id.button_menu_to_track_me);
        Button menuToSettingsButton = (Button) findViewById(R.id.button_menu_to_settings);

        menuToProfileButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent menuToProfileIntent = new Intent(StrideMainMenuActivity.this, ProfileActivity.class);
                startActivity(menuToProfileIntent);

            }
        });

        menuToTrackMeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent menuToTrackMeIntent = new Intent(StrideMainMenuActivity.this, TrackMeActivity.class);
                startActivity(menuToTrackMeIntent);

            }
        });

        menuToSettingsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent menuToSettingsIntent = new Intent(StrideMainMenuActivity.this, SettingsActivity.class);
                startActivity(menuToSettingsIntent);

            }
        });
    }
}
