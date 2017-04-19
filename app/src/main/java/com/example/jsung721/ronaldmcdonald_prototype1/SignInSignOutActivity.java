package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInSignOutActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    // Constants
    private static final String TAG = "SignInSignOutActivity";
    private static final int REQUEST_CODE_SIGN_IN = 9001;

    // Widgets
    private Button logInButton;
    private Button logOutButton;
    private TextView firebaseUserStatus;

    // Firebase Authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;

    // Google Authentication
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions mGoogleSignInOptions;
    private GoogleSignInAccount mGoogleSignInAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_sign_out);
        initFirebaseAuth();
        initUI();
        initGoogleAuth();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void initUI() {
        // log in button
        logInButton = (Button) findViewById(R.id.activity_sign_in_sign_out_log_in_button);
        logInButton.setOnClickListener(this);

        // log out button
        logOutButton = (Button) findViewById(R.id.activity_sign_in_sign_out_log_out_button);
        logOutButton.setOnClickListener(this);

        // user status
        firebaseUserStatus = (TextView) findViewById(R.id.activity_sign_in_sign_out_firebase_user_status);

        updateUI();
    }

    private void updateUI() {
        // Firebase user status
        if (isLoggedInWithFirebase()) {
            String name = mUser.getDisplayName();
            firebaseUserStatus.setText(name + " is logged in.");
        } else {
            firebaseUserStatus.setText("User is not logged in.");
        }
    }

    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (isLoggedInWithFirebase()) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in: " + mUser.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }
                updateUI();
            }
        };
    }

    private void initGoogleAuth() {
        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                .build();
    }

    private void logIn() {
        if (isLoggedInWithFirebase()) {
            // user is already logged in with Firebase
            Toast.makeText(this, "Cannot log in user with Firebase! User is already logged in!", Toast.LENGTH_SHORT).show();
        } else {
            // user is not logged in
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the Google SignIn result
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                mGoogleSignInAccount = result.getSignInAccount();
                Toast.makeText(this, "Google Sign In succeeded! " + mGoogleSignInAccount.getDisplayName() + " is logged in with Google, but not yet with Firebase", Toast.LENGTH_SHORT).show();
                firebaseAuthWithGoogle();
            } else {
                // Google Sign In failed
                Toast.makeText(this, "Google Sign In Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle() {
        Log.d(TAG, "firebaseAuthWithGoogle:" + mGoogleSignInAccount.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(mGoogleSignInAccount.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" +task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignInSignOutActivity.this, "Authentication with Firebase failed.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignInSignOutActivity.this, "Firebase Sign In succeeded! " + mUser.getDisplayName() + " is logged in with Firebase", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void logOut() {
        // log out of Firebase
        if (isLoggedInWithFirebase()) {
            // user is logged in

            // Firebase sign out
            mAuth.signOut();

            // Google sign out
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    updateUI();
                }
            });


        } else {
            // user is not logged in with Google
            Toast.makeText(this, "Cannot log out user out of Firebase! User is not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isLoggedInWithFirebase() {
        return mUser!=null;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occured and Google APIs (including Sign-In) are not available
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.activity_sign_in_sign_out_log_in_button) {
            logIn();
        } else if (i == R.id.activity_sign_in_sign_out_log_out_button) {
            logOut();
        }
    }
}




