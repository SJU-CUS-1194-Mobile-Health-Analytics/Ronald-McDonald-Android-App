package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import edu.stjohns.cus1194.stride.data.UserProfile;
import edu.stjohns.cus1194.stride.db.UserProfileDBAccess;

public class EditProfileActivity extends BaseActivity {

    // UI elements
    EditText ageEditText;
    EditText heightEditText;
    EditText weightEditText;
    Button saveChangesButton;

    // UserProfile Object for logged in user
    UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initUI();
        initDB();
    }

    private void initUI() {
        TextView profileNameTextView = (TextView) findViewById(R.id.edit_profile_username_textview);
        profileNameTextView.setText(mFirebaseUser.getDisplayName());

        ageEditText = (EditText) findViewById(R.id.edit_profile_age);
        heightEditText = (EditText) findViewById(R.id.edit_profile_height);
        weightEditText = (EditText) findViewById(R.id.edit_profile_weight);

        saveChangesButton = (Button) findViewById(R.id.edit_profile_save_changes);
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userProfile!=null) {
                    int age = Integer.parseInt(ageEditText.getText().toString());
                    int heightInInches = Integer.parseInt(heightEditText.getText().toString());
                    double weight = Double.parseDouble(weightEditText.getText().toString());

                    userProfile.setAge(age);
                    userProfile.setHeightInInches(heightInInches);
                    userProfile.setWeight(weight);

                    UserProfileDBAccess.setUserProfileById(mFirebaseUser.getUid(), userProfile);

                    Intent goBackToSettingsIntent = new Intent(EditProfileActivity.this, SettingsActivity.class);
                    startActivity(goBackToSettingsIntent);
                }
            }
        });
    }

    private void initDB() {
        final DatabaseReference userProfileRef = UserProfileDBAccess.getUserProfileRefById(mFirebaseUser.getUid());
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
        ageEditText.setText("" + userProfile.getAge());
        heightEditText.setText("" + userProfile.getHeightInInches());
        weightEditText.setText("" + userProfile.getWeight());
    }

}
