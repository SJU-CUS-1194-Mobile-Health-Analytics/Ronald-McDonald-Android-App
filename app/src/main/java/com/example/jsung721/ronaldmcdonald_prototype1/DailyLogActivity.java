package com.example.jsung721.ronaldmcdonald_prototype1;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.stjohns.cus1194.stride.data.RunSummary;
import edu.stjohns.cus1194.stride.db.RunSummariesByUserDBAccess;

public class DailyLogActivity extends BaseActivity {

    private ArrayList<RunSummary> runSummaries;
    private RecyclerView recyclerView;
    private RunSummariesAdaptor runSummariesAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_log);

        runSummaries = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.daily_log_recycler_view);


        runSummariesAdaptor = new RunSummariesAdaptor(runSummaries);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLayoutManager.getOrientation());

        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(runSummariesAdaptor);

        DatabaseReference runSummariesRef = RunSummariesByUserDBAccess.getRunsByUserRef(mFirebaseUser.getUid());
        runSummariesRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot runSummariesSnapshot) {
                for (DataSnapshot runSummarySnapshot : runSummariesSnapshot.getChildren()) {
                    RunSummary currentRun = (RunSummary) runSummarySnapshot.getValue(RunSummary.class);
                    long timeKey = Long.parseLong(runSummarySnapshot.getKey());
                    currentRun.setTimeKey(timeKey);
                    RunSummariesByUserDBAccess.addRunForUser(mFirebaseUser.getUid(),""+timeKey, currentRun);
                    runSummaries.add(0,currentRun);
                }
                runSummariesAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error obtaining run summaries from DB");
            }
        });
    }
}
