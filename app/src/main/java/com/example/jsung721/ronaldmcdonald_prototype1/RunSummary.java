package com.example.jsung721.ronaldmcdonald_prototype1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yzhan265 on 4/7/2017.
 */

public class RunSummary implements Parcelable{
    private long totalTimeElapsed;
    private long totalDistanceRun;

    public RunSummary(long totalTimeElapsed, long totalDistanceRun) {
        this.totalTimeElapsed = totalTimeElapsed;
        this.totalDistanceRun = totalDistanceRun;
    }

    protected RunSummary(Parcel in) {
        totalTimeElapsed = in.readLong();
        totalDistanceRun = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(totalTimeElapsed);
        dest.writeLong(totalDistanceRun);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RunSummary> CREATOR = new Creator<RunSummary>() {
        @Override
        public RunSummary createFromParcel(Parcel in) {
            return new RunSummary(in);
        }

        @Override
        public RunSummary[] newArray(int size) {
            return new RunSummary[size];
        }
    };

    public long getTotalTimeElapsed() {
        return totalTimeElapsed;
    }

    public long getTotalDistanceRun() {
        return totalDistanceRun;
    }


}
