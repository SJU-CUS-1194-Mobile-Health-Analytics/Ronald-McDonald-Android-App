package edu.stjohns.cus1194.stride.data;

public class RunSummary {

    // Instance Variables
    private long totalTimeElapsed;
    private long totalDistanceRun;
    private double totalCalories;

    // Default Constructor
    public RunSummary() {
        this.totalTimeElapsed = 0;
        this.totalDistanceRun = 0;
        this.totalCalories = 0;
    }

    // Constructor With Arguments
    public RunSummary(long totalTimeElapsed, long totalDistanceRun, double totalCalories) {
        this.totalTimeElapsed = totalTimeElapsed;
        this.totalDistanceRun = totalDistanceRun;
        this.totalCalories = totalCalories;
    }

    // Getters
    public long getTotalTimeElapsed() {
        return totalTimeElapsed;
    }
    public long getTotalDistanceRun() {
        return totalDistanceRun;
    }
    public double getTotalCalories() {
        return totalCalories;
    }

    // Setters
    public void setTotalTimeElapsed(long totalTimeElapsed) {
        this.totalTimeElapsed = totalTimeElapsed;
    }
    public void setTotalDistanceRun(long totalDistanceRun) {
        this.totalDistanceRun = totalDistanceRun;
    }
    public void setTotalCalories(double totalCalories) {
        this.totalCalories = totalCalories;
    }
}
