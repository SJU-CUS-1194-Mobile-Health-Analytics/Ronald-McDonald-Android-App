package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Button settingsBackButton = (Button) findViewById(R.id.button_settings_to_menu);

        settingsBackButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent settingsToMenuIntent = new Intent(SettingsActivity.this, StrideMainMenuActivity.class);
                startActivity(settingsToMenuIntent);
            }
        });

        Button signOutButton = (Button) findViewById(R.id.button_settings_log_out);

        signOutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent settingsLogOutIntent = new Intent(SettingsActivity.this, SignInSignOutActivity.class);
                startActivity(settingsLogOutIntent);
            }
        });

    }
}
