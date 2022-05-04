package com.example.knowwealth;

import java.util.ArrayList;
import java.util.List;

public class User {
    // User's first name to be used in DashBoard
    private String firstName;
    private String fullName;

    // Data fields
    List<UtilDate> utilities;
    List<CreditDate> creditCards;
    List<SubscriptDate> Subscriptions;
    List<Expence> expenses;

    // Constructors
    public User(){
        utilities = new ArrayList<>();
        creditCards = new ArrayList<>();
        Subscriptions = new ArrayList<>();
        expenses = new ArrayList<>();
    }
    public User(String fullName, String firstName){
        this.firstName = firstName;
        this.fullName = fullName;
        utilities = new ArrayList<>();
        creditCards = new ArrayList<>();
        Subscriptions = new ArrayList<>();
        expenses = new ArrayList<>();
    }

    // Utilities
    public class UtilDate{
        private int dueDay;
        private String name;

        public UtilDate(){ }
        public UtilDate(String dueDay, String name){
            this.dueDay = Integer.parseInt(dueDay);
            this.name = name;
        }
    }

    // The rest of the processing screens data collected
    public class CreditDate extends UtilDate{}
    public class SubscriptDate extends UtilDate{}

    // Expenses
    public class Expence {
        // Fields
        private String name;
        private float amount = 0;
        private float budget;

        //Constructors
        public Expence(){}
        public Expence(String name){
            this.name = name;
        }

        // Expense amount getter
        public float getAmount() { return amount; }

        // Method to add to the expense amount
        public void AddExpence(float amount){ this.amount += amount; }

        // Budget getter and setter
        public float getBudget() { return budget; }
        public void setBudget(float budget) { this.budget = budget; }
    }
}
