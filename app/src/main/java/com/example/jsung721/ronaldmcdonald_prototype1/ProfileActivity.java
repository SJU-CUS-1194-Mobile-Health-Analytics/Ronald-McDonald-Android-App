package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        Button profileToMenuButton = (Button) findViewById(R.id.button_profile_to_menu);
        Button profileToGraphsButton = (Button) findViewById(R.id.button_profile_to_graphs);
        Button profileToDailyLogButton = (Button) findViewById(R.id.button_profile_to_daily_log);
        

        profileToMenuButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent profileToMenuIntent = new Intent(ProfileActivity.this, StrideMainMenuActivity.class);
                startActivity(profileToMenuIntent);

            }
        });
        profileToGraphsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent profileToGraphsIntent = new Intent(ProfileActivity.this, GraphsActivity.class);
                startActivity(profileToGraphsIntent);

            }
        });
        profileToDailyLogButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent profileToDailyLogIntent = new Intent(ProfileActivity.this, DailyLogActivity.class);
                startActivity(profileToDailyLogIntent);

            }
        });
    }
}
