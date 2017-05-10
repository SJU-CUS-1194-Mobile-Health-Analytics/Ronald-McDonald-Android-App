package edu.stjohns.cus1194.stride.data;

/**
 * We use the UserProfile Object to store data about a user that is not inherently
 * part of the FirebaseUser Object. The FirebaseUser Object provided by FirebaseAuth
 * stores the user id, name, email, potoUrl, and a few other select fields. Any other
 * data we want to keep for each user needs to be programmatically saved and stored in
 * the FirebaseDatabase, which is what we will use this UserProfile Object for.
 */
public class UserProfile {

    // Constants
    private static final double METERS_PER_MILE = 1609.34;
    private static final double METERS_PER_KILOMETER = 1000;
    private static final double MILLISECONDS_PER_SECOND = 1000;
    private static final double MILLISECONDS_PER_MINUTE = 1000*60;
    private static final double MILLISECONDS_PER_HOUR = 1000*60*60;

    // Instance variables for UserProfile info not kept by FirebaseAuth
    private int age; // to keep it simple, we won't calculate age based on birth date
    private int heightInInches;
    private double weight;
    private int goal;
    // Instance variables for lifetime user stats
    private long lifetimeTotalDistanceInMeters;
    private long lifetimeTotalTimeInMillis;
    private double lifetimeTotalCalories;
    private long lifetimeLongestRunByDistance;
    private long lifetimeLongestRunByTime;
    private double lifetimeHighestCaloriesBurned;


    // Default Constructor
    public UserProfile() {
        this.age = 0;
        this.heightInInches = 0;
        this.weight = 0.0;
        this.goal = 100;
        this.lifetimeTotalDistanceInMeters = 0;
        this.lifetimeTotalTimeInMillis = 0;
        this.lifetimeTotalCalories = 0.0;
        this.lifetimeLongestRunByDistance = 0;
        this.lifetimeLongestRunByTime = 0;
        this.lifetimeHighestCaloriesBurned = 0.0;
    }

    // Constructor With Arguments
    public UserProfile(int age, int heightInInches, double weight, int goal) {
        this.age = age;
        this.heightInInches = heightInInches;
        this.weight = weight;
        this.goal = goal;
        this.lifetimeTotalDistanceInMeters = 0;
        this.lifetimeTotalTimeInMillis = 0;
        this.lifetimeTotalCalories = 0.0;
        this.lifetimeLongestRunByDistance = 0;
        this.lifetimeLongestRunByTime = 0;
        this.lifetimeHighestCaloriesBurned = 0.0;
    }

    // Method to update a user's stats based on a newly completed run
    public void updateUserStats(RunSummary runSummary) {
        long runDistance = runSummary.getTotalDistanceRunInMeters();
        long runTime = runSummary.getTotalTimeElapsedInMillis();
        lifetimeTotalDistanceInMeters += runDistance;
        lifetimeTotalTimeInMillis += runTime;
        lifetimeTotalCalories += runSummary.getTotalCalories();
        if (runDistance > lifetimeLongestRunByDistance) {
            lifetimeLongestRunByDistance = runDistance;
        }
        if (runTime > lifetimeLongestRunByTime) {
            lifetimeLongestRunByTime = runTime;
        }
        if (runSummary.getTotalCalories() > lifetimeHighestCaloriesBurned)
        {
            lifetimeHighestCaloriesBurned = runSummary.getTotalCalories();
        }
    }

    // Getters
    public int getAge() {
        return age;
    }
    public int getHeightInInches() {
        return heightInInches;
    }
    public double getWeight() {
        return weight;
    }
    public int getGoal(){return goal;}

    public long getLifetimeTotalDistanceInMeters() {
        return lifetimeTotalDistanceInMeters;
    }
    public long getLifetimeTotalTimeInMillis() {
        return lifetimeTotalTimeInMillis;
    }
    public double getLifetimeTotalCalories() {
        return lifetimeTotalCalories;
    }
    public long getLifetimeLongestRunByDistance() {
        return lifetimeLongestRunByDistance;
    }
    public long getLifetimeLongestRunByTime() {
        return lifetimeLongestRunByTime;
    }
    public double getLifetimeHighestCaloriesBurned() {return lifetimeHighestCaloriesBurned;}

    // Setters
    public void setAge(int age) {
        this.age = age;
    }
    public void setHeightInInches(int heightInInches) {
        this.heightInInches = heightInInches;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public void setGoal(int goal){this.goal = goal;}

    public void setLifetimeTotalDistanceInMeters(long lifetimeTotalDistanceInMeters) {
        this.lifetimeTotalDistanceInMeters = lifetimeTotalDistanceInMeters;
    }
    public void setLifetimeTotalTimeInMillis(long lifetimeTotalTimeInMillis) {
        this.lifetimeTotalTimeInMillis = lifetimeTotalTimeInMillis;
    }
    public void setLifetimeTotalCalories(double lifetimeTotalCalories) {
        this.lifetimeTotalCalories = lifetimeTotalCalories;
    }
    public void setLifetimeLongestRunByDistance(long lifetimeLongestRunByDistance) {
        this.lifetimeLongestRunByDistance = lifetimeLongestRunByDistance;
    }
    public void setLifetimeLongestRunByTime(long lifetimeLongestRunByTime) {
        this.lifetimeLongestRunByTime = lifetimeLongestRunByTime;
    }
    public void setLifetimeHighestCaloriesBurned(double lifetimeHighestCaloriesBurned) {
        this.lifetimeHighestCaloriesBurned = lifetimeHighestCaloriesBurned;
    }
    // Helper Methods

    /**
     * @return the lifetime total number of miles run by this user
     */
    public double calculateLifetimeTotalMiles() {
        return lifetimeTotalDistanceInMeters/METERS_PER_MILE;
    }

    /**
     * @return the lifetime total number of kilometers run by this user
     */
    public double calculateLifetimeTotalKilometers() {
        return lifetimeTotalDistanceInMeters/METERS_PER_KILOMETER;
    }

    /**
     * @return the lifetime number of seconds run by this user
     */
    public double calculateLifetimeTotalSeconds() {
        return lifetimeTotalTimeInMillis/MILLISECONDS_PER_SECOND;
    }

    /**
     * @return the lifetime number of minutes run by this user
     */
    public double calculateLifetimeTotalMinutes() {
        return lifetimeTotalTimeInMillis/MILLISECONDS_PER_MINUTE;
    }

    /**
     * @return the lifetime number of hours run by this user
     */
    public double calculateLifetimeTotalHours() {
        return lifetimeTotalTimeInMillis/MILLISECONDS_PER_HOUR;
    }

    /**
     * @return the lifetime average pace run by this user in minutes per mile
     */
    public double calculateLifetimeMinutesPerMile() {
        return calculateLifetimeTotalMinutes()/calculateLifetimeTotalMiles();
    }

    /**
     * @return the lifetime average pace run by this user in minutes per kilometer
     */
    public double calculateLifetimeMinutesPerKilometer() {
        return calculateLifetimeTotalMinutes()/calculateLifetimeTotalKilometers();
    }

    /**
     * Examples:
     *    1 hour 15 minutes and 3 seconds --> 1:15:03
     *    0 hours 6 minutes and 0 seconds --> 6:00
     * @return String representation of the lifetime duration run by this user
     */
    public String printLifetimeRunningDuration() {
        int hoursOnly = ((int) calculateLifetimeTotalHours());
        int minutesOnly = ((int) calculateLifetimeTotalMinutes()) - (60*hoursOnly);
        int secondsOnly = ((int) calculateLifetimeTotalSeconds()) - (60*60*hoursOnly) - (60*minutesOnly);

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
    public String printLifetimePacePerMile() {
        return printPace(calculateLifetimeMinutesPerMile());
    }

    /**
     * @return String representation of the pace per kilometer of this run
     */
    public String printLifetimePacePerKilometer() {
        return printPace(calculateLifetimeMinutesPerKilometer());
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


}
