package com.example.jsung721.ronaldmcdonald_prototype1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.stjohns.cus1194.stride.data.RunningRecord;

/**
 * Created by yzhan265 on 3/29/2017.
 */

public class TestReceiveDataActivity extends AppCompatActivity {
    protected final String RUNNING_RECORDS = "running records";
    protected String USER_KEY = "default_user";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_receive_data);

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        USER_KEY = mUser.getUid() + "_" + mUser.getEmail();

        final TextView textView = (TextView) findViewById(R.id.text_test_receive_data);
        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(RUNNING_RECORDS).child(USER_KEY);

        // Attach a listener to read the data at our posts reference
        ref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                RunningRecord runningRecord = dataSnapshot.getValue(RunningRecord.class);
                // TODO: do something with running records

                textView.setText(textView.getText()+
                        "\n"+ runningRecord
                        +"\nPrevious Child Key:"+s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
