package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StrideMainMenuActivity extends BaseActivity {

    // Keys for intent
    protected final static String INTENT_RUNNING_RECORDS_KEY = "intent-running-records-key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stride_main_menu);

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

                Intent menuToTrackMeSendDataActivity = new Intent(StrideMainMenuActivity.this, TrackMeSendDataActivity.class);
                startActivity(menuToTrackMeSendDataActivity);

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
