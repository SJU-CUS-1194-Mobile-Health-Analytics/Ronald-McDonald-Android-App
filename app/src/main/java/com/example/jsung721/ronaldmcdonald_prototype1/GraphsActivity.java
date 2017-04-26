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


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.BaseSeries;

import java.util.ArrayList;

import static android.R.attr.data;


public class GraphsActivity extends AppCompatActivity
{
    Spinner graphsTimeSpinner, graphsStatSpinner, graphsMeasureSpinner;
    ArrayAdapter <CharSequence> graphsTimeAdapter, graphsStatAdapter, graphsMeasureAdapter;
    public FirebaseDatabase database;
    public DatabaseReference ref;
    public DatabaseReference runSummariesRef;
    public String userID;
    public UserInfo u;
    public DataPoint [] newData;
    public DataPoint [] newData2;
    public ArrayList <Integer> data;
    public ArrayList <Double> data2;
    public String val;
    public int high, low, total;
    public double high2, low2, total2;
    LineGraphSeries<DataPoint> series;
    BarGraphSeries <DataPoint> series2;
    private int count;
    GraphView graph;
    GraphView graph2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphs);
        Button GraphsBack_Button = (Button) findViewById(R.id.button_graphs_to_profile);

      /*  graphsTimeSpinner = (Spinner) findViewById(R.id.spinner_graphs_time);
        graphsTimeAdapter = ArrayAdapter.createFromResource(this,R.array.Times,android.R.layout.simple_spinner_item);
        graphsTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        graphsTimeSpinner.setAdapter(graphsTimeAdapter);*/

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
        database = FirebaseDatabase.getInstance();
        ref = database.getReferenceFromUrl("https://myfitnesstracker-4c9c0.firebaseio.com/");
        runSummariesRef = ref.child("RUN SUMMARIES BY USER").child(userID);
        u = new UserInfo();
        series = new LineGraphSeries<>();
        val = "totalDistanceRun";

        runSummariesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable <DataSnapshot> children = dataSnapshot.getChildren();
                newData = new DataPoint [(int)dataSnapshot.getChildrenCount()];
                count = 1;
                for (DataSnapshot child: children){

                    newData [count-1] = new DataPoint(count,child.child(val).getValue(Integer.class));
                    count++;
                }
                series.resetData(newData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        graph.addSeries(series);

       graphsStatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String value = parent.getItemAtPosition(position).toString();
               val = "totalDistanceRun";
               if (value.equalsIgnoreCase("Meters Run"))
               {
                   graph.getGridLabelRenderer().setVerticalAxisTitle("Miles Run");
                   val = "totalDistanceRun";
                   runSummariesRef.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           Iterable <DataSnapshot> children = dataSnapshot.getChildren();
                           newData = new DataPoint [(int)dataSnapshot.getChildrenCount()];
                           count = 1;
                           for (DataSnapshot child: children){

                               newData [count-1] = new DataPoint(count,child.child(val).getValue(Integer.class));
                               count++;
                           }
                           series.resetData(newData);
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });
                   graph.getViewport().setMaxY(15.0);
                   graph.addSeries(series);
               }
               else if(value.equalsIgnoreCase("Time"))
               {
                   graph.getGridLabelRenderer().setVerticalAxisTitle("Time Elapsed");
                   val = "totalTimeElapsed";
                   runSummariesRef.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           Iterable <DataSnapshot> children = dataSnapshot.getChildren();
                           newData = new DataPoint [(int)dataSnapshot.getChildrenCount()];
                           count = 1;
                           for (DataSnapshot child: children){

                               u.setTime(child.child(val).getValue(Integer.class));
                               newData [count-1] = new DataPoint(count, u.getMinutes());
                               count++;
                           }
                           series.resetData(newData);
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });
                   graph.getViewport().setMaxY(20.0);
                   graph.addSeries(series);
               }
               else if(value.equalsIgnoreCase("Pace"))
               {
                   graph.getGridLabelRenderer().setVerticalAxisTitle("Pace");
                   runSummariesRef.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           Iterable <DataSnapshot> children = dataSnapshot.getChildren();
                           newData = new DataPoint [(int)dataSnapshot.getChildrenCount()];
                           count = 1;
                           for (DataSnapshot child: children){
                               u.setTotalDistanceRun(child.child("totalDistanceRun").getValue(Integer.class));
                               u.setTime(child.child("totalTimeElapsed").getValue(Integer.class));

                               newData [count-1] = new DataPoint((double)count,u.getPace());
                               count++;
                           }
                           series.resetData(newData);
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });
                   graph.getViewport().setMaxY(2.0);
                   graph.addSeries(series);
               }

           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

        graph2 = (GraphView) findViewById(R.id.graph2);
        graph2.getGridLabelRenderer().setVerticalAxisTitle("Miles Run");
        series2 = new BarGraphSeries<>();
        final StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph2);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"High","","", "","","Low","","","","","Avg"});
        graph2.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph2.getViewport().setMinY(0);
        graph2.getViewport().setMaxY(10);
        graph2.getViewport().setMinX(0);
        graph2.getViewport().setMaxX(20);
        graph2.getViewport().setXAxisBoundsManual(true);
        graph2.getViewport().setYAxisBoundsManual(true);
        graph2.getViewport().setScrollable(true); // enables horizontal scrolling
        graph2.getViewport().setScrollableY(true); // enables vertical scrolling

        graphsMeasureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String value = parent.getItemAtPosition(position).toString();
                if (value.equalsIgnoreCase("Distance"))
                {
                    val = "totalDistanceRun";
                    graph2.getGridLabelRenderer().setVerticalAxisTitle("Miles Run");
                    runSummariesRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterable <DataSnapshot> children = dataSnapshot.getChildren();
                            data = new ArrayList<Integer>();
                            count = 0;
                            for (DataSnapshot child: children){

                                data.add(child.child(val).getValue(Integer.class));
                            }
                            high = data.get(0);
                            low = data.get(0);
                            total = 0;

                            for (int i = 0; i < data.size(); i++)
                            {
                                if (data.get(i)>high)
                                    high = data.get(i);
                                if (data.get(i)< low)
                                    low = data.get(i);
                                total = total + data.get(i);
                            }
                            newData2 = new DataPoint[3];
                            newData2 [0] = new DataPoint(0,high);
                            newData2 [1] = new DataPoint(10,low);
                            newData2 [2] = new DataPoint(20,total/data.size());
                            series2.resetData(newData2);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    graph2.getViewport().setMaxX(20);
                    graph2.getViewport().setMaxY((double)high +5.0);
                    graph2.addSeries(series2);
                    series2.setDrawValuesOnTop(true);
                    series2.setValuesOnTopColor(Color.WHITE);
                }
                else if(value.equalsIgnoreCase("Time"))
                {
                    graph2.getGridLabelRenderer().setVerticalAxisTitle("Time Elapsed");
                    val = "totalTimeElapsed";
                    runSummariesRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterable <DataSnapshot> children = dataSnapshot.getChildren();
                            data = new ArrayList<Integer>();
                            count = 0;
                            for (DataSnapshot child: children){

                                data.add(child.child(val).getValue(Integer.class));
                            }
                            high = data.get(0);
                            low = data.get(0);
                            total = 0;

                            for (int i = 0; i < data.size(); i++)
                            {
                                if (data.get(i)>high)
                                    high = data.get(i);
                                if (data.get(i)< low)
                                    low = data.get(i);
                                total = total + data.get(i);
                            }
                            newData2 = new DataPoint[3];
                            newData2 [0] = new DataPoint(0,(int) ((high/(1000*60))));
                            newData2 [1] = new DataPoint(10,(int) ((low/(1000*60))));
                            newData2 [2] = new DataPoint(20,(int) (total/data.size())/(1000*60));
                            series2.resetData(newData2);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    graph2.getViewport().setMaxY((double)high + 5.0);
                    graph2.addSeries(series2);
                    series2.setDrawValuesOnTop(true);
                    series2.setValuesOnTopColor(Color.WHITE);
                }
                else if(value.equalsIgnoreCase("Pace"))
                {
                    graph2.getGridLabelRenderer().setVerticalAxisTitle("Pace");
                    runSummariesRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterable <DataSnapshot> children = dataSnapshot.getChildren();
                            data2 = new ArrayList<Double>();
                            count = 0;
                            for (DataSnapshot child: children){

                                u.setTotalDistanceRun(child.child("totalDistanceRun").getValue(Integer.class));
                                u.setTime((child.child("totalTimeElapsed").getValue(Integer.class)));
                                data2.add(u.getPace());
                            }
                            high2 = data2.get(0);
                            low2 = data2.get(0);
                            total2 = 0;

                            for (int i = 0; i < data2.size(); i++)
                            {
                                if (data2.get(i)>high)
                                    high2 = data2.get(i);
                                if (data2.get(i)< low)
                                    low2= data2.get(i);
                                total2 = total2 + data2.get(i);
                            }
                            newData2 = new DataPoint[3];
                            newData2 [0] = new DataPoint(0,high2);
                            newData2 [1] = new DataPoint(10,low2);
                            newData2 [2] = new DataPoint(20,(total2/data2.size()));
                            series2.resetData(newData2);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    graph2.getViewport().setMaxY(2.0);
                    graph2.addSeries(series2);
                    series2.setDrawValuesOnTop(true);
                    series2.setValuesOnTopColor(Color.WHITE);
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
