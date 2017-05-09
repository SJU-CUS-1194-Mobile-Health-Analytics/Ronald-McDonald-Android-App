package edu.stjohns.cus1194.stride.data;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class RunSummary {

    // Constants
    private static final double METERS_PER_MILE = 1609.34;
    private static final double METERS_PER_KILOMETER = 1000;
    private static final double MILLISECONDS_PER_SECOND = 1000;
    private static final double MILLISECONDS_PER_MINUTE = 1000*60;
    private static final double MILLISECONDS_PER_HOUR = 1000*60*60;

    // Instance Variables
    private long timeKey;
    private long totalTimeElapsedInMillis;
    private long totalDistanceRunInMeters;
    private double totalCalories;

    // Default Constructor
    public RunSummary() {
        this.timeKey = 0;
        this.totalTimeElapsedInMillis = 0;
        this.totalDistanceRunInMeters = 0;
        this.totalCalories = 0;
    }

    // Constructor With Arguments
    public RunSummary(long timeKey, long totalTimeElapsedInMillis, long totalDistanceRunInMeters, double userWeight) {
        this.timeKey = timeKey;
        this.totalTimeElapsedInMillis = totalTimeElapsedInMillis;
        this.totalDistanceRunInMeters = totalDistanceRunInMeters;
        calculateCalories(userWeight);
    }

    // Getters
    public long getTimeKey() {
        return timeKey;
    }
    public long getTotalTimeElapsedInMillis() {
        return totalTimeElapsedInMillis;
    }
    public long getTotalDistanceRunInMeters() {
        return totalDistanceRunInMeters;
    }
    public double getTotalCalories() {
        return totalCalories;
    }

    // Setters
    public void setTimeKey(long timeKey) {
        this.timeKey = timeKey;
    }
    public void setTotalTimeElapsedInMillis(long totalTimeElapsedInMillis) {
        this.totalTimeElapsedInMillis = totalTimeElapsedInMillis;
    }
    public void setTotalDistanceRunInMeters(long totalDistanceRunInMeters) {
        this.totalDistanceRunInMeters = totalDistanceRunInMeters;
    }
    public void setTotalCalories(double totalCalories) {
        this.totalCalories = totalCalories;
    }

    // Helper methods

    /**
     * @return the distance of this run in miles
     */
    public double calculateMiles() {
        return totalDistanceRunInMeters/METERS_PER_MILE;
    }

    /**
     * @return the distance of this run in kilometers
     */
    public double calculateKilometers() {
        return totalDistanceRunInMeters/METERS_PER_KILOMETER;
    }

    /**
     * @return the duration of this run in seconds
     */
    public double calculateSeconds() {
        return totalTimeElapsedInMillis/MILLISECONDS_PER_SECOND;
    }

    /**
     * @return the duration of this run in minutes
     */
    public double calculateMinutes() {
        return totalTimeElapsedInMillis/MILLISECONDS_PER_MINUTE;
    }

    /**
     * @return the duration of this run in hours
     */
    public double calculateHours() {
        return totalTimeElapsedInMillis/MILLISECONDS_PER_HOUR;
    }

    /**
     * @return the average pace of this run in minutes per mile
     */
    public double calculateMinutesPerMile() {
        return calculateMinutes()/calculateMiles();
    }

    /**
     * @return the average pace of this run in minutes per kilometer
     */
    public double calculateMinutesPerKilometer() {
        return calculateMinutes()/calculateKilometers();
    }

    /**
     * This method calculates and sets the total calories burned for this run.
     * Since calories burned cannot be determined without knowing the runner's
     * weight, getTotalCalories() will return 0 if the default constructor was
     * used and this method has not yet been called.
     *
     * @param userWeight - the weight of the user that created this run
     * @return the calories burned during this run
     */
    public double calculateCalories(double userWeight) {

        double METcalc = calculateMinutes()/getTotalDistanceRunInMeters();

        int METmeasure;
        if(METcalc <= 4)
            METmeasure = 23;
        else if(METcalc <= 5)
            METmeasure = 21;
        else if(METcalc <= 6)
            METmeasure = 17;
        else if(METcalc <= 7)
            METmeasure = 13;
        else if(METcalc <= 8)
            METmeasure = 12;
        else if(METcalc <= 9)
            METmeasure = 11;
        else if(METcalc <= 10)
            METmeasure = 10;
        else if(METcalc <= 11)
            METmeasure = 9;
        else if(METcalc <= 12)
            METmeasure = 8;
        else if(METcalc <= 13)
            METmeasure = 7;
        else if(METcalc <= 14)
            METmeasure = 6;
        else // METcalc > 14
            METmeasure = 5;

        totalCalories = (int)((double)METmeasure * 3.5 * (userWeight/2.2)/200);

        return totalCalories;
    }

    /**
     * Examples:
     *    1 hour 15 minutes and 3 seconds --> 1:15:03
     *    0 hours 6 minutes and 0 seconds --> 6:00
     * @return String representation of the duration of this run
     */
    public String printDuration() {
        int hoursOnly = ((int) calculateHours());
        int minutesOnly = ((int) calculateMinutes()) - (60*hoursOnly);
        int secondsOnly = ((int) calculateSeconds()) - (60*60*hoursOnly) - (60*minutesOnly);

        String hoursString = "";
        if (hoursOnly !=0 ) {
            hoursString += hoursOnly + ":";
        }

        String minutesString = "" + minutesOnly + ":";
        if ((hoursOnly != 0) && (minutesString.length() == 2)) {
            minutesString = "0" + minutesString;
        }

        String secondsString = "" + secondsOnly;
        if (secondsString.length() == 1) {
            secondsString = "0" + secondsString;
        }

        return (hoursString + minutesString + secondsString);
    }

    /**
     * @return String representation of the pace per mile of this run
     */
    public String printPacePerMile() {
        return printPace(calculateMinutesPerMile()) + " per mile";
    }

    /**
     * @return String representation of the pace per kilometer of this run
     */
    public String printPacePerKilometer() {
        return printPace(calculateMinutesPerKilometer());
    }

    private String printPace(double paceInMinutes) {
        int minutesOnly = (int) paceInMinutes;
        int secondsOnly = (int) ((paceInMinutes - minutesOnly)*60);

        String minutesString = "" + minutesOnly + ":";

        String secondsString = "" + secondsOnly;
        while (secondsString.length() == 1) {
            secondsString = "0" + secondsString;
        }

        return (minutesString + secondsString);
    }

    /**
     * @return String representation of the distance in miles
     */
    public String printDistanceInMiles() {
        double miles = calculateMiles();
        String milesString = String.format("%.2f", miles);
        return milesString + " miles";
    }

    /**
     * @return String representation of the date
     */
    public String printDate() {
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.setTimeInMillis(timeKey);
        int mYear = myCalendar.get(Calendar.YEAR);
        String yearString = "" + mYear;
        yearString = yearString.substring(2);
        int mMonth = myCalendar.get(Calendar.MONTH)+1;
        int mDay = myCalendar.get(Calendar.DAY_OF_MONTH);
        return "" + mMonth + "/" + mDay + "/" + yearString;
    }


}
