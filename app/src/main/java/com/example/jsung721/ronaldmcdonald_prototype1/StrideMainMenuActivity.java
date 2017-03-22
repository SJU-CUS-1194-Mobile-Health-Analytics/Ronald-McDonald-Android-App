package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class StrideMainMenuActivity extends AppCompatActivity {

    private String mUserName;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stride_main_menu);

        mUserName = "anonymous";

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUserName = mFirebaseUser.getDisplayName();
        }

        Toast.makeText(getApplicationContext(), "Logged in as: " + mUserName, Toast.LENGTH_LONG).show();

        Button MainMenuViewProfile_Button = (Button) findViewById(R.id.MainMenu_ViewProfile_Button);
        Button MainMenuTrackMe_Button = (Button) findViewById(R.id.MainMenu_TrackMe_Button);
        Button MainMenuSettings_Button = (Button) findViewById(R.id.MainMenu_Settings_Button);

        MainMenuViewProfile_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent MainMenu_to_UserHistory = new Intent(StrideMainMenuActivity.this, UserHistoryActivity.class);
                startActivity(MainMenu_to_UserHistory);

            }
        });

        MainMenuTrackMe_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent MainMenu_to_TrackMe = new Intent(StrideMainMenuActivity.this, TrackMeActivity.class);
                startActivity(MainMenu_to_TrackMe);

            }
        });

        MainMenuSettings_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent MainMenu_to_Settings = new Intent(StrideMainMenuActivity.this, SettingsActivity.class);
                startActivity(MainMenu_to_Settings);

            }
        });
    }
}
