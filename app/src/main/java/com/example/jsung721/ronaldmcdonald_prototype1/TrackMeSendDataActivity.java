package com.example.jsung721.ronaldmcdonald_prototype1;

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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
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
public class TrackMeSendDataActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // User info
    private FirebaseUser mUser;
    private UserProfile userProfile;

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

    // permissions
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    protected static final String TAG = "location-updates-sample";

    // The desired interval for location updates. Inexact. Updates may be more or less frequent.
    protected static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    // Provides the entry point to Google Play services.
    protected GoogleApiClient mGoogleApiClient;

    //Stores parameters for requests to the FusedLocationProviderApi.
    protected LocationRequest mLocationRequest;

    // Represents a geographical location.
    protected Location mCurrentLocation;

    // Tracks the status of the location updates request.
    // Value changes when the user presses the Start Updates and Stop Updates buttons.
    protected Boolean mRequestingLocationUpdates;

    // Time when the location was updated represented as a String.
    protected long mLastUpdateTime;

    // Variables for the run currently being tracked
    protected RunningRecord runningRecord;
    protected long totalDistanceRun;

    // Google Maps
    protected MapsFragment mapsFragment;
    protected Polyline polyline;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_me);



        initUserInfo();
        initUI();

        mRequestingLocationUpdates = false;
        mLastUpdateTime = 0;
        runningRecord = new RunningRecord();

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);


        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
        checkLocationPermission();
        buildGoogleApiClient();


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
        startTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check that the userProfile object has been retrieved from the database
                if (userProfile != null) {
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
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        Log.i(TAG, "Updating values from bundle");
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
                changeTrackingState();
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = Long.parseLong(savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY));
            }

        }
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).


        checkLocationPermission();
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        runningRecord = new RunningRecord();
        totalDistanceRun = 0;
        mLastUpdateTime = System.currentTimeMillis();
        addRecord();
    }

    /**
     * Ensures that only one button is enabled at any time. The Start Updates button is enabled
     * if the user is not requesting location updates. The Stop Updates button is enabled if the
     * user is requesting location updates.
     */
    private void changeTrackingState() {
        if (mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;
            updateUiThread.interrupt();
            stopLocationUpdates();
            this.startTrackingButton.setText("START TRACKING");


        } else {
            mRequestingLocationUpdates = true;
            startLocationUpdates();
            beginUiUpdates();
            this.startTrackingButton.setText("STOP TRACKING");
        }
        updateUI();
    }

    protected void beginUiUpdates(){
        updateUiThread = new Thread(){
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
        updateUiThread.start();
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
            paceValueTextView.setText(String.format("%:2f",((double)milliseconds)/(60*1000.0*(totalDistanceRun*METERS_TO_MILES_CONSTANT))));
        } else {
            paceValueTextView.setText("0:00");
        }

    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        if (runningRecord.getRunningPath().size()>0) {
            this.sendData();
        }
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    public void sendData() {
        // User id
        String userId = mUser.getUid();

        // Add runningRecord to the DB
        String runId = "" + runningRecord.getRunningPath().get(0).getTime();
        RunningRecordsDBAccess.addRunningRecord(runId, runningRecord);

        // Add runSummary to the DB
        long totalTimeElapsedInMillis = mLastUpdateTime - runningRecord.getRunningPath().get(0).getTime();
        RunSummary runSummary = new RunSummary(totalTimeElapsedInMillis, totalDistanceRun, userProfile.getWeight());
        RunSummariesByUserDBAccess.addRunForUser(userId, runId, runSummary);

        // Update user's historical stats
        userProfile.updateUserStats(runSummary);
        UserProfileDBAccess.setUserProfileById(userId, userProfile);

        // Reset the runningRecord variable
        runningRecord = new RunningRecord();
    }

    public void addRecord() {
        if(mCurrentLocation != null){
            Toast.makeText(this,
                    "Location Accuracy: hasAccuracy:"+mCurrentLocation.hasAccuracy()+" "+mCurrentLocation.getAccuracy(),
                    Toast.LENGTH_SHORT).show();
            TimestampedLocation t = new TimestampedLocation(
                    this.mLastUpdateTime,
                    mCurrentLocation.getLatitude(),
                    mCurrentLocation.getLongitude());
            runningRecord.getRunningPath().add(t);
        }

        if (runningRecord.getRunningPath().size() > 1) {
            TimestampedLocation prev = runningRecord.getRunningPath().get(runningRecord.getRunningPath().size() - 2);
            Location prevLoc = new Location(mCurrentLocation);
            prevLoc.setLatitude(prev.getLatitude());
            prevLoc.setLongitude(prev.getLongitude());
            this.totalDistanceRun += prevLoc.distanceTo(mCurrentLocation);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            try{
                stopLocationUpdates();
            } catch (Exception e){

            }
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();

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
        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
            if (checkLocationPermission()){
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                mLastUpdateTime = System.currentTimeMillis();
                mapsFragment.moveMapCamera(mCurrentLocation);
                updateUI();
            }
        }

        // If the user presses the Start Updates button before GoogleApiClient connects, we set
        // mRequestingLocationUpdates to true (see startUpdatesButtonHandler()). Here, we check
        // the value of mRequestingLocationUpdates and if it is true, we start location updates.
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = System.currentTimeMillis();
        updateUI();
        addRecord();

        //move map camera
       mapsFragment.moveMapCamera(mCurrentLocation);

        // add polyline
        if(runningRecord.getRunningPath().size() >= 2) {
            mapsFragment.addPolylinePath(this.runningRecord);
        }
    }



    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    /**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, ""+mLastUpdateTime);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle outState){
        updateValuesFromBundle(outState);
        //TODO: update ui from outstate!!
        updateUI();
    }

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            //You can add here other case statements according to your requirement.
        }
    }
}
