package com.example.knowwealth;

import java.util.ArrayList;
import java.util.List;

public class User {
    // User's first name to be used in DashBoard
    private static String firstName;
    private static String fullName;
    private static String email;

    // Data fields
    static List<UtilDate> utilities;
    static List<UtilDate> creditCards;
    static List<UtilDate> subscriptions;
    static List<UtilDate> expenses;
    // Global variable to know
    static String currentActivity;
    static Boolean processingCompleted = false;
    static int notificationDays = 0;

    // Constructors
    public User(){
        utilities = new ArrayList<>();
        creditCards = new ArrayList<>();
        subscriptions = new ArrayList<>();
        expenses = new ArrayList<>();
    }
    public User(String fullName, String email){
        this.fullName = fullName;
        String[] getName = fullName.split(" ");
        this.firstName = getName[0];
        this.email = email;
        utilities = new ArrayList<>();
        creditCards = new ArrayList<>();
        subscriptions = new ArrayList<>();
        expenses = new ArrayList<>();
    }

    // firstName and fullName setters
    public void setFullName(String fullName){
        this.fullName = fullName;
        String[] getName = fullName.split(" ");
        this.firstName = getName[0];
    }

    // firstName and fullName getters
    public static String getFullName() { return fullName; }
    public static String getFirstName() { return firstName; }

    // email getter and setter
    public String getEmail() { return email; }
    public void setEmail( String email){ this.email = email; }

    // Global variables getters
    public Boolean getProcessingCompleted(){return processingCompleted;}
    public String getCurrentActivity(){ return currentActivity;}

    // Global variables setters
    public void setProcessingCompleted( Boolean value){
        processingCompleted = value;
    }
    public void setCurrentActivity( String value){
        currentActivity = value;
    }

    // Utilities
    public static class UtilDate{
        private String dueDay;
        private String name;

        public UtilDate(){ }
        public UtilDate(String dueDay, String name){
            this.dueDay = dueDay;
            this.name = name;
        }
        public String getDueDay(){return dueDay;}
        public String getName(){return name;}
    }

    // method to remove deleted entries
    public static void deleteFromList(String removeDueDay, String removeName, List<UtilDate> array){
        for (int i = 0; i < array.size(); i++){
            UtilDate data = array.get(i);
            if (data.name.equals(removeName) && data.dueDay.equals(removeDueDay)){
                array.remove(i);
            }
        }
    }

    // Expenses
    public class Expense {
        // Fields
        private String name;
        private float amount = 0;
        private float budget;

        //Constructors
        public Expense(){}
        public Expense(String name){
            this.name = name;
        }

        // Expense amount getter
        public float getAmount() { return amount; }

        // Method to add to the expense amount
        public void AddExpense(float amount){ this.amount += amount; }

        // Budget getter and setter
        public float getBudget() { return budget; }
        public void setBudget(float budget) { this.budget = budget; }
    }
}
