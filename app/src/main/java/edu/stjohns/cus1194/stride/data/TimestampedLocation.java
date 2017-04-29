package edu.stjohns.cus1194.stride.data;

public class TimestampedLocation {

    // Instance Variables
    private long time;
    private double longitude;
    private double latitude;

    // Default Constructor
    public TimestampedLocation(){
        this.time = 0;
        this.longitude = 0.0;
        this.latitude = 0.0;
    }

    // Constructor With Arguments
    public TimestampedLocation(long time, double latitude, double longitude) {
        this.time = time;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // Getters
    public long getTime() {
        return time;
    }
    public double getLongitude() {
        return longitude;
    }
    public double getLatitude() {
        return latitude;
    }

    // Setters
    public void setTime(long Time) {
        this.time = time;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return  "\nTime:"+this.getTime()+
                "\nLatitude:"+this.getLatitude()+
                "\nLongitude:"+this.getLongitude()+"\n";
    }

}
