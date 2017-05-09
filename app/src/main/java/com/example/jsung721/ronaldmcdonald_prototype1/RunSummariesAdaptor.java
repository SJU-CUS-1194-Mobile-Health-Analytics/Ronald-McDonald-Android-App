package com.example.jsung721.ronaldmcdonald_prototype1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.stjohns.cus1194.stride.data.RunSummary;

public class RunSummariesAdaptor extends RecyclerView.Adapter<RunSummariesAdaptor.MyViewHolder> {

    private ArrayList<RunSummary> runSummaries;
    private Context context;

    public RunSummariesAdaptor(ArrayList<RunSummary> runSummaries) {
        this.runSummaries = runSummaries;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_log_run_summary_view, parent, false);
        context = itemView.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RunSummary runSummary = runSummaries.get(position);
        holder.date.setText(runSummary.printDate());
        holder.distance.setText("Distance: " + runSummary.printDistanceInMiles());
        holder.time.setText("Duration: " + runSummary.printDuration());
    }

    @Override
    public int getItemCount() {
        if (runSummaries != null) {
            return runSummaries.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, distance, time;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.run_summary_date);
            distance = (TextView) view.findViewById(R.id.run_summary_distance);
            time = (TextView) view.findViewById(R.id.run_summary_time);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    long runKey = runSummaries.get(pos).getTimeKey();
                    Intent displayPastRunIntent = new Intent((DailyLogActivity)context,DisplayPastRunActivity.class);
                    displayPastRunIntent.putExtra(DisplayPastRunActivity.FLAG_RUNNING_RECORD_ID, ("" + runKey));
                    context.startActivity(displayPastRunIntent);
                }
            });

        }
    }
}
