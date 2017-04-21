package edu.stjohns.cus1194.stride.db;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.stjohns.cus1194.stride.data.UserProfile;

public class UserProfileDBAccess {

    // Database User Profiles Reference Constant
    private static final String USER_PROFILES = "USER PROFILES";

    /**
     * Sets a UserProfile Object for a user with the given id to the Firebase DB
     * @param id - the unique user id for the given user
     * @param userProfile - the UserProfile Object for the given user
     */
    public static void setUserProfileById(String id, UserProfile userProfile) {
        DatabaseReference userProfileRef = getUserProfileRefById(id);
        userProfileRef.setValue(userProfile);
    }

    /**
     * Note: in order to actually get a UserProfile object from the returned DatabaseReference,
     * you must set a listener to the DatabaseReference and handle the UserProfile data when it
     * is received.
     *
     * Here is an example of what the code for this might look like:
     *
     *      DatabaseReference userProfileRef = getUserProfileRefById(id);
     *      userProfileRef.addValueEventListener(new ValueEventListener() {
     *          @Override
     *          public void onDataChange(DataSnapshot dataSnapshot) {
     *              UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
     *              // Do something with userProfile
     *          }
     *
     *          @Override
     *          public void onCancelled(DatabaseError databaseError) {
     *              Log.d(TAG, "Error reading UserProfile data from the Firebase Database");
     *          }
     *      });
     *
     * @param id - the unique id for the given user
     * @return a DatabaseReference to the user profile data for the user with the given id
     */
    public static DatabaseReference getUserProfileRefById(String id) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference userProfilesRef = db.getReference(USER_PROFILES);
        DatabaseReference userProfileRef = userProfilesRef.child(id);
        return userProfileRef;
    }

}
