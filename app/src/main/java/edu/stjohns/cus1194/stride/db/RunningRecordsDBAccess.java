package edu.stjohns.cus1194.stride.db;

import android.provider.ContactsContract;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.stjohns.cus1194.stride.data.RunningRecord;

public class RunningRecordsDBAccess {

    // Database User Profiles Reference Constant
    private static final String RUNNING_RECORDS = "RUNNING RECORDS";
    private static final String runningPath = "runningPath";

    /**
     * Adds a RunningRecord to the Firebase DB
     * @param runningRecord - the RunningRecord to be added
     * @return the id assigned to the newly stored RunningRecord
     */
    public static void addRunningRecord(String runId, RunningRecord runningRecord) {
        DatabaseReference runningRecordsRef = getRunningRecordsRef();
        runningRecordsRef.child(runId).setValue(runningRecord);
    }

    /**
     * Note: in order to actually get the RunningRecord Objects from the returned DatabaseReference,
     * you must set a listener to the DatabaseReference and handle the RunningRecord data when it
     * is received.
     *
     * Here is an example of what the code for this might look like:
     *
     *      DatabaseReference runningRecordsRef = getRunningRecordsRef();
     *      runningRecordsRef.addValueEventListener(new ValueEventListener() {
     *          @Override
     *          public void onDataChange(DataSnapshot dataSnapshot) {
     *              RunningRecord runningRecord = dataSnapshot.getValue(RunningRecord.class);
     *              // Do something with runningRecord
     *          }
     *
     *          @Override
     *          public void onCancelled(DatabaseError databaseError) {
     *              Log.d(TAG, "Error reading RunningRecord data from the Firebase Database");
     *          }
     *      });
     *
     * @return a DatabaseReference to the running records
     */
    public static DatabaseReference getRunningRecordsRef() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference runningRecordsRef = db.getReference(RUNNING_RECORDS);
        return runningRecordsRef;
    }

    public static DatabaseReference getRunningRefordRefById(String id){
        DatabaseReference runningRecordRecordRef = getRunningRecordsRef().child(id);
        return runningRecordRecordRef;
    }

    public static DatabaseReference getRunningPathRefById(String id){
        DatabaseReference runningRecordRecordRef = getRunningRecordsRef().child(id).child(runningPath);
        return runningRecordRecordRef;
    }

}
