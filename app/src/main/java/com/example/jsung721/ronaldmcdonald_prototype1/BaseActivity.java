package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {

    // Firebase instance variables
    protected FirebaseAuth mFirebaseAuth;
    protected FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFirebaseUser();
        // Toast.makeText(getApplicationContext(), "Logged in as: " + mFirebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
    }

    private void initFirebaseUser() {
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInSignOutActivity.class));
            finish();
            return;
        }
    }
}
