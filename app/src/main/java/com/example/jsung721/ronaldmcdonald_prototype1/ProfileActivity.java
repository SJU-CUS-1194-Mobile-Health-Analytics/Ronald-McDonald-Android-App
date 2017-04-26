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



public class ProfileActivity extends AppCompatActivity {
    private TextView profileTotalMilesValueTextView;
    private TextView profileTotalTimeValueTextView;
    private TextView profileAveragePaceValueTextView;
    private TextView profileCaloriesBurnedValueTextView;
    private TextView profileAgeValueTextView;
    private TextView profileHeightValueTextView;
    private TextView profileWeightValueTextView;
    private String userID;
    private int count;
    private UserInfo u;
    public FirebaseDatabase database;
    public DatabaseReference databaseRef;
    public DatabaseReference runSummariesUserIDRef;
    public DatabaseReference userProfileRef;

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


        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReferenceFromUrl("https://myfitnesstracker-4c9c0.firebaseio.com/");
        runSummariesUserIDRef = databaseRef.child("RUN SUMMARIES BY USER").child(userID);
        userProfileRef = databaseRef.child("USER PROFILES").child(userID);
        u = new UserInfo();
        count = 1;

        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u.setAge(dataSnapshot.child("age").getValue(Integer.class));
                u.setHeightInInches(dataSnapshot.child("heightInInches").getValue(Integer.class));
                u.setWeight(dataSnapshot.child("weight").getValue(Integer.class));

                profileAgeValueTextView.setText(""+ u.getAge());
                profileHeightValueTextView.setText(""+u.getHeightInInches());
                profileWeightValueTextView.setText(""+u.getWeight());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        runSummariesUserIDRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable <DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child:children)
                {
                    u.setTotalDistanceRun(u.getTotalDistanceRun() + child.child("totalDistanceRun").getValue(Integer.class));
                    u.setTime(u.getTime() + child.child("totalTimeElapsed").getValue(Integer.class));
                    count++;

                }

                u.setCount(count);
                profileTotalMilesValueTextView.setText("" + u.getTotalDistanceRun() + " Miles");
                int seconds = (int) (u.getTime() / 1000) % 60;
                int minutes = (int) ((u.getTime()/(1000*60)) %60);
                int hours = (int)((u.getTime() /(1000*60*60) % 60));
                profileTotalTimeValueTextView.setText((String.format("%02d : %02d : %02d",hours,minutes,seconds)));
                double avgpace = (double)u.getTotalDistanceRun()/(double)u.getMinutes();
                profileAveragePaceValueTextView.setText("" + String.format("%.2f",avgpace) + " m/min");
                profileCaloriesBurnedValueTextView.setText("" + u.getCalories() + " Cal");

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
