package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.stjohns.cus1194.stride.data.RunSummary;
import edu.stjohns.cus1194.stride.data.RunningRecord;
import edu.stjohns.cus1194.stride.data.TimestampedLocation;

import static com.example.jsung721.ronaldmcdonald_prototype1.R.id.text_track_me_pace;
import static edu.stjohns.cus1194.stride.db.RunSummariesByUserDBAccess.getRunSummaryByRunId;
import static edu.stjohns.cus1194.stride.db.RunningRecordsDBAccess.getRunningPathRefById;

public class DisplayPastRunActivity extends BaseActivity {

    // Google Maps
    protected MapsFragment mapsFragment;

    // UI elements
    protected Button trackMeBackButton;
    protected Spinner trackMeModeSpinner;
    protected Button startTrackingButton;
    protected TextView milesValueTextView;
    protected TextView timeValueTextView;
    protected TextView paceValueTextView;
    protected TextView paceTextTextView;

    // constant for passing a run ID to this activity in the Intent
    public static final String FLAG_RUNNING_RECORD_ID = "RUNNING RECORD ID";

    // running record
    protected RunningRecord runningRecord;
    protected ArrayList<TimestampedLocation> runningRecordArrayList;
    protected RunSummary runSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_me);

        // Build map fragment
        buildMapFragment();

        // initiate views and hide unused views
        initUI();

        // get run key from previous activity
        Bundle bundle = getIntent().getExtras();
        String runId = bundle.getString(FLAG_RUNNING_RECORD_ID);
        retrieveRunSummary(runId);

    }
    protected void onMapReadyDrawPolyline(){
        Thread waitForMapReady = new Thread(){
            @Override
            public void run(){
                try{
                    while (true){
                        if(mapsFragment.mMap != null){
                            // plot running record
                            plotRunningRecordByKey("" + runSummary.getTimeKey());
                            break;
                        } else {
                            Thread.sleep(10);
                        }
                    }

                } catch(InterruptedException e){

                }
            }
        };

        waitForMapReady.start();
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
                startActivity(trackMeToMenuIntent);

            }
        });

        // Hide: Spinner for trackMeMode
        trackMeModeSpinner = (Spinner)findViewById(R.id.spinner_track_me_mode);
        trackMeModeSpinner.setVisibility(View.INVISIBLE);
        // Disable: Button to start/stop tracking
        startTrackingButton = (Button) findViewById(R.id.button_track_me_start_tracking);
        startTrackingButton.setEnabled(false);
        startTrackingButton.setText("Run Date");
        // Change: Label for pace
        paceTextTextView = (TextView) findViewById(R.id.text_track_me_pace);
        paceTextTextView.setText("Average Pace:");

        // TextViews
        milesValueTextView = (TextView) findViewById(R.id.text_track_me_miles_value);
        timeValueTextView = (TextView) findViewById(R.id.text_track_me_time_value);
        paceValueTextView = (TextView) findViewById(R.id.text_track_me_pace_value);
    }

    private void updateUI(RunSummary runSummary){
        milesValueTextView.setText(runSummary.printDistanceInMiles());
        timeValueTextView.setText(runSummary.printDuration());
        paceValueTextView.setText(runSummary.printPacePerMile());
        startTrackingButton.setText(runSummary.printDate());

        // wait for map to be ready
        onMapReadyDrawPolyline();
    }

    protected void retrieveRunSummary(String runId){
        DatabaseReference runSummaryRef = getRunSummaryByRunId(mFirebaseUser.getUid(), runId);
        runSummaryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                runSummary = dataSnapshot.getValue(RunSummary.class);
                updateUI(runSummary);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
