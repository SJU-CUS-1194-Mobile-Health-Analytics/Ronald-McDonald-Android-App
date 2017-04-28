package com.example.jsung721.ronaldmcdonald_prototype1;

import java.math.BigDecimal;

/**
 * Created by jsung721 on 4/19/2017.
 */

public class UserInfo
{
    public int totalDistanceRun;
    public int totalTimeElapsed;
    public int calories;
    public int age;
    public int minutes;
    public int heightInInches;
    public int weight;
    public int count;
    public double pace;



    public int high;
    public int low;

    public UserInfo() {
        totalDistanceRun = 0 ;
        totalTimeElapsed = 0;
        calories = 0;
        age = 0;
        heightInInches = 0;
        weight = 0;

    }

    public int getCount() {return count;}

    public void setCount(int count) {this.count = count;}



    public double getPace() {
        pace = ((double)totalDistanceRun/(double)getMinutes());
        BigDecimal db = new BigDecimal(pace);
        db = db.setScale(2,BigDecimal.ROUND_UP);
        return pace;
    }

    public void setPace(int pace) {
        this.pace = pace;
    }


    public int getMinutes() {

        minutes = (int) (totalTimeElapsed/(1000*60));
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getTotalDistanceRun() {
        return totalDistanceRun;
    }

    public void setTotalDistanceRun(int totalDistanceRun) {this.totalDistanceRun = totalDistanceRun;}

    public int getTime() {
        return totalTimeElapsed;
    }

    public void setTime(int time) {
        this.totalTimeElapsed = time;
    }

    public int getCalories() {
        minutes = (int) (totalTimeElapsed/(1000*60));
        if (totalDistanceRun==0) { totalDistanceRun=1; }
        int METcalc = minutes/totalDistanceRun;
        int METmeasure = 1;
        if(METcalc <= 4)
            METmeasure = 23;
        else if(METcalc == 5)
            METmeasure = 21;
        else if(METcalc == 6)
            METmeasure = 17;
        else if(METcalc == 7)
            METmeasure = 13;
        else if(METcalc == 8)
            METmeasure = 12;
        else if(METcalc == 9)
            METmeasure = 11;
        else if(METcalc == 10)
            METmeasure = 10;
        else if(METcalc == 11)
            METmeasure = 9;
        else if(METcalc == 12)
            METmeasure = 8;
        else if(METcalc == 13)
            METmeasure = 7;
        else if(METcalc == 14)
            METmeasure = 6;
        else if(METcalc >= 15)
            METmeasure = 5;
        calories = (int)((double)METmeasure * 3.5 * ((double)weight/2.2)/200);
        return calories;}

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getAge() { return age;}

    public void setAge(int age) {this.age = age;}

    public int getHeightInInches() { return heightInInches;}

    public void setHeightInInches(int heightInInches) {this.heightInInches = heightInInches;}

    public int getWeight() {return weight;}

    public void setWeight(int weight) {this.weight = weight;}

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }
}
