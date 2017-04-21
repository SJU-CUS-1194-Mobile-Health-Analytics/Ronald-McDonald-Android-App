package edu.stjohns.cus1194.stride.data;

/**
 * We use the UserProfile Object to store data about a user that is not inherently
 * part of the FirebaseUser Object. The FirebaseUser Object provided by FirebaseAuth
 * stores the user id, name, email, potoUrl, and a few other select fields. Any other
 * data we want to keep for each user needs to be programmatically saved and stored in
 * the FirebaseDatabase, which is what we will use this UserProfile Object for.
 */
public class UserProfile {

    // Instance variables
    private int age; // to keep it simple, we won't calculate age based on birth date
    private int heightInInches;
    private double weight;

    // Default Constructor
    public UserProfile() {
        this.age = 0;
        this.heightInInches = 0;
        this.weight = 0.0;
    }

    // Constructor With Arguments
    public UserProfile(int age, int heightInInches, double weight) {
        this.age = age;
        this.heightInInches = heightInInches;
        this.weight = weight;
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

}
