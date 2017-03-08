package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Button TMBack = (Button) findViewById(R.id.tmBack);

        TMBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent8 = new Intent(Main4Activity.this, MainActivity.class);
                startActivity(intent8);

            }
        });

        Button StartTrackingButton = (Button) findViewById(R.id.rmTrack);
        StartTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main4Activity.this, TestSendDataActivity.class);
                startActivity(intent);
            }
        });
    }
}
