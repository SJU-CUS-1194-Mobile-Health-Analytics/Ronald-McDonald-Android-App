package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class TrackMeActivity extends AppCompatActivity {
    Spinner trackMeModeSpinner;
    ArrayAdapter <CharSequence> trackMeModeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_me);

        Button trackMeBackButton = (Button) findViewById(R.id.button_track_me_to_menu);

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
        trackMeBackButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent trackMeToMenuIntent = new Intent(TrackMeActivity.this, StrideMainMenuActivity.class);
                startActivity(trackMeToMenuIntent);

            }
        });

        Button StartTrackingButton = (Button) findViewById(R.id.button_track_me_start_tracking);
        StartTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrackMeActivity.this, TestSendDataActivity.class);
                startActivity(intent);
            }
        });
    }
}
