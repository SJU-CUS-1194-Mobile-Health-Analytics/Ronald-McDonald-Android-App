package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.stjohns.cus1194.stride.data.RunningRecord;
import edu.stjohns.cus1194.stride.data.TimestampedLocation;
import edu.stjohns.cus1194.stride.data.UserProfile;

import static edu.stjohns.cus1194.stride.db.RunningRecordsDBAccess.getRunningPathRefById;

/**
 * Created by yzhan265 on 5/6/2017.
 */

public class DisplayPastRunActivity extends AppCompatActivity {

    // Google Maps
    protected MapsFragment mapsFragment;

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
    protected String runId;
    protected final String FLAG_RUNNING_RECORD_ID = "RUNNING RECORD ID";

    // running record
    protected RunningRecord runningRecord;
    protected ArrayList<TimestampedLocation> runningRecordArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_me);

        // Build map fragment
        buildMapFragment();

        // initiate views and hide unused views
        initUI();

        // get run key from previous activity
//        Bundle bundle = getIntent().getExtras();
//        runId = bundle.get(FLAG_RUNNING_RECORD_ID).toString();

        // for testing
        runId = "1493580223711";

        // plot running record
        plotRunningRecordByKey(runId);


    }

    private void buildMapFragment(){
        // Build map fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mapsFragment = new MapsFragment();
        fragmentTransaction
                .add(R.id.frame_track_me_fragment_map, mapsFragment)
                .commit();
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

    protected void plotRunningRecordByKey(String runId){

        DatabaseReference runningRecordRef = getRunningPathRefById(runId);
//        runningRecordArrayList = new ArrayList<TimestampedLocation>();
        runningRecord = new RunningRecord();

        runningRecordRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable <DataSnapshot> children = dataSnapshot.getChildren();
                runningRecordArrayList = new ArrayList<TimestampedLocation>();
                for (DataSnapshot child: children){

//                    runningRecordArrayList.add(child.getValue(TimestampedLocation.class));
                    runningRecord.addTimestampedLocation(child.getValue(TimestampedLocation.class));
                    Log.d("timeStampedLocations",child.getValue(TimestampedLocation.class).getLatitude()+"");
                }

                mapsFragment.addCompletedPolylinePath(runningRecord);
                Log.d("path size:", ""+runningRecord.getRunningPath().size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
