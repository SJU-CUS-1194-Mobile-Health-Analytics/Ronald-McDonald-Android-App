package com.example.jsung721.ronaldmcdonald_prototype1;

import android.text.format.Time;

import java.util.Date;

/**
 * Created by yzhan265 on 3/7/2017.
 */

public class RunningRecord {

    private long time;
    private double longitude;
    private double latitude;

    public RunningRecord(){
    }

    public RunningRecord(long time, double latitude, double longitude) {
        this.time = time;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public long getTime() {
        return time;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String toString() {
        return  "\nTime:"+this.getTime()+
                "\nLatitude:"+this.getLatitude()+
                "\nLongitude:"+this.getLongitude()+"\n";
    }
}
