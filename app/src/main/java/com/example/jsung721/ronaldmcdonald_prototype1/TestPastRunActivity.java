package com.example.jsung721.ronaldmcdonald_prototype1;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import edu.stjohns.cus1194.stride.data.RunningRecord;
import edu.stjohns.cus1194.stride.data.TimestampedLocation;

public class TestPastRunActivity extends AppCompatActivity {
    protected MapsFragment mapsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_past_run);

        // Build map fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mapsFragment = new MapsFragment();
        fragmentTransaction
                .add(R.id.frame_track_me_fragment_map, mapsFragment)
                .commit();
        RunningRecord runningRecord = new RunningRecord();
        ArrayList<TimestampedLocation> timestampedLocations = new ArrayList<>();
        timestampedLocations.add(new TimestampedLocation(1,40.7336, 73.8124));
        timestampedLocations.add(new TimestampedLocation(2, 40.7363, 73.8161));
        runningRecord.setRunningPath(timestampedLocations);
        Polyline polyline = mapsFragment.addPolylinePath(runningRecord);
    }
}
