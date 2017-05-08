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


import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import edu.stjohns.cus1194.stride.data.UserProfile;
import edu.stjohns.cus1194.stride.db.RunSummariesByUserDBAccess;
import edu.stjohns.cus1194.stride.db.UserProfileDBAccess;

import static edu.stjohns.cus1194.stride.db.RunSummariesByUserDBAccess.getRunSummaryByRunId;
import static edu.stjohns.cus1194.stride.db.RunningRecordsDBAccess.getRunningPathRefById;

/**
 * Created by yzhan265 on 5/6/2017.
 */

public class DisplayPastRunActivity extends AppCompatActivity {

    // Google Maps
    protected MapsFragment mapsFragment;
    protected GoogleMap myMap;

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
    protected RunSummary runSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_me);

        // Build map fragment
        buildMapFragment();

        // initiate views and hide unused views
        initUI();
        initUserInfo();

        // get run key from previous activity
//        Bundle bundle = getIntent().getExtras();
//        runId = bundle.get(FLAG_RUNNING_RECORD_ID).toString();

        // for testing
        runId = "1494278795892";

        retrieveRunSummary();

        // wait for map to be ready
        onMapReadyDrawPolyline();

    }
    protected void onMapReadyDrawPolyline(){
        Thread waitForMapReady = new Thread(){
            @Override
            public void run(){
                try{
                    while (true){
                        if(mapsFragment.mMap != null){
                            myMap = mapsFragment.mMap;
                            // plot running record
                            plotRunningRecordByKey(runId);
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
                //trackMeToMenuIntent.putParcelableArrayListExtra(INTENT_RUNNING_RECORDS_KEY, runningRecordArrayList);
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

        // TextViews
        milesValueTextView = (TextView) findViewById(R.id.text_track_me_miles_value);
        timeValueTextView = (TextView) findViewById(R.id.text_track_me_time_value);
        paceValueTextView = (TextView) findViewById(R.id.text_track_me_pace_value);
    }

    private void updateUI(RunSummary runSummary){
        milesValueTextView.setText(String.format("%.2f", runSummary.calculateMiles()));
        timeValueTextView.setText(String.format("%02d : %02d",
                (int)Math.floor(runSummary.calculateMinutes()),
                (int)Math.floor(runSummary.calculateSeconds())));
        paceValueTextView.setText(String.format("%02d : %02d",
                (int)Math.floor(runSummary.calculateMinutesPerMile()),
                (int)Math.floor(runSummary.calculateMinutesPerMile()*(60/100))));
        Date date = new Date(Long.parseLong(runId));
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss aa MMM dd, yyyy");
        startTrackingButton.setText(dateFormat.format(date));
    }

    protected void retrieveRunSummary(){
        DatabaseReference runSummaryRef = getRunSummaryByRunId(mUser.getUid(), runId);
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

    private void initUserInfo() {
        // Init FirebaseUser Object
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        // Init UserProfile Object
        // Note that database reads are performed asynchronously, so we should always check that
        // userProfile != null before we do anything that requires it
        userProfile = null;
        UserProfileDBAccess.getUserProfileRefById(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot userProfileSnapshot) {
                if (userProfileSnapshot != null) {
                    userProfile = userProfileSnapshot.getValue(UserProfile.class);
                } else {
                    System.out.println("Failed to retrieve a UserProfile object with the given UserId. ");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Request to get UserProfile from database was cancelled. ");
            }
        });
    }


}
