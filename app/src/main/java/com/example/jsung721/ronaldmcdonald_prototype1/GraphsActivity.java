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

    public class GraphsActivity extends AppCompatActivity {
    Spinner graphsTimeSpinner, graphsStatSpinner, graphsMeasureSpinner;
    ArrayAdapter <CharSequence> graphsTimeAdapter, graphsStatAdapter, graphsMeasureAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphs);
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
