package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private TextView profileTotalMilesValueTextView;
    private TextView profileTotalTimeValueTextView;
    private TextView profileAveragePaceTextView;
    private TextView profileCaloriesBurnedValueTextView;
    public FirebaseDatabase database;
    public DatabaseReference ref;
    public DatabaseReference ref2;
    private int weight;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        Button profileToMenuButton = (Button) findViewById(R.id.button_profile_to_menu);
        Button profileToGraphsButton = (Button) findViewById(R.id.button_profile_to_graphs);
        Button profileToDailyLogButton = (Button) findViewById(R.id.button_profile_to_daily_log);
        profileTotalMilesValueTextView  = (TextView)findViewById(R.id.text_profile_total_miles_value);
        profileTotalTimeValueTextView = (TextView) findViewById(R.id.text_profile_total_time_value);
        profileAveragePaceTextView  = (TextView) findViewById(R.id.text_profile_avg_pace_value);
        profileCaloriesBurnedValueTextView = (TextView)findViewById(R.id.text_profile_calories_burned_value);

        database = FirebaseDatabase.getInstance();
        ref = database.getReferenceFromUrl("https://myfitnesstracker-4c9c0.firebaseio.com/users/userkey1");

        ref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                UserInfo u = new UserInfo();
                for (DataSnapshot child:children)
                {
                    u.setDistance(u.getDistance() + child.child("totalDistanceRun").getValue(Integer.class));
                    u.setTime(u.getTime()+ child.child("totalTimeElapsed").getValue(Integer.class));
                }
                profileTotalMilesValueTextView.setText(""+ u.getDistance() + " Miles");
                profileTotalTimeValueTextView.setText("" + u.getTime() + " Min");
                profileAveragePaceTextView.setText("" + ((u.getDistance()/u.getTime()) + " Miles/Min"));
                profileCaloriesBurnedValueTextView.setText("" + );

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
