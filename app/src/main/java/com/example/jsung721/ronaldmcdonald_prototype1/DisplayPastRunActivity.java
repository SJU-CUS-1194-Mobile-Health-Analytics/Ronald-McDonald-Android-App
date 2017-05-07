package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import edu.stjohns.cus1194.stride.data.UserProfile;

/**
 * Created by yzhan265 on 5/6/2017.
 */

public class DisplayPastRunActivity extends AppCompatActivity {

    // User info
    private FirebaseUser mUser;
    private UserProfile userProfile;

    // UI elements
    protected Button trackMeBackButton;
    protected Spinner trackMeModeSpinner;
    protected Button startTrackingButton;
    protected TextView milesValueTextView;
    protected TextView timeValueTextView;
    protected TextView paceValueTextView;

    // get run key from previous activity
    protected String runKey;
    protected final String FLAG_RUN_KEY = "RUN KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_me);

        // initiate views and hide unused views
        initUI();

        // get run key from previous activity
        Bundle bundle = getIntent().getExtras();
        runKey = bundle.get(FLAG_RUN_KEY).toString();


    }

    private void initUI() {
        // Button to go back to the main menu
        trackMeBackButton = (Button) findViewById(R.id.button_track_me_to_menu);
        trackMeBackButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent trackMeToMenuIntent = new Intent(DisplayPastRunActivity.this, DailyLogActivity.class);
                //trackMeToMenuIntent.putParcelableArrayListExtra(INTENT_RUNNING_RECORDS_KEY, runningRecordArrayList);
                startActivity(trackMeToMenuIntent);

            }
        });

        // Spinner for trackMeMode
        trackMeModeSpinner = (Spinner)findViewById(R.id.spinner_track_me_mode);
        trackMeModeSpinner.setVisibility(View.INVISIBLE);
        // HIDE: Button to start/stop tracking
        startTrackingButton = (Button) findViewById(R.id.button_track_me_start_tracking);
        startTrackingButton.setVisibility(View.INVISIBLE);

        // TextViews
        milesValueTextView = (TextView) findViewById(R.id.text_track_me_miles_value);
        timeValueTextView = (TextView) findViewById(R.id.text_track_me_time_value);
        paceValueTextView = (TextView) findViewById(R.id.text_track_me_pace_value);
    }
}
