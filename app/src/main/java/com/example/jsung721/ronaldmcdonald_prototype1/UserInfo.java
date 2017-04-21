package com.example.jsung721.ronaldmcdonald_prototype1;

/**
 * Created by jsung721 on 4/19/2017.
 */

public class UserInfo
{
    public UserInfo() {
        distance = 0 ;
        time = 0;
        calories = 0;

    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int distance;
    public int time;

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int calories;

}
