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
    Spinner TrackMeMode_Spinner;
    ArrayAdapter <CharSequence> TrackMeMode_Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_me);

        Button TrackMeBack_Button = (Button) findViewById(R.id.TrackMe_Back_Button);

        TrackMeMode_Spinner = (Spinner)findViewById(R.id.TrackMe_Mode_Spinner);
        TrackMeMode_Adapter = ArrayAdapter.createFromResource(this,R.array.Track_Mode,android.R.layout.simple_spinner_item);
        TrackMeMode_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TrackMeMode_Spinner.setAdapter(TrackMeMode_Adapter);

        TrackMeMode_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        TrackMeBack_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent TrackMe_to_MainMenu = new Intent(TrackMeActivity.this, StrideMainMenuActivity.class);
                startActivity(TrackMe_to_MainMenu);

            }
        });

        Button StartTrackingButton = (Button) findViewById(R.id.TrackMe_Track_Button);
        StartTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrackMeActivity.this, TestSendDataActivity.class);
                startActivity(intent);
            }
        });
    }
}
