package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.stjohns.cus1194.stride.data.RunSummary;
import edu.stjohns.cus1194.stride.data.UserProfile;
import edu.stjohns.cus1194.stride.db.RunSummariesByUserDBAccess;
import edu.stjohns.cus1194.stride.db.UserProfileDBAccess;

import static android.R.attr.data;


public class GraphsActivity extends AppCompatActivity
{
    Spinner graphsStatSpinner, graphsMeasureSpinner;
    ArrayAdapter <CharSequence> graphsStatAdapter, graphsMeasureAdapter;
    public DatabaseReference ref;
    public DatabaseReference ref2;
    public UserProfileDBAccess u;
    public RunSummariesByUserDBAccess rs;
    public UserProfile up;
    public String userID;
    public DataPoint [] newData;
    public DataPoint [] newData2;
    public ArrayList <Integer> data;
    public ArrayList <Date> c;
    public ArrayList <RunSummary> runSummaryData;
    public String value;
    LineGraphSeries<DataPoint> series;
    BarGraphSeries <DataPoint> series2;
    private int count;
    private Date d1;

    GraphView graph;
    GraphView graph2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphs);

        Button GraphsBack_Button = (Button) findViewById(R.id.button_graphs_to_profile);


        graphsStatSpinner = (Spinner) findViewById(R.id.spinner_graphs_statistics);
        graphsStatAdapter = ArrayAdapter.createFromResource(this,R.array.Stats,android.R.layout.simple_spinner_item);
        graphsStatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        graphsStatSpinner.setAdapter(graphsStatAdapter);

        graphsMeasureSpinner = (Spinner) findViewById(R.id.spinner_graphs_measure);
        graphsMeasureAdapter = ArrayAdapter.createFromResource(this,R.array.Measure,android.R.layout.simple_spinner_item);
        graphsMeasureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        graphsMeasureSpinner.setAdapter(graphsMeasureAdapter);

        graph = (GraphView) findViewById(R.id.graph);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Miles Run");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Run Number");
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(50);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        runSummaryData = new ArrayList<RunSummary>();
        rs = new RunSummariesByUserDBAccess();
        ref = rs.getRunsByUserRef(userID);
        series = new LineGraphSeries<>();
        c = new ArrayList <Date> ();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable <DataSnapshot> children = dataSnapshot.getChildren();
                runSummaryData = new ArrayList <RunSummary>();
                for (DataSnapshot child: children){

                    runSummaryData.add(child.getValue(RunSummary.class));
                    c.add(new Date(Long.parseLong(child.getKey())));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

                if(value.equalsIgnoreCase("Miles Run"))
                    Toast.makeText(getApplicationContext(), "Date: " + format.format(c.get((int)dataPoint.getX()-1)) + " \nDistance Run: "+ runSummaryData.get((int)dataPoint.getX()-1).calculateMiles() + " miles", Toast.LENGTH_SHORT).show();
                else if(value.equalsIgnoreCase("Time"))
                    Toast.makeText(getApplicationContext(), "Date: " + format.format(c.get((int)dataPoint.getX()-1)) + " \nTime Elapsed: "+ runSummaryData.get((int)dataPoint.getX()-1).printDuration() + " elapsed", Toast.LENGTH_SHORT).show();
                else if (value.equalsIgnoreCase("Pace"))
                    Toast.makeText(getApplicationContext(), "Date: " + format.format(c.get((int)dataPoint.getX()-1)) + " \nPace: "+ runSummaryData.get((int)dataPoint.getX()-1).printPacePerMile() + " min/mi", Toast.LENGTH_SHORT).show();
                else if(value.equals("Calories"))
                    Toast.makeText(getApplicationContext(), "Date: " + format.format(c.get((int)dataPoint.getX()-1)) + " \nCalories: "+ dataPoint.getY() + " Calories", Toast.LENGTH_SHORT).show();
            }
        });

       graphsStatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               value = parent.getItemAtPosition(position).toString();
               if (value.equalsIgnoreCase("Miles Run"))
               {
                   graph.getGridLabelRenderer().setVerticalAxisTitle("Miles Run");
                   newData = new DataPoint [runSummaryData.size()];

                   for (int i = 0; i < runSummaryData.size();i++){

                       newData[i] = new DataPoint(i+1, runSummaryData.get(i).calculateMiles());
                   }
                   series.resetData(newData);
                   graph.getViewport().setMaxY(0.5);
                   graph.addSeries(series);
               }
               else if(value.equalsIgnoreCase("Time"))
               {
                   graph.getGridLabelRenderer().setVerticalAxisTitle("Time Elapsed");

                   for (int i = 0; i < runSummaryData.size();i++){

                       newData[i] = new DataPoint(i+1,runSummaryData.get(i).calculateMinutes() );
                   }
                   series.resetData(newData);
                   graph.getViewport().setMaxY(10.0);
                   graph.addSeries(series);
               }
               else if(value.equalsIgnoreCase("Pace"))
               {
                   graph.getGridLabelRenderer().setVerticalAxisTitle("Pace");

                   for (int i = 0; i < runSummaryData.size();i++){

                       newData[i] = new DataPoint(i+1,runSummaryData.get(i).calculateMinutesPerMile());
                   }
                   series.resetData(newData);
                   graph.getViewport().setMaxY(25.0);
                   graph.addSeries(series);
               }
               else if (value.equalsIgnoreCase("Calories"))
               {
                   graph.getGridLabelRenderer().setVerticalAxisTitle("Calories");

                   for (int i = 0; i < runSummaryData.size();i++){

                       newData[i] = new DataPoint(i+1,runSummaryData.get(i).getTotalCalories());
                   }
                   series.resetData(newData);
                   graph.getViewport().setMaxY(30.0);
                   graph.addSeries(series);
               }

           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

        graph2 = (GraphView) findViewById(R.id.graph2);
        graph2.getGridLabelRenderer().setVerticalAxisTitle("Miles Run");
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph2);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"Best", "","","Average"});
        graph2.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        series2 = new BarGraphSeries<>();
        graph2.getViewport().setXAxisBoundsManual(true);
        graph2.getViewport().setYAxisBoundsManual(true);
        graph2.getViewport().setScrollable(true); // enables horizontal scrolling
        graph2.getViewport().setScrollableY(true); // enables vertical scrolling
        ref2 = u.getUserProfileRefById(userID);
        up = new UserProfile();
        newData2 = new DataPoint[2];

        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                up = dataSnapshot.getValue(UserProfile.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        graphsMeasureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String value = parent.getItemAtPosition(position).toString();
                int numberOfRuns = runSummaryData.size();
                if (numberOfRuns==0) {numberOfRuns =1;} //this is a bad hack... if the database hasn't responded yet, than we won't be displaying the average
                if (value.equalsIgnoreCase("Distance"))
                {
                    graph2.getGridLabelRenderer().setVerticalAxisTitle("Distance");
                    newData2[0] = new DataPoint(1,(int)up.getLifetimeLongestRunByDistance());
                    Toast.makeText(GraphsActivity.this, "runs: " + numberOfRuns, Toast.LENGTH_SHORT).show();
                    newData2[1] = new DataPoint(10,(int)up.getLifetimeLongestRunByDistance()/numberOfRuns);

                    series2.resetData(newData2);
                    // Shunt the viewport, per v3.1.3 to show the full width of the first and last bars.
                    series2.setDrawValuesOnTop(true);
                    series2.setValuesOnTopColor(Color.WHITE);
                    series2.setSpacing(5);
                    graph2.addSeries(series2);
                    graph2.getViewport().setXAxisBoundsManual(true);
                    graph2.getViewport().setYAxisBoundsManual(true);
                    graph2.getViewport().setMinX(0.5);
                    graph2.getViewport().setMaxX(2.5);
                    graph2.getViewport().setMinY(0);
                    graph2.getViewport().setMaxY(series2.getHighestValueY());

                }
                else if(value.equalsIgnoreCase("Time"))
                {
                    graph2.getGridLabelRenderer().setVerticalAxisTitle("Time Elapsed");
                    newData2[0] = new DataPoint(1,(int)up.getLifetimeLongestRunByTime());
                    newData2[1] = new DataPoint(10,(int)up.getLifetimeLongestRunByTime()/numberOfRuns);

                    series2.resetData(newData2);
                    // Shunt the viewport, per v3.1.3 to show the full width of the first and last bars.
                    series2.setDrawValuesOnTop(true);
                    series2.setValuesOnTopColor(Color.WHITE);
                    series2.setSpacing(5);
                    graph2.addSeries(series2);
                    graph2.getViewport().setXAxisBoundsManual(true);
                    graph2.getViewport().setYAxisBoundsManual(true);
                    graph2.getViewport().setMinX(0.5);
                    graph2.getViewport().setMaxX(2.5);
                    graph2.getViewport().setMinY(0);
                    graph2.getViewport().setMaxY(series2.getHighestValueY());

                }
                else if(value.equalsIgnoreCase("Calories"))
                {
                    graph2.getGridLabelRenderer().setVerticalAxisTitle("Calories");
                    newData2[0] = new DataPoint(1,(int)up.getLifetimeHighestCaloriesBurned());
                    newData2[1] = new DataPoint(10,(int)up.getLifetimeTotalCalories()/numberOfRuns);

                    series2.resetData(newData2);
                    // Shunt the viewport, per v3.1.3 to show the full width of the first and last bars.
                    series2.setDrawValuesOnTop(true);
                    series2.setValuesOnTopColor(Color.WHITE);
                    series2.setSpacing(5);
                    graph2.addSeries(series2);
                    graph2.getViewport().setXAxisBoundsManual(true);
                    graph2.getViewport().setYAxisBoundsManual(true);
                    graph2.getViewport().setMinX(0.5);
                    graph2.getViewport().setMaxX(2.5);
                    graph2.getViewport().setMinY(0);
                    graph2.getViewport().setMaxY(series2.getHighestValueY());

                }

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
//    public void startTestSendDataActivity(View view) {
//        Intent launch = new Intent(this, TestSendDataActivity.class);
//        startActivity(launch);
//    }
}
