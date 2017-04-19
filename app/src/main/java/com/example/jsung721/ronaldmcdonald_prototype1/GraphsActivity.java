package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class GraphsActivity extends AppCompatActivity {
    Spinner graphsTimeSpinner, graphsStatSpinner, graphsMeasureSpinner;
    ArrayAdapter <CharSequence> graphsTimeAdapter, graphsStatAdapter, graphsMeasureAdapter;
    public FirebaseDatabase database;
    public DatabaseReference ref;
    LineGraphSeries <DataPoint> series;
    private int count;
    ArrayList<DataPoint> points;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphs);
        count = 0;
        Button GraphsBack_Button = (Button) findViewById(R.id.button_graphs_to_profile);

        graphsTimeSpinner = (Spinner) findViewById(R.id.spinner_graphs_time);
        graphsTimeAdapter = ArrayAdapter.createFromResource(this,R.array.Times,android.R.layout.simple_spinner_item);
        graphsTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        graphsTimeSpinner.setAdapter(graphsTimeAdapter);

        graphsStatSpinner = (Spinner) findViewById(R.id.spinner_graphs_statistics);
        graphsStatAdapter = ArrayAdapter.createFromResource(this,R.array.Stats,android.R.layout.simple_spinner_item);
        graphsStatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        graphsStatSpinner.setAdapter(graphsStatAdapter);

        graphsMeasureSpinner = (Spinner) findViewById(R.id.spinner_graphs_measure);
        graphsMeasureAdapter = ArrayAdapter.createFromResource(this,R.array.Measure,android.R.layout.simple_spinner_item);
        graphsMeasureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        graphsMeasureSpinner.setAdapter(graphsMeasureAdapter);

            GraphView graph = (GraphView) findViewById(R.id.graph);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(10);

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(10);
            graph.getViewport().setMaxX(10);

            // enable scaling and scrolling
            graph.getViewport().setScalable(true);
            graph.getViewport().setScalableY(true);
            series = new LineGraphSeries<>();

        database = FirebaseDatabase.getInstance();
        ref = database.getReferenceFromUrl("https://myfitnesstracker-4c9c0.firebaseio.com/users/userkey1");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                UserInfo u = new UserInfo();
                Iterable <DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child:children) {
                    u.setDistance(child.child("totalDistanceRun").getValue(Integer.class));
                    series.appendData(new DataPoint(count,u.getDistance()),true,5);
                    count++;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        graph.addSeries(series);

        graphsTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView spinnerDialog = (TextView) view;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        graphsStatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        graphsMeasureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        GraphsBack_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent graphsToProfileIntent = new Intent(GraphsActivity.this, ProfileActivity.class);
                startActivity(graphsToProfileIntent);

            }
        });
    }

    public void startTestSendDataActivity(View view) {
        Intent launch = new Intent(this, TestSendDataActivity.class);
        startActivity(launch);
    }
}
