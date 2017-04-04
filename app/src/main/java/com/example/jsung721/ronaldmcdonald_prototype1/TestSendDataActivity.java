package com.example.jsung721.ronaldmcdonald_prototype1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.Date;

public class TestSendDataActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String date;
    private String time;
    private double distance;
    protected boolean chronometerOn = false;

    private final String RUNNING_RECORDS = "running records";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_send_data);

        final EditText editTextDistance = (EditText) findViewById(R.id.text_test_distance);
        editTextDistance.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                editTextDistance.setText(""+0);
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    distance = Double.parseDouble((String)v.getText());
                    handled = true;
                }
                return handled;
            }
        });

        DatePicker datePicker = (DatePicker) findViewById(R.id.picker_test_date);
        datePicker.updateDate(2017,6,22);
        DatePicker.OnDateChangedListener dateChangedListener = new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                view.updateDate(year, monthOfYear, dayOfMonth);
                date = year+"-"+monthOfYear+"-"+dayOfMonth;

            }
        };
    }

    public void sendTestData(View view) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(RUNNING_RECORDS);
//        RunningRecords r = new RunningRecords(date,time, distance);
        final DatabaseReference pushReference = myRef.push();
        pushReference.setValue(null);


    }

    public void changeChronometer(View view) {
        Button buttonChronometer = (Button) view;
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer_test);
        chronometer.setFormat("%s");
        if (!chronometerOn) {
            buttonChronometer.setText("Stop");
            chronometer.start();
            chronometerOn = true;
        } else {
            buttonChronometer.setText("Start");
            chronometer.stop();
            time = chronometer.getFormat();
            chronometerOn = false;
        }
    }
}
