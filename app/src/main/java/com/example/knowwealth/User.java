package com.example.knowwealth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

enum Budgets{
    Entertainment,
    Food,
    Fuel,
    Groceries,
    Restaurant,
    Shopping,
    Transportation
}
public class User {
    // User's first name to be used in DashBoard
    private static String firstName;
    private static String fullName;
    private static String email;

    // Data fields
    static List<UtilDate> utilities;
    static List<UtilDate> creditCards;
    static List<UtilDate> subscriptions;
    static ArrayList<Expense> expenses;
    static ArrayList<Boolean> paid;
    static ArrayList<Budget> budgets;

    static DatabaseReference userDatabase= FirebaseDatabase.getInstance().getReferenceFromUrl("https://know-wealth-default-rtdb.firebaseio.com/").child("users");
    private static String uID;
    static DataSnapshot snapshot;

    // Global variable to know
    static String currentActivity;
    static boolean processingCompleted;
    static boolean switch1;
    static float sliderPosition;


    // Constructors
    public User(String uid) {

        utilities = new ArrayList<>();
        creditCards = new ArrayList<>();
        subscriptions = new ArrayList<>();
        expenses = new ArrayList<>();
        paid = new ArrayList<>();
        budgets = new ArrayList<>();
        uID = uid;
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
        paid = new ArrayList<>();
        budgets = new ArrayList<>();
    }

    public static String getuID() {
        return uID;
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
    public boolean getSwitch1(){return switch1;}
    public float getSliderPosition() { return sliderPosition;}

    // Global variables setters
    public void setProcessingCompleted( Boolean value){processingCompleted = value;}
    public void setCurrentActivity( String value){currentActivity = value;}
    public void setSwitch1(Boolean value){switch1 = value;}
    public void setSliderPosition(float sliderPosition) { User.sliderPosition = sliderPosition; }

    // Expenses list getter
    public ArrayList<Expense> getExpenses(){ return expenses; }

    // Utilities
    public static class UtilDate{
        private String dueDay;
        private String name;
        private String paid;
        private String month;

        public UtilDate( String month, String name, String dueDay, String paid ){
            this.dueDay = dueDay;
            this.name = name;
            this.paid = paid;
            this.month = month;
        }
        public String getData(){return dueDay;}
        public String getName(){return name;}
        public String getPaid(){return paid;}
        public String getMonth(){return month;}

    }
    public static void updatedPaid(String _month, String _name, String _dueDay, String _paid, String arrayName, List<UtilDate> array ) {

        for (int i = 0; i < array.size(); i++) {
            UtilDate data = array.get(i);
            if(data.month.equals(_month) && data.name.equals(_name) && data.dueDay.equals(_dueDay)){
                array.set(i,new UtilDate(_month,_name,_dueDay,_paid));
                userDatabase.child(uID + "/" + _month + "/" + arrayName + "/" + _name).child("Paid").setValue(_paid);
            }
        }
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

    // method to remove deleted expenses
    public static void deleteExpenseFromList(String month, String name, String amount, List<Expense> array){
        for (int i = 0; i < array.size(); i++){
            Expense tempExpense = array.get(i);
            if (tempExpense.name.equals(name) && tempExpense.month.equals(month)){
                String expenseNum = amount.replaceAll("[$,,]", "");
                Float tempSubtract = Float.parseFloat(expenseNum);
                Float tempOld = Float.parseFloat(tempExpense.getAmount());
                tempOld -= tempSubtract;
                tempExpense.setAmount(String.valueOf(tempOld));
            }
        }
    }

    // Expenses
    public static class Expense {
        // Fields
        private String name;
        private String amount;
        private String month;

        //Constructors
        public Expense(String month, String name, String amount){ // add budget to parameter
            this.name = name;
            String amountNum = amount.replaceAll("[$,,]", "");
            this.amount = amountNum;
            this.month = month;
        }

        // getters
        public String getAmount() { return amount; }
        public String getName() { return name; }
        public String getMonth() {return month;}

        // sEtTers
        public void setMonth(String month) {this.month = month;}
        public void setAmount(String amount) {this.amount = amount;}

        // Method to add to the expense amount
        public void AddExpense(String amount) {
            String expenseNum = amount.replaceAll("[$,,]", "");
            Float tempAdd = Float.parseFloat(expenseNum);
            Float tempOld = Float.parseFloat(this.amount);
            tempOld += tempAdd;
            this.amount = String.valueOf(tempOld);
        }
    }

    public static class Budget {
        private String name;
        private String amount;

        public Budget(String name, String amount){
            this.name = name;
            this.amount = amount;
        }

        public String getName() {
            return name;
        }

        public String getAmount() { return amount; }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
