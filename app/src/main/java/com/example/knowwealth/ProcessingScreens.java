package com.example.knowwealth;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProcessingScreens extends AppCompatActivity {

    static DatabaseReference userDatabase= User.userDatabase.child(User.getuID());
    // Processing screen elements
    Button addBtn;
    FloatingActionButton closeBtn;
    ImageView backArrow;
    TextView skipNext;
    TextView pageTitle;

    // User holder
    User user;

    // Global variables holders
    String currentActivity;
    boolean processingCompleted;

    // User input data holders
    ArrayList<String> utilities, uDates, uPaid;
    ArrayList<String> subscriptions, sDates, sPaid;
    ArrayList<String> creditCards, cDates, cPaid;
    ArrayList<String> expenses, eAmounts;
    ArrayList<String> budgets, bAmounts;

    // Options for the nameSpinner to change it dynamically
    ArrayList<String> spinnerOptions = new ArrayList<>();

    // RecyclerView elements for the background list
    RecyclerView recyclerView;
    RecyclerView.LayoutManager  layoutManager;
    RecyclerView.Adapter adapter;

    // Calendar instance
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility_processing);

        // Initializing user and global variables
        user = LoginActivity.user;
        currentActivity = user.getCurrentActivity();
        processingCompleted = user.getProcessingCompleted();

        // Initializing buttons and text
        backArrow = findViewById(R.id.backArrow2);
        skipNext = findViewById(R.id.skip_next);
        addBtn = findViewById(R.id.addButton);
        closeBtn = findViewById(R.id.closeButton);
        pageTitle = findViewById(R.id.pageTitle);

        // Initializing recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.utilityDueDatesList);

        // Dynamically initializing correct storage, changing texts, and making elements visible or invisible
        if (currentActivity.equals("utility")){
            utilities = new ArrayList<>();
            uDates = new ArrayList<>();
            uPaid = new ArrayList<>();
            rePopulateLists(user.utilities, utilities, uDates, uPaid);
            setSkipNext();
            pageTitle.setText("Utilities");
            setRecyclerView(this, utilities, uDates, closeBtn);
        }
        else if (currentActivity.equals("subscriptions")){
            subscriptions = new ArrayList<>();
            sDates = new ArrayList<>();
            sPaid = new ArrayList<>();
            rePopulateLists(user.subscriptions, subscriptions, sDates, sPaid);
            setSkipNext();
            pageTitle.setText("Monthly Subscriptions");
            setRecyclerView(this, subscriptions, sDates, closeBtn);
        }
        else if (currentActivity.equals("creditCards")){
            creditCards = new ArrayList<>();
            cDates = new ArrayList<>();
            cPaid = new ArrayList<>();
            rePopulateLists(user.creditCards, creditCards, cDates, cPaid);
            setSkipNext();
            pageTitle.setText("Credit Card Due Dates");
            setRecyclerView(this, creditCards, cDates, closeBtn);
        }
        else if (currentActivity.equals("expenses")){
            expenses = new ArrayList<>();
            eAmounts = new ArrayList<>();
            if( user.expenses.size() > 0){
                for (int i = 0; i < user.expenses.size(); i++){
                    User.Expense tempExpense = user.expenses.get(i);
                    if (tempExpense.getMonth().equals(LocalDate.now().getMonth().toString())) {
                        if (!tempExpense.getAmount().equals(" 0.00")){
                            if (!tempExpense.getAmount().equals("0.0")){
                                expenses.add(tempExpense.getName());
                                eAmounts.add(formatCurrency(tempExpense.getAmount()));
                            }
                        }
                    }
                }
            }
            setSkipNext();
            pageTitle.setText("Enter Expense");
            setRecyclerView(this, expenses, eAmounts, closeBtn);
        }
        else if (currentActivity.equals("budgets")){
            budgets = new ArrayList<>();
            bAmounts = new ArrayList<>();
            if( user.budgets.size() > 0){
                for (int i = 0; i < user.budgets.size(); i++){
                    User.Budget temp = user.budgets.get(i);
                        budgets.add(temp.getName());
                        bAmounts.add(temp.getAmount());
                }
            }
            setSkipNext();
            pageTitle = findViewById(R.id.pageTitle);
            pageTitle.setText("Add Budget");
            setRecyclerView(this, budgets, bAmounts, closeBtn);
        }

        //----------------------------------------------------------------------------------------------------------------  BUTTONS
        skipNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!processingCompleted) {
                    if (currentActivity.equals("utility")) {
                        user.setCurrentActivity("subscriptions");
                        refreshActivity();
                    } else if (currentActivity.equals("subscriptions")) {
                        user.setCurrentActivity("creditCards");
                        refreshActivity();
                    } else if (currentActivity.equals("creditCards")) {
                        user.setProcessingCompleted(true);
                        user.userDatabase.child(User.getuID()).child("Processing Completed").setValue("true");
                        startActivity(new Intent(ProcessingScreens.this, DashBoard.class));
                    } else if (currentActivity.equals("expenses")) {
                        //finish();
                        startActivity(new Intent(ProcessingScreens.this, MonthlyExpenses.class));
                    } else if (currentActivity.equals("budgets")) {
                        //finish();
                        startActivity(new Intent(ProcessingScreens.this, MonthlyExpenses.class));
                    }
                }
                finish();
            }
        });


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (processingCompleted){finish();}
                if(currentActivity.equals("utility")){
                    finish();
                }else if (currentActivity.equals("subscriptions")){
                    user.setCurrentActivity("utility");
                    refreshActivity();
                }else if (currentActivity.equals("creditCards")){
                    user.setCurrentActivity("subscriptions");
                    refreshActivity();
                }else if (currentActivity.equals("expenses")){
                    finish();
                }else if (currentActivity.equals("budgets")){
                    finish();
                    startActivity(new Intent(ProcessingScreens.this, MonthlyExpenses.class));
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showAddValueDialog();
            }
        });
    }

    // Overwriting click event for the phone back button
    @Override
    public void onBackPressed(){
        if (processingCompleted){finish();}
//        else if(currentActivity.equals("utility")){
//            finish();}
        else if (currentActivity.equals("subscriptions")){
            user.setCurrentActivity("utility");
            refreshActivity();
        }else if (currentActivity.equals("creditCards")){
            user.setCurrentActivity("subscriptions");
            refreshActivity();
        }
    }

    //----------------------------------------------------------------------------------------------------------------  HELPER METHODS

    // Currency formatter
    private static String formatCurrency(String number){
        DecimalFormat formatter = new DecimalFormat("$ ###,###,##0.00");
        return formatter.format(Float.parseFloat(number));
    }

    // Method to reload the activity
    private void refreshActivity(){
        finish();
        startActivity(getIntent());
    }

    // Method to change the text in the skipNext button
    private void setSkipNext(){
        switch (user.getCurrentActivity()){
            case "utility":
                if(processingCompleted){skipNext.setText("Done");}
                else if(utilities.isEmpty()){skipNext.setText("Skip");}
                else{skipNext.setText("Next");}
                break;
            case "subscriptions":
                if(!subscriptions.isEmpty() && !processingCompleted){skipNext.setText("Next");}
                else if(subscriptions.isEmpty() && !processingCompleted){skipNext.setText("Skip");}
                else{skipNext.setText("Done");}
                break;
            case "creditCards":
                if(!creditCards.isEmpty() && !processingCompleted){skipNext.setText("Next");}
                else if(creditCards.isEmpty() && !processingCompleted){skipNext.setText("Skip");}
                else{skipNext.setText("Done");}
                break;
            case "expenses":
            case "budgets":
                skipNext.setText("Done");
                break;
        }

    }

    // Method to set the recyclerView adapter
    private void setRecyclerView(Context ct, ArrayList<String> names, ArrayList<String> dueDates, FloatingActionButton closeBtn){
        adapter = new RecyclerViewAdapter(ct, names, dueDates, null, closeBtn, null);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    // Method to populate the storage lists in the user instance
    private void populateLists(String name, String data, String paid){
        String month = LocalDate.now().getMonth().toString();
        int cnt = 1;
        if (currentActivity.equals("utility")){
            while (cnt < 6) {
                user.utilities.add(new User.UtilDate(month, name, data, paid));
                month = LocalDate.now().getMonth().plus(cnt).toString();
                cnt++;
            }
            utilities.add(name);
            uDates.add(data);
            uPaid.add(paid);
            addToFirebase("Utilities", name, data, paid);
        }
        else if (currentActivity.equals("subscriptions")){
            while (cnt < 6) {
                user.subscriptions.add(new User.UtilDate(month, name, data, paid));
                month = LocalDate.now().getMonth().plus(cnt).toString();
                cnt++;
            }
            subscriptions.add(name);
            sDates.add(data);
            sPaid.add(paid);
            addToFirebase("Subscriptions", name, data, paid);
        }
        else if (currentActivity.equals("creditCards")){
            while (cnt < 6) {
                user.creditCards.add(new User.UtilDate(month, name, data, paid));
                month = LocalDate.now().getMonth().plus(cnt).toString();
                cnt++;
            }
            creditCards.add(name);
            cDates.add(data);
            cPaid.add(paid);
            addToFirebase("Credit Cards", name, data, paid);
        }
        else if (currentActivity.equals("expenses")){
            boolean contains = false;
            int index = 0;
            for (int i = 0; i < user.expenses.size(); i++){
                if(user.getExpenses().get(i).getName().equals(name) && user.getExpenses().get(i).getMonth().equals(LocalDate.now().getMonth().toString())){
                    contains = true;
                    index = i;
                }
            }
            if(contains){
                user.getExpenses().get(index).AddExpense(data);
            }else{
                user.expenses.add(new User.Expense(LocalDate.now().getMonth().toString(), name, data));
            }
            if (!data.equals("$ 0.00")){
                if (!data.equals("0.0")){
                    expenses.add(name);
                    eAmounts.add(data);
                }
            }
            userDatabase.child(LocalDate.now().getMonth().toString() + "/Expenses/" + name).setValue(data);
        }
        else if (currentActivity.equals("budgets")){
            userDatabase.child("Budgets/" + name).setValue(data);
            if (budgets.size() > 0) { //if any budgets exist
                boolean exists = false;
                for (int i = 0; i < budgets.size(); i++) { // update existing budget
                    if (budgets.get(i).equals(name)) {
                        bAmounts.set(i, data);
                        exists = true;
                        User.Budget tempBudget = null;
                        for (int j = 0; j < User.budgets.size(); j++) {     // loop through budgets to store a match as temp
                            tempBudget = User.budgets.get(j);
                            if (name.equals(tempBudget.getName())) {
                                tempBudget.setAmount(data);
                            }
                        }
                        break;
                    }

                }
                if (!exists){ // or create a new one
                    budgets.add(name);
                    bAmounts.add(data);
                }
            }
            else{
                budgets.add(name);
                bAmounts.add(data);
            }
            for(int temp = 0; temp <= 6; ++temp) {
                if (Budgets.values()[temp].toString() == name){
                    user.budgets.set(temp, new User.Budget(name, data));
                }
            }
        }
        setSkipNext();
    }

    // Method to repopulate local storage lists from the user instance when the activity starts
    private void rePopulateLists(List<User.UtilDate> arrName, ArrayList<String> name, ArrayList<String> data, ArrayList<String> paid) {
        if (arrName.size() > 0) {
            for (int i = 0; i < arrName.size(); i++) {
                User.UtilDate temp = arrName.get(i);
                if (temp.getMonth().equals(LocalDate.now().getMonth().toString())) {
                    name.add(temp.getName());
                    data.add(temp.getData());
                    paid.add(temp.getPaid());
                }
            }
        }
    }

    //Method to add new data to firebase database
    private void addToFirebase(String arrList, String name, String data, String paid){

        Month month1 = Month.JANUARY;
        String month = month1.toString();
        for (int i = 0; i < 12; i++) {
            userDatabase.child(month + "/" + arrList + "/" + name).child("Due Date").setValue(data);
            userDatabase.child(month + "/" + arrList + "/" + name).child("Paid").setValue(paid);
            month1 = month1.plus(1);
            month = month1.toString();
        }

    }
    //----------------------------------------------------------------------------------------------------------------  DIALOGS

    // Dialog to enter values into the recyclerView and the user instance storage
    private void showAddValueDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_utility_dialog);
        // This closes the dialog if the user clicks outside the dialog
        dialog.setCancelable(true);
        spinnerOptions.clear();
        dialog.show();

        //Initializing the views of the dialog
        TextView description = dialog.findViewById(R.id.descriptionTitle);
        TextView otherDescription = dialog.findViewById(R.id.descriptionTitle2);
        TextView dueDate = dialog.findViewById(R.id.dueDateText);
        TextView amountText = dialog.findViewById(R.id.amountText);
        EditText amount = dialog.findViewById(R.id.amount);
        EditText otherName = dialog.findViewById(R.id.otherName);
        Spinner utilitySpinner = dialog.findViewById(R.id.nameSpinner);
        Spinner dueDateSpinner = dialog.findViewById(R.id.dueDateSpinner);
        FloatingActionButton addValueBtn = dialog.findViewById(R.id.addValueButton);

        //Creating a spinner adapter
        switch (currentActivity){
            case "utility":
                description.setText("Utility Type");
                spinnerOptions.add("Mortgage - Rent"); spinnerOptions.add("Power"); spinnerOptions.add("Water");
                spinnerOptions.add("Cable - Satellite"); spinnerOptions.add("Internet"); spinnerOptions.add("Cell Phone"); spinnerOptions.add("Car Payment");
                spinnerOptions.add("Other");
                break;
            case "subscriptions":
                description.setText("Subscription Name");
                spinnerOptions.add("HBO Max"); spinnerOptions.add("Hulu"); spinnerOptions.add("Netflix");
                spinnerOptions.add("Disney+"); spinnerOptions.add("Apple TV+"); spinnerOptions.add("Prime Video"); spinnerOptions.add("Sling TV");
                spinnerOptions.add("Other");
                break;
            case "creditCards":
                description.setText("Card Name");
                spinnerOptions.add("American Express"); spinnerOptions.add("Discover"); spinnerOptions.add("Wells Fargo");
                spinnerOptions.add("Capital One"); spinnerOptions.add("U.S Bank"); spinnerOptions.add("Citi"); spinnerOptions.add("Bank of America");
                spinnerOptions.add("Other");
                break;
            case "expenses":
                description.setText("Expense Type");
                dueDateSpinner.setVisibility(View.GONE);
                dueDate.setVisibility(View.GONE);
                amountText.setVisibility(View.VISIBLE);
                amount.setVisibility(View.VISIBLE);
                spinnerOptions.add("Food - Drink"); spinnerOptions.add("Groceries"); spinnerOptions.add("Fuel");
                spinnerOptions.add("Shopping"); spinnerOptions.add("Entertainment"); spinnerOptions.add("Transportation"); spinnerOptions.add("Restaurant");
                break;
            case "budgets":
                description.setText("Expense to Budget");
                dueDateSpinner.setVisibility(View.GONE);
                dueDate.setVisibility(View.GONE);
                amountText.setVisibility(View.VISIBLE);
                amount.setVisibility(View.VISIBLE);
                spinnerOptions.add("Food - Drink"); spinnerOptions.add("Groceries"); spinnerOptions.add("Fuel");
                spinnerOptions.add("Shopping"); spinnerOptions.add("Entertainment"); spinnerOptions.add("Transportation"); spinnerOptions.add("Restaurant");
                break;
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        utilitySpinner.setAdapter(spinnerAdapter);

        // Swapping the spinner for a textBox to enter other category
        utilitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (utilitySpinner.getSelectedItem().toString().equals("Other")){
                    description.setVisibility(View.GONE);
                    otherDescription.setVisibility(View.VISIBLE);
                    utilitySpinner.setVisibility(View.GONE);
                    otherName.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        addValueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                String data = null;
                String paid = null;
                if (currentActivity.equals("expenses") || currentActivity.equals("budgets")){
                    name = utilitySpinner.getSelectedItem().toString();

                    String test = amount.getText().toString();
                    if (!test.isEmpty()) {
                    data = formatCurrency(amount.getText().toString());}
                }
                else if( utilitySpinner.getVisibility() == View.GONE){
                    name = otherName.getText().toString();
                    data = dueDateSpinner.getSelectedItem().toString();
                    paid = "false";
                }
                else {
                    name = utilitySpinner.getSelectedItem().toString();
                    data = dueDateSpinner.getSelectedItem().toString();
                    paid = "false";
                }

                if (data != null) {
                    populateLists(name, data, paid);
                    adapter.notifyDataSetChanged();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }else{
                    Toast.makeText(ProcessingScreens.this,"Please enter amount.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}