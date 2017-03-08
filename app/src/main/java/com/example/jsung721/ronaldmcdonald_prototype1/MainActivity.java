package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;




public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button ViewP = (Button) findViewById(R.id.ViewProf);
        Button TrackM = (Button) findViewById(R.id.TrackMe);
        Button Sett = (Button) findViewById(R.id.Settings);

        ViewP.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent1 = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent1);

            }
        });

        TrackM.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent2 = new Intent(MainActivity.this, Main4Activity.class);
                startActivity(intent2);

            }
        });

        Sett.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent3 = new Intent(MainActivity.this, Main5Activity.class);
                startActivity(intent3);

            }
        });
    }
}
