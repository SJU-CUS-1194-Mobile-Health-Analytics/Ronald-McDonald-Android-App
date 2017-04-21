package edu.stjohns.cus1194.stride.data;

public class RunSummary {

    // Instance Variables
    private long totalTimeElapsed;
    private long totalDistanceRun;

    // Default Constructor
    public RunSummary() {
        this.totalTimeElapsed = 0;
        this.totalDistanceRun = 0;
    }

    // Constructor With Arguments
    public RunSummary(long totalTimeElapsed, long totalDistanceRun) {
        this.totalTimeElapsed = totalTimeElapsed;
        this.totalDistanceRun = totalDistanceRun;
    }

    // Getters
    public long getTotalTimeElapsed() {
        return totalTimeElapsed;
    }

    public long getTotalDistanceRun() {
        return totalDistanceRun;
    }

    // Setters
    public void setTotalTimeElapsed(long totalTimeElapsed) {
        this.totalTimeElapsed = totalTimeElapsed;
    }

    public void setTotalDistanceRun(long totalDistanceRun) {
        this.totalDistanceRun = totalDistanceRun;
    }

}
