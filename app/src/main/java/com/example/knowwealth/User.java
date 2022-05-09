package com.example.knowwealth;

import java.util.ArrayList;
import java.util.List;

public class User {
    // User's first name to be used in DashBoard
    private String firstName;
    private String fullName;
    private String email;

    // Data fields
    List<UtilDate> utilities;
    List<CreditDate> creditCards;
    List<SubscriptDate> Subscriptions;
    List<Expense> expenses;

    // Constructors
    public User(String fullName, String email){
        this.fullName = fullName;
        String[] getName = fullName.split(" ");
        this.firstName = getName[0];
        this.email = email;
        utilities = new ArrayList<>();
        creditCards = new ArrayList<>();
        Subscriptions = new ArrayList<>();
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
