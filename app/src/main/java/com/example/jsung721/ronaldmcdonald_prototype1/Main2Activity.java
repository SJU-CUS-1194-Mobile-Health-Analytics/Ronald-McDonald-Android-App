package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button BackVP = (Button) findViewById(R.id.uhMainMenu);
        Button GraphVP = (Button) findViewById(R.id.Graph);
        Button DailyVP = (Button) findViewById(R.id.DailyLog);
        

        BackVP.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent4 = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent4);

            }
        });
        GraphVP.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent5 = new Intent(Main2Activity.this, Main3Activity.class);
                startActivity(intent5);

            }
        });
        DailyVP.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent5 = new Intent(Main2Activity.this, Main6Activity.class);
                startActivity(intent5);

            }
        });
    }
}
