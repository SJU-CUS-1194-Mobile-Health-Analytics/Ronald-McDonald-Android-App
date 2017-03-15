package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_run_history);
        Button HistoryBack_Button = (Button) findViewById(R.id.History_Back_Button);
        Button HistoryGraph_Button = (Button) findViewById(R.id.HistoryGraph_Button);
        Button HistoryDailyLog_Button = (Button) findViewById(R.id.HistoryDailyLog_Button);
        

        HistoryBack_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent History_Back_To_Main = new Intent(UserHistoryActivity.this, StrideMainMenuActivity.class);
                startActivity(History_Back_To_Main);

            }
        });
        HistoryGraph_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent History_To_Graph = new Intent(UserHistoryActivity.this, GraphsActivity.class);
                startActivity(History_To_Graph);

            }
        });
        HistoryDailyLog_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent History_To_DailyLog = new Intent(UserHistoryActivity.this, DailyLogActivity.class);
                startActivity(History_To_DailyLog);

            }
        });
    }
}
