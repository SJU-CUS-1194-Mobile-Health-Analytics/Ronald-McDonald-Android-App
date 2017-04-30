package edu.stjohns.cus1194.stride.utils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yzhan265 on 4/28/2017.
 */

public class TimeConverter {
    private Calendar calendar;

    // Constructor
    public TimeConverter(long millisecondsSinceEpoch){
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisecondsSinceEpoch);
    }

    public long getMillesecondsSinceEpoch(){
        return calendar.getTimeInMillis();
    }

    public Calendar getCalendar(){
        return calendar;
    }

    public int getSecond(){
        return calendar.SECOND;
    }

    public int getMinute(){
        return calendar.MINUTE;
    }

    public int getHour(){
        return calendar.HOUR;
    }

    public static int getSecondFromMillisecond(long milliseconds){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(milliseconds);
        return c.SECOND;
    }

    public static int getMinuteFromMillisecond(long milliseconds){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(milliseconds);
        return c.MINUTE;
    }

    public static int getHourFromMillisecond(long milliseconds){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(milliseconds);
        return c.HOUR;
    }

    public String getFormattedTimeHHMMSS(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(calendar);
    }

    public static String getFormattedTimeHHMMSS(long milliseconds){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(milliseconds);
        return format.format(c);
    }

    public String getFormattedTimeMMSS(){
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        return format.format(calendar);
    }

    public static String getFormattedTimeMMSS(long milliseconds){
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(milliseconds);
        return format.format(c);
    }

    public String getFormattedTimeDDMM(){
        SimpleDateFormat format = new SimpleDateFormat("M/d");
        return format.format(calendar);
    }

    public static String getFormattedDateDDMM(long milliseconds){
        SimpleDateFormat format = new SimpleDateFormat("M/d");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(milliseconds);
        return format.format(c);
    }
}

