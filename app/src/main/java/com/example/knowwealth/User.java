package com.example.knowwealth;

import android.graphics.drawable.Drawable;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    static ArrayList<Expense> expenses;

    DatabaseReference userDatabase= FirebaseDatabase.getInstance().getReferenceFromUrl("https://know-wealth-default-rtdb.firebaseio.com/").child("users");
    private static String uID;
    List<String> list;

    // Global variable to know
    static String currentActivity;
    static Boolean processingCompleted = false;
    static boolean switch1;
    static float sliderPosition;

    // Constructors
    public User(String uid) {

        utilities = new ArrayList<>();
        creditCards = new ArrayList<>();
        subscriptions = new ArrayList<>();
        expenses = new ArrayList<>();
        uID = uid;

        userDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email = snapshot.child("Email").getValue().toString();
                fullName = snapshot.child("Full Name").getValue().toString();
                firstName = snapshot.child("First Name").getValue().toString();
                for(DataSnapshot i : snapshot.child("Utilities").getChildren()){
                    utilities.add(new UtilDate(i.child("Due Date").getValue().toString(),i.getKey()));
                }
                for(DataSnapshot i : snapshot.child("Credit Cards").getChildren()){
                    creditCards.add(new UtilDate(i.child("Due Date").getValue().toString(),i.getKey()));
                }for(DataSnapshot i : snapshot.child("Subscriptions").getChildren()){
                    subscriptions.add(new UtilDate(i.child("Due Date").getValue().toString(),i.getKey()));
                }

//                expenses = new ArrayList<>();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //add toast for error message
            }
        });
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
    public void setProcessingCompleted( Boolean value){
        processingCompleted = value;
    }
    public void setCurrentActivity( String value){
        currentActivity = value;
    }
    public void setSwitch1(Boolean value){switch1 = value;}
    public void setSliderPosition(float sliderPosition) { User.sliderPosition = sliderPosition; }

    // Expenses list getter
    public ArrayList<Expense> getExpenses(){ return expenses; }

    // Utilities
    public static class UtilDate{
        private String dueDay;
        private String name;

        public UtilDate(){ }
        public UtilDate(String dueDay, String name){
            this.dueDay = dueDay;
            this.name = name;
        }
        public String getData(){return dueDay;}
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

    // method to remove deleted expenses
    public static void deleteExpenseFromList(String removeDueDay, String removeName, List<Expense> array){
        for (int i = 0; i < array.size(); i++){
            Expense data = array.get(i);
            if (data.name.equals(removeName) && data.amount == Float.parseFloat(removeDueDay)){
                array.remove(i);
            }
        }
    }

    // method to remove deleted budgets
    public static void deleteBudget(String removeName, List<Expense> array){
        for (int i = 0; i < array.size(); i++){
            Expense data = array.get(i);
            if (data.name.equals(removeName) && data.budget != 0){
                data.budget = 0;
                if (data.amount == 0){
                    array.remove(i);
                }
            }
        }
    }

    // Expenses
    public static class Expense {
        // Fields
        private String name;
        private float amount = 0;
        private float budget = 0;

        //Constructors
        public Expense(){}
        public Expense(String name, String amount){
            this.name = name;
            String amountNum = amount.replaceAll("[$,,]", "");
            this.amount = Float.parseFloat(amountNum);
        }

        // Expense amount getter
        public String getAmount() { return String.valueOf(amount); }
        public String getName() { return name; }

        // Method to add to the expense amount
        public void AddExpense(String amount) {
            String amountNum = amount.replaceAll("[$,,]", "");
            this.amount += Float.parseFloat(amountNum);
        }

        // Budget getter and setter
        public String getBudget() { return String.valueOf(budget); }
        public void setBudget(String budget) {
            String amountNum = budget.replaceAll("[$,,]", "");
            this.budget = Float.parseFloat(amountNum);
        }
    }
}
