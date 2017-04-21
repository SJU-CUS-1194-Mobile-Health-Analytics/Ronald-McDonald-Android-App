package edu.stjohns.cus1194.stride.db;

import edu.stjohns.cus1194.stride.data.RunSummary;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RunSummariesByUserDBAccess {

    // Database User Profiles Reference Constant
    private static final String RUN_SUMMARIES_BY_USER = "RUN SUMMARIES BY USER";

    /**
     * Adds a RunSummary to the Firebase DB for the given user
     * @param userId - the unique user id for the given user
     * @param runId - the unique run id for the given run
     * @param runSummary - the RunSummary we want to add for the given user
     */
    public static void addRunForUser(String userId, String runId, RunSummary runSummary) {
        DatabaseReference runsByUserRef = getRunsByUserRef(userId);
        runsByUserRef.child(runId).setValue(runSummary);
    }

    /**
     * Note: in order to actually get the RunSummary Objects from the returned DatabaseReference,
     * you must set a listener to the DatabaseReference and handle the RunSummary data when it
     * is received.
     *
     * Here is an example of what the code for this might look like:
     *
     *      DatabaseReference runsByUserRef = getRunsByUserRef(id);
     *      runsByUserRef.addValueEventListener(new ValueEventListener() {
     *          @Override
     *          public void onDataChange(DataSnapshot dataSnapshot) {
     *              RunSummary runSummary = dataSnapshot.getValue(RunSummary.class);
     *              // Do something with userProfile
     *          }
     *
     *          @Override
     *          public void onCancelled(DatabaseError databaseError) {
     *              Log.d(TAG, "Error reading RunSummary data from the Firebase Database");
     *          }
     *      });
     *
     * @param id - the unique id for the given user
     * @return a DatabaseReference to the user profile data for the user with the given id
     */
    public static DatabaseReference getRunsByUserRef(String id) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference runsByUserRef = db.getReference(RUN_SUMMARIES_BY_USER).child(id);
        return runsByUserRef;
    }

}
