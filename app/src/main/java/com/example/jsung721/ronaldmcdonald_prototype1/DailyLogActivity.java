package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DailyLogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_log);

        Button DailyLogBack_Button = (Button) findViewById(R.id.DailyLog_Back_Button);

        DailyLogBack_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent DailyLog_to_History = new Intent(DailyLogActivity.this, UserHistoryActivity.class);
                startActivity(DailyLog_to_History);

            }
        });
    }
}
