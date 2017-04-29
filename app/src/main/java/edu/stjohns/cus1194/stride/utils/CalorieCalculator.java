package edu.stjohns.cus1194.stride.utils;

import edu.stjohns.cus1194.stride.data.RunSummary;
import edu.stjohns.cus1194.stride.data.UserProfile;

public class CalorieCalculator {

    public static double calculateCalories(UserProfile user, RunSummary run) {
        double calories = 0.0;

        double minutes = run.getTotalTimeElapsed()/(1000*60);
        double METcalc = minutes/run.getTotalDistanceRun();
        int METmeasure;
        if(METcalc <= 4)
            METmeasure = 23;
        else if(METcalc> 4 && METcalc<= 5)
            METmeasure = 21;
        else if(METcalc> 5 &&METcalc <= 6)
            METmeasure = 17;
        else if(METcalc> 6 &&METcalc <= 7)
            METmeasure = 13;
        else if(METcalc> 7 &&METcalc <= 8)
            METmeasure = 12;
        else if(METcalc> 8 &&METcalc <= 9)
            METmeasure = 11;
        else if(METcalc> 9 &&METcalc <= 10)
            METmeasure = 10;
        else if(METcalc > 10 &&METcalc <= 11)
            METmeasure = 9;
        else if(METcalc> 11 &&METcalc <= 12)
            METmeasure = 8;
        else if(METcalc> 12 &&METcalc <= 13)
            METmeasure = 7;
        else if(METcalc> 13 &&METcalc <= 14)
            METmeasure = 6;
        else // METcalc > 14
            METmeasure = 5;
        calories = ((double)METmeasure * 3.5 * ((double)user.getWeight()/2.2)/200);

        return calories;
    }

}
