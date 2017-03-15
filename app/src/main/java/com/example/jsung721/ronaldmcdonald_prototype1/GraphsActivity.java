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
    Spinner GraphsTime_Spinner,GraphsStat_Spinner,GraphsMeasure_Spinner;
    ArrayAdapter <CharSequence> GraphsTime_Adapter,GraphsStat_Adapter,GraphsMeasure_Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.Graphs);
        Button GraphsBack_Button = (Button) findViewById(R.id.Graphs_Back_Button);

        GraphsTime_Spinner = (Spinner) findViewById(R.id.Graphs_Time_Spinner);
        GraphsTime_Adapter = ArrayAdapter.createFromResource(this,R.array.Times,android.R.layout.simple_spinner_item);
        GraphsTime_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        GraphsTime_Spinner.setAdapter(GraphsTime_Adapter);

        GraphsStat_Spinner = (Spinner) findViewById(R.id.Graphs_Stats_Spinner);
        GraphsStat_Adapter = ArrayAdapter.createFromResource(this,R.array.Stats,android.R.layout.simple_spinner_item);
        GraphsStat_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        GraphsStat_Spinner.setAdapter(GraphsStat_Adapter);

        GraphsMeasure_Spinner = (Spinner) findViewById(R.id.Graphs_Measure_Spinner);
        GraphsMeasure_Adapter = ArrayAdapter.createFromResource(this,R.array.Measure,android.R.layout.simple_spinner_item);
        GraphsMeasure_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        GraphsMeasure_Spinner.setAdapter(GraphsMeasure_Adapter);

        GraphsTime_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView spinnerDialog = (TextView) view;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        GraphsStat_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        GraphsMeasure_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                Intent Graphs_To_History = new Intent(GraphsActivity.this, UserHistoryActivity.class);
                startActivity(Graphs_To_History);

            }
        });
    }

    public void startTestSendDataActivity(View view) {
        Intent launch = new Intent(this, TestSendDataActivity.class);
        startActivity(launch);
    }
}
