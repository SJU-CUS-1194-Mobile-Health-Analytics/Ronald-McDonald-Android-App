package com.example.jsung721.ronaldmcdonald_prototype1;

/**
 * Created by yzhan265 on 4/7/2017.
 */

public class RunSummary {
    private long totalTimeElapsed;
    private long totalDistanceRun;

    public RunSummary(long totalTimeElapsed, long totalDistanceRun) {
        this.totalTimeElapsed = totalTimeElapsed;
        this.totalDistanceRun = totalDistanceRun;
    }

    public long getTotalTimeElapsed() {
        return totalTimeElapsed;
    }

    public long getTotalDistanceRun() {
        return totalDistanceRun;
    }
}
