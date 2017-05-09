package com.example.jsung721.ronaldmcdonald_prototype1;


import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import edu.stjohns.cus1194.stride.data.RunningRecord;
import edu.stjohns.cus1194.stride.data.TimestampedLocation;
import edu.stjohns.cus1194.stride.db.RunningRecordsDBAccess;


public class MapsFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,OnMapReadyCallback {

    protected GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    protected Marker markerEnd;
    protected Marker markerStart;
    protected Polyline polyline;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

//    protected Polyline polyline;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        checkLocationPermission();


    }

    public static MapsFragment newInstance(){
        MapsFragment fragment = new MapsFragment();
        return  fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_maps, null, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }


    protected void moveMapCamera(Location location){
        if (location != null) {
            moveMapCamera(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    protected void moveMapCamera(LatLng latLng){

        if (latLng != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    /**
     * This method is for active running.
     * add polyline path and start marker to path
     * @param runningRecord
     * @return
     */
    protected Polyline addPolylinePath(RunningRecord runningRecord){
        // Instantiates a new Polyline object and adds points to define a rectangle
        final PolylineOptions pathOptions = new PolylineOptions()
                .color(Color.BLUE);
        for (TimestampedLocation t: runningRecord.getRunningPath()){
                pathOptions.add(new LatLng(t.getLatitude(), t.getLongitude()));
        }

        // start marker
        MarkerOptions markerStartOptions = new MarkerOptions()
                .position(pathOptions.getPoints().get(0))
                .title("Start")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                .anchor((float)0.5,(float)0.5)
//                .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.presence_online));

        if(markerStart != null){markerStart.remove();}
        markerStart = mMap.addMarker(markerStartOptions);

        if(polyline != null){polyline.remove();}
        // Get back the mutable Polyline
        polyline = mMap.addPolyline(pathOptions);

        return polyline;

    }

    /**
     * This method is for plotting polylines for a previous run.
     * @param runningRecord
     */
    protected void addCompletedPolylinePath(RunningRecord runningRecord){

        if (runningRecord.getRunningPath().size() <= 1){
            Log.d("addCompletedPath","Error: path size ="+runningRecord.getRunningPath().size());
            return;}

        ArrayList<TimestampedLocation> path = runningRecord.getRunningPath();
        long min = -1;
        long max = -1;

        for (int i = 1; i<path.size();i++){
            if (min < 0 || min > path.get(i).getTime()-path.get(i-1).getTime()){
                min = path.get(i).getTime()-path.get(i-1).getTime();
            }
            if (max < 0 || max < path.get(i).getTime()-path.get(i-1).getTime()){
                max = path.get(i).getTime()-path.get(i-1).getTime();
            }
        }

        for (int i = 1; i<path.size();i++){
            this.mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(path.get(i-1).getLatitude(), path.get(i-1).getLongitude()))
                    .add(new LatLng(path.get(i).getLatitude(), path.get(i).getLongitude()))
                    .color(getSpeedColor(path.get(i).getTime()-path.get(i-1).getTime(), min, max)));
            Log.d("polyline color",getSpeedColor(path.get(i).getTime()-path.get(i-1).getTime(), min, max)+"");
        }

        // start marker
        MarkerOptions markerStartOptions = new MarkerOptions()
                .position(new LatLng(path.get(0).getLatitude(), path.get(0).getLongitude()))
                .title("Start")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));


        // end marker
        MarkerOptions markerEndOptions = new MarkerOptions()
                .position(new LatLng(path.get(path.size()-1).getLatitude(), path.get(path.size()-1).getLongitude()))
                .title("End")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        if(markerStart != null){markerStart.remove();}
        markerStart = mMap.addMarker(markerStartOptions);

        if(markerEnd != null){markerEnd.remove();}
        markerEnd = mMap.addMarker(markerEndOptions);

        if(polyline != null){polyline.remove();}

        moveMapCamera(new LatLng(path.get(0).getLatitude(), path.get(0).getLongitude()));
    }

    /**
     * Calculates what the color of the line segment should be
     * @param curTime
     * @param minTime
     * @param maxTime
     * @return
     */

    protected int getSpeedColor(long curTime, long minTime, long maxTime){

//        Red = {255, 0,0};
//        Yellow = {255,255,0};
//        Green = {0,255,0};
        int Red = (int)Math.round(254*(1-curTime*1.0/(1.0*maxTime - minTime)));
        int Green = (int)Math.round(254*(curTime*1.0/(1.0*maxTime - minTime)));
        // alpha, red, green, blue
        int RGB = android.graphics.Color.argb(100, Red, Green, 0);
        return RGB;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
