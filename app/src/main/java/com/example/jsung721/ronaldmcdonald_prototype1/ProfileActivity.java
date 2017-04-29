package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.stjohns.cus1194.stride.data.UserProfile;
import edu.stjohns.cus1194.stride.db.UserProfileDBAccess;


public class ProfileActivity extends AppCompatActivity {
    private TextView profileTotalMilesValueTextView;
    private TextView profileTotalTimeValueTextView;
    private TextView profileAveragePaceValueTextView;
    private TextView profileCaloriesBurnedValueTextView;
    private TextView profileAgeValueTextView;
    private TextView profileHeightValueTextView;
    private TextView profileWeightValueTextView;
    private String userID;
    private UserProfile u;
    public DatabaseReference ref;
    private UserProfileDBAccess userProfileReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        Button profileToMenuButton = (Button) findViewById(R.id.button_profile_to_menu);
        Button profileToGraphsButton = (Button) findViewById(R.id.button_profile_to_graphs);
        Button profileToDailyLogButton = (Button) findViewById(R.id.button_profile_to_daily_log);



        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        TextView profileName = (TextView) findViewById(R.id.text_user_profile);
        profileName.setText(userName);

        profileTotalMilesValueTextView  = (TextView)findViewById(R.id.text_profile_total_miles_value);
        profileTotalTimeValueTextView = (TextView) findViewById(R.id.text_profile_total_time_value);
        profileAveragePaceValueTextView  = (TextView) findViewById(R.id.text_profile_avg_pace_value);
        profileCaloriesBurnedValueTextView = (TextView)findViewById(R.id.text_profile_calories_burned_value);
        profileAgeValueTextView = (TextView) findViewById(R.id.text_profile_age_value);
        profileHeightValueTextView = (TextView) findViewById(R.id.text_profile_height_value);
        profileWeightValueTextView = (TextView) findViewById(R.id.text_profile_weight_value);

        userProfileReference = new UserProfileDBAccess();
        ref = userProfileReference.getUserProfileRefById(userID);
        u = new UserProfile();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u = dataSnapshot.getValue(UserProfile.class);
                int seconds = (int) (u.getLifetimeTotalTime() / 1000) % 60;
                int minutes = (int) ((u.getLifetimeTotalTime()/(1000*60)) %60);
                int hours = (int)((u.getLifetimeTotalTime() /(1000*60*60) % 60));
                double avgpace = (double)minutes/(double)u.getLifetimeTotalMiles();
                profileAgeValueTextView.setText(""+ u.getAge());
                profileHeightValueTextView.setText(""+u.getHeightInInches());
                profileWeightValueTextView.setText(""+u.getWeight());
                profileTotalMilesValueTextView.setText("" + u.getLifetimeTotalMiles() + " miles");
                profileTotalTimeValueTextView.setText((String.format("%02d : %02d : %02d",hours,minutes,seconds)));
                profileAveragePaceValueTextView.setText(String.format("%.2f",avgpace) + " min/Mi");
                profileCaloriesBurnedValueTextView.setText("" + u.getLifetimeTotalCalories() +" cal");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
