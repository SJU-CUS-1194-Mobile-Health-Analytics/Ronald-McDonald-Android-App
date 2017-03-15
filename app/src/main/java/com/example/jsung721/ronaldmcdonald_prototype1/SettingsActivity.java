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
        Button SettingsBack_Button = (Button) findViewById(R.id.Settings_Back_Button);

        SettingsBack_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent Settings_to_MainMenu = new Intent(SettingsActivity.this, StrideMainMenuActivity.class);
                startActivity(Settings_to_MainMenu);

            }
        });
    }
}
