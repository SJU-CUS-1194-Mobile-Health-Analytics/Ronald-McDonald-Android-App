package com.example.jsung721.ronaldmcdonald_prototype1;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import edu.stjohns.cus1194.stride.data.RunSummary;
import edu.stjohns.cus1194.stride.data.RunningRecord;
import edu.stjohns.cus1194.stride.data.TimestampedLocation;
import edu.stjohns.cus1194.stride.data.UserProfile;
import edu.stjohns.cus1194.stride.db.RunSummariesByUserDBAccess;
import edu.stjohns.cus1194.stride.db.RunningRecordsDBAccess;
import edu.stjohns.cus1194.stride.db.UserProfileDBAccess;

/**
 * Getting Location Updates.
 *
 * Demonstrates how to use the Fused Location Provider API to get updates about a device's
 * location. The Fused Location Provider is part of the Google Play services location APIs.
 *
 * For a simpler example that shows the use of Google Play services to fetch the last known location
 * of a device, see
 * https://github.com/googlesamples/android-play-location/tree/master/BasicLocation.
 *
 * This sample uses Google Play services, but it does not require authentication. For a sample that
 * uses Google Play services for authentication, see
 * https://github.com/googlesamples/android-google-accounts/tree/master/QuickStart.
 */
public class TrackMeSendDataActivity extends TrackMeBaseActivity {

    // Firebase
    FirebaseUser mUser;
    UserProfile userProfile;

    // UI elements
    protected Button trackMeBackButton;
    protected Spinner trackMeModeSpinner;
    protected ArrayAdapter<CharSequence> trackMeModeAdapter;
    protected Button startTrackingButton;
    protected TextView milesValueTextView;
    protected TextView timeValueTextView;
    protected TextView paceValueTextView;

    public final double METERS_TO_MILES_CONSTANT = 0.000621371;
    protected Thread updateUiThread;

    protected static final String TAG = "location-updates-sample";

    // Value changes when the user presses the Start Updates and Stop Updates buttons.
    protected boolean mTrackingLocationUpdates;
    protected boolean readyToTrack;
    protected final int RADIAL_ACCURACY_THRESHOLD = 25; // meters

    // Variables for the run currently being tracked
    protected RunningRecord runningRecord;
    protected long totalDistanceRun;

    // Google Maps
    protected MapsFragment mapsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_me);
        initUI();
        initUserInfo();

        mTrackingLocationUpdates = false;
        runningRecord = new RunningRecord();

        // Build map fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mapsFragment = new MapsFragment();
        fragmentTransaction
                .add(R.id.frame_track_me_fragment_map, mapsFragment)
                .commit();

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

    private void initUI() {
        // Button to go back to the main menu
        trackMeBackButton = (Button) findViewById(R.id.button_track_me_to_menu);
        trackMeBackButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent trackMeToMenuIntent = new Intent(TrackMeSendDataActivity.this, StrideMainMenuActivity.class);
                //trackMeToMenuIntent.putParcelableArrayListExtra(INTENT_RUNNING_RECORDS_KEY, runningRecordArrayList);
                startActivity(trackMeToMenuIntent);

            }
        });

        // Spinner for trackMeMode
        trackMeModeSpinner = (Spinner)findViewById(R.id.spinner_track_me_mode);
        trackMeModeAdapter = ArrayAdapter.createFromResource(this,R.array.Track_Mode,android.R.layout.simple_spinner_item);
        trackMeModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trackMeModeSpinner.setAdapter(trackMeModeAdapter);
        trackMeModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Button to start/stop tracking
        startTrackingButton = (Button) findViewById(R.id.button_track_me_start_tracking);
        startTrackingButton.setEnabled(true);
//        startTrackingButton.setBackgroundColor(Color.GRAY);
        startTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check that the userProfile object has been retrieved from the database
                // check that there is sufficient accuracy of location before tracking begins
                if (userProfile != null && readyToTrack) {
                    Toast.makeText(getApplicationContext(), "change tracking",Toast.LENGTH_SHORT).show();
                    changeTrackingState();
                }
            }
        });

        // TextViews
        milesValueTextView = (TextView) findViewById(R.id.text_track_me_miles_value);
        timeValueTextView = (TextView) findViewById(R.id.text_track_me_time_value);
        paceValueTextView = (TextView) findViewById(R.id.text_track_me_pace_value);
    }


    /**
     * Ensures that only one button is enabled at any time. The Start Updates button is enabled
     * if the user is not requesting location updates. The Stop Updates button is enabled if the
     * user is requesting location updates.
     */
    private void changeTrackingState() {
        if (mTrackingLocationUpdates) {
            mTrackingLocationUpdates = false;
            totalDistanceRun = 0;
            updateUiThread.interrupt();
            this.startTrackingButton.setText("START TRACKING");
            if (runningRecord.getRunningPath().size() > 0) {
                this.sendData();
            }
        } else {
            mTrackingLocationUpdates = true;
            updateUiThread = createUiUpdatesThread();
            updateUiThread.start();
            this.startTrackingButton.setText("STOP TRACKING");
        }
        updateUI();
    }

    protected Thread createUiUpdatesThread(){
        Thread updateUiThread = new Thread(){
            @Override
            public void run(){
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView here
                                updateUI();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        return updateUiThread;
    }

    /**
     * Updates the latitude, the longitude, and the last location time in the UI.
     */
    private void updateUI() {
        long timeElapsed;
        if(runningRecord.getRunningPath().isEmpty()){
            timeElapsed = 0;
            totalDistanceRun = 0;
        }else{
            timeElapsed = System.currentTimeMillis() - runningRecord.getRunningPath().get(0).getTime();
        }
        long milliseconds = timeElapsed;
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        timeValueTextView.setText(String.format("%02d : %02d", minutes, seconds));
        milesValueTextView.setText(String.format("%.2f",totalDistanceRun*METERS_TO_MILES_CONSTANT));

        // pace = min/mile
        if (totalDistanceRun > 0){
            double pace = getDistanceFromPreviousLocation()/(60*1000.0*(totalDistanceRun*METERS_TO_MILES_CONSTANT));
            paceValueTextView.setText(String.format("%.2f",((double)milliseconds)/(60*1000.0*(totalDistanceRun*METERS_TO_MILES_CONSTANT))));
        } else {
            paceValueTextView.setText("0.00");
        }

    }

    private void sendData() {
        // User id
        String userId = mUser.getUid();

        // Add runningRecord to the DB
        String runId = "" + runningRecord.getRunningPath().get(0).getTime();
        RunningRecordsDBAccess.addRunningRecord(runId, runningRecord);

        // Add runSummary to the DB
        long totalTimeElapsedInMillis = mLastUpdateTime - runningRecord.getRunningPath().get(0).getTime();
        RunSummary runSummary = new RunSummary(Long.parseLong(runId), totalTimeElapsedInMillis, totalDistanceRun, userProfile.getWeight());
        RunSummariesByUserDBAccess.addRunForUser(userId, runId, runSummary);

        // Update user's historical stats
        userProfile.updateUserStats(runSummary);
        UserProfileDBAccess.setUserProfileById(userId, userProfile);

        // Reset the runningRecord variable
        runningRecord = new RunningRecord();
    }

    private void addRecord() {
        if(mCurrentLocation != null){
            TimestampedLocation t = new TimestampedLocation(
                    this.mLastUpdateTime,
                    mCurrentLocation.getLatitude(),
                    mCurrentLocation.getLongitude());
            runningRecord.getRunningPath().add(t);
        }

        totalDistanceRun += getDistanceFromPreviousLocation();

    }

    protected double getDistanceFromPreviousLocation(){
        if (runningRecord.getRunningPath().size() > 1) {
            TimestampedLocation prev = runningRecord.getRunningPath().get(runningRecord.getRunningPath().size() - 2);
            Location prevLoc;
            prevLoc = new Location(mCurrentLocation);
            prevLoc.setLatitude(prev.getLatitude());
            prevLoc.setLongitude(prev.getLongitude());
            return prevLoc.distanceTo(mCurrentLocation);
        }

        return 0;
    }

    @Override
    protected void onStop() {
//        mGoogleApiClient.disconnect();
        super.onStop();
        if (updateUiThread != null) {
            updateUiThread.interrupt();
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        if(super.checkLocationPermission()) {
            mapsFragment.mMap.setMyLocationEnabled(true);
            mLastUpdateTime = System.currentTimeMillis();
            mapsFragment.moveMapCamera(mCurrentLocation);
            updateUI();
            waitForLocationPrecision();
        }
//            }
//        }

//        startLocationUpdates();
    }

    /**
     * wait for 5 seconds after a connection is established
     * check if the accuracy of the current location is less than the threshold
     */
    protected void waitForLocationPrecision(){
        Thread wait = new Thread(){
            @Override
            public void run(){
                try{
                    Thread.sleep(5000);
                    while(mCurrentLocation.getAccuracy() > RADIAL_ACCURACY_THRESHOLD) {
                        Thread.sleep(1000);
                    }
                    readyToTrack = true;
                } catch (InterruptedException e){

                }
            }
        };
        wait.start();
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
//        mCurrentLocation = location;
        Toast.makeText(this,
                "Location Accuracy: hasAccuracy:"+mCurrentLocation.hasAccuracy()+" "+mCurrentLocation.getAccuracy(),
                Toast.LENGTH_SHORT).show();
        if (mTrackingLocationUpdates) {
//            mLastUpdateTime = System.currentTimeMillis();
            updateUI();
            addRecord();
            // add polyline
            mapsFragment.addPolylinePath(this.runningRecord);
        }
        //move map camera
        mapsFragment.moveMapCamera(mCurrentLocation);



    }

    @Override
    public void onRestoreInstanceState(Bundle outState){
        super.onRestoreInstanceState(outState);
//        updateValuesFromBundle(outState);
        //TODO: update ui from outstate!!
        updateUI();
    }

}
