package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import edu.stjohns.cus1194.stride.data.UserProfile;
import edu.stjohns.cus1194.stride.db.UserProfileDBAccess;

public class ProfileActivity extends BaseActivity {

    // UI elements
    private TextView profileTotalMilesValueTextView;
    private TextView profileTotalTimeValueTextView;
    private TextView profileAveragePaceValueTextView;
    private TextView profileCaloriesBurnedValueTextView;
    private TextView profileAgeValueTextView;
    private TextView profileHeightValueTextView;
    private TextView profileWeightValueTextView;
    private String value;
    private TextView profileProgress;
    private TextView profileProgressComment;
    // UserProfile Object for logged in user
    private UserProfile userProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        initUI();
        initDB();


    }

    private void initUI() {

        final TextView profileNameTextView = (TextView) findViewById(R.id.text_user_profile);
        profileNameTextView.setText(mFirebaseUser.getDisplayName());

        profileTotalMilesValueTextView  = (TextView)findViewById(R.id.text_profile_total_miles_value);
        profileTotalTimeValueTextView = (TextView) findViewById(R.id.text_profile_total_time_value);
        profileAveragePaceValueTextView  = (TextView) findViewById(R.id.text_profile_avg_pace_value);
        profileCaloriesBurnedValueTextView = (TextView)findViewById(R.id.text_profile_calories_burned_value);
        profileAgeValueTextView = (TextView) findViewById(R.id.text_profile_age_value);
        profileHeightValueTextView = (TextView) findViewById(R.id.text_profile_height_value);
        profileWeightValueTextView = (TextView) findViewById(R.id.text_profile_weight_value);

        profileProgress = (TextView) findViewById(R.id.text_progress_percentage);
        profileProgressComment = (TextView) findViewById(R.id.text_progress_comment);


        Button profileToMenuButton = (Button) findViewById(R.id.button_profile_to_menu);


        profileToMenuButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent profileToMenuIntent = new Intent(ProfileActivity.this, StrideMainMenuActivity.class);
                startActivity(profileToMenuIntent);

            }
        });

        Button profileToGraphsButton = (Button) findViewById(R.id.button_profile_to_graphs);
        profileToGraphsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent profileToGraphsIntent = new Intent(ProfileActivity.this, GraphsActivity.class);
                startActivity(profileToGraphsIntent);

            }
        });

        Button profileToDailyLogButton = (Button) findViewById(R.id.button_profile_to_daily_log);
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

    private void initDB() {
        DatabaseReference userProfileRef = UserProfileDBAccess.getUserProfileRefById(mFirebaseUser.getUid());
        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot userProfileSnapshot) {
                if (userProfileSnapshot != null) {
                    userProfile = userProfileSnapshot.getValue(UserProfile.class);
                    updateUI();
                } else {
                    System.out.println("Error obtaining UserProfile data. Data snapshot null. ");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error obtaining UserProfile data. DB Listener cancelled. ");
            }
        });
    }

    private void updateUI() {
        int num = (int)(((double)userProfile.calculateLifetimeTotalMiles()/(double)userProfile.getGoal())*100);
        profileProgress.setText(num +"%");
        if (num == 25)
        {
            profileProgressComment.setText("Keep Working Hard!!!!");
        }
        else if(num > 25 && num < 50)
        {
            profileProgressComment.setText("You are doing great!");
        }
        else if (num == 50)
        {
            profileProgressComment.setText("HALFWAY THERE!!!");
        }
        else if (num > 50 && num < 75)
        {
            profileProgressComment.setText("Keep Running!!!");
        }
        else if (num == 75)
        {
            profileProgressComment.setText("YOU ARE ALMOST THERE");
        }
        else if (num > 75 && num < 100)
        {
            profileProgressComment.setText("Amazing Progress. Keep Pushing!");
        }
        else if (num == 100)
        {
            profileProgressComment.setText("YOU DID IT! GREAT JOB!");
        }
        else
        {
            profileProgressComment.setText("");
        }
        profileAgeValueTextView.setText(""+ userProfile.getAge());
        profileHeightValueTextView.setText(""+userProfile.getHeightInInches());
        profileWeightValueTextView.setText(""+userProfile.getWeight());
        profileTotalMilesValueTextView.setText("" + (((int)(userProfile.calculateLifetimeTotalMiles()*100))/100.00) + " Miles");
        profileTotalTimeValueTextView.setText(userProfile.printLifetimeRunningDuration());
        profileAveragePaceValueTextView.setText(userProfile.printLifetimePacePerMile() + " min/mi");
        profileCaloriesBurnedValueTextView.setText("" + userProfile.getLifetimeTotalCalories() + " Cal");

    }

}
