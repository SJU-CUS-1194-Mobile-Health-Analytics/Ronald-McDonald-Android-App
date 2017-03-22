package com.example.jsung721.ronaldmcdonald_prototype1;

/**
 * Created by yzhan265 on 3/7/2017.
 */

public class RunningRecords {

    public String time;
    public String date;
    public double longitude;
    public double latitude;

    public RunningRecords(){

    }

    public RunningRecords(String time, String date, double latitude, Double longitude) {
        this.time = time;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
