package edu.stjohns.cus1194.stride.data;

import java.util.ArrayList;

public class RunningRecord {

    // Instance Variables
    private ArrayList<TimestampedLocation> runningPath;

    // Default Constructor
    public RunningRecord(){
        runningPath = new ArrayList<>();
    }

    public void addTimestampedLocation(TimestampedLocation timestampedLocation) {
        runningPath.add(timestampedLocation);
    }

    // Getters
    public ArrayList<TimestampedLocation> getRunningPath() {
        return runningPath;
    }

    // Setters
    public void setRunningPath(ArrayList<TimestampedLocation> runningPath) {
        this.runningPath = runningPath;
    }

}
