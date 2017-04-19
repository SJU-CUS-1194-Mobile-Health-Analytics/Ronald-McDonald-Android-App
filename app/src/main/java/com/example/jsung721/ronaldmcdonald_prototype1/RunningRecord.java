package com.example.jsung721.ronaldmcdonald_prototype1;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

import java.util.Date;

/**
 * Created by yzhan265 on 3/7/2017.
 */

public class RunningRecord implements Parcelable{

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


    protected RunningRecord(Parcel in) {
        time = in.readLong();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RunningRecord> CREATOR = new Creator<RunningRecord>() {
        @Override
        public RunningRecord createFromParcel(Parcel in) {
            return new RunningRecord(in);
        }

        @Override
        public RunningRecord[] newArray(int size) {
            return new RunningRecord[size];
        }
    };


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
