package com.example.knowwealth;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ProcessingScreens extends AppCompatActivity {

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
    ArrayList<String> utilities, uDates;
    ArrayList<String> subscriptions, sDates;
    ArrayList<String> creditCards, cDates;
    ArrayList<String> expenses, eAmounts;
    //ArrayList<String> budgets, bAmounts;

    // Options for the nameSpinner to change it dynamically
    ArrayList<String> spinnerOptions = new ArrayList<>();

    // RecyclerView elements for the background list
    RecyclerView recyclerView;
    RecyclerView.LayoutManager  layoutManager;
    RecyclerView.Adapter adapter;


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
            rePopulateLists(user.utilities, utilities, uDates);
            setSkipNext();
            pageTitle = findViewById(R.id.pageTitle);
            pageTitle.setText("Utilities");
            setRecyclerView(this, utilities, uDates, closeBtn);
        }
        else if (currentActivity.equals("subscriptions")){
            subscriptions = new ArrayList<>();
            sDates = new ArrayList<>();
            rePopulateLists(user.subscriptions, subscriptions, sDates);
            setSkipNext();
            pageTitle = findViewById(R.id.pageTitle);
            pageTitle.setText("Monthly Subscriptions");
            setRecyclerView(this, subscriptions, sDates, closeBtn);
        }
        else if (currentActivity.equals("creditCards")){
            creditCards = new ArrayList<>();
            cDates = new ArrayList<>();
            rePopulateLists(user.creditCards, creditCards, cDates);
            setSkipNext();
            pageTitle = findViewById(R.id.pageTitle);
            pageTitle.setText("Credit Card Due Dates");
            setRecyclerView(this, creditCards, cDates, closeBtn);
        }
        else if (currentActivity.equals("expenses")){
            expenses = new ArrayList<>();
            eAmounts = new ArrayList<>();
            if( user.expenses.size() > 0){
                for (int i = 0; i <= user.expenses.size() - 1; i++){
                    User.Expense temp = user.expenses.get(i);
                    expenses.add(temp.getName());
                    eAmounts.add(temp.getAmount());
                }
            }
            setSkipNext();
            pageTitle = findViewById(R.id.pageTitle);
            pageTitle.setText("Enter Expense");
            setRecyclerView(this, expenses, eAmounts, closeBtn);
        }
//        else if (currentActivity.equals("budgets")){
//            budgets = new ArrayList<>();
//            bAmounts = new ArrayList<>();
//            pageTitle = findViewById(R.id.pageTitle);
//            pageTitle.setText("Add Budget");
//            setRecyclerView(this, budgets, bAmounts, closeBtn);
//        }

        //----------------------------------------------------------------------------------------------------------------  BUTTONS
        skipNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(processingCompleted){finish();}
                else if (currentActivity.equals("utility")){
                   user.setCurrentActivity("subscriptions");
                    refreshActivity();
                }else if (currentActivity.equals("subscriptions")){
                    user.setCurrentActivity("creditCards");
                    refreshActivity();
                }else if (currentActivity.equals("creditCards")){
                    user.setProcessingCompleted(true);
                    startActivity(new Intent(ProcessingScreens.this, DashBoard.class));
                }else if (currentActivity.equals("expenses")){finish();}
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (processingCompleted){finish();}
                else if(currentActivity.equals("utility")){
                    finish();
                }else if (currentActivity.equals("subscriptions")){
                    user.setCurrentActivity("utility");
                    refreshActivity();
                }else if (currentActivity.equals("creditCards")){
                    user.setCurrentActivity("subscriptions");
                    refreshActivity();
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
                skipNext.setText("Done");
                break;
        }

    }

    // Method to set the recyclerView adapter
    private void setRecyclerView(Context ct, ArrayList<String> names, ArrayList<String> dueDates, FloatingActionButton closeBtn){
        adapter = new RecyclerViewAdapter(ct, names, dueDates, closeBtn, null);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    // Method to populate the storage lists in the user instance
    private void populateLists(String name, String data){
        if (currentActivity.equals("utility")){
            user.utilities.add(new User.UtilDate(data, name));
            utilities.add(name);
            uDates.add(data);
        }
        else if (currentActivity.equals("subscriptions")){
            user.subscriptions.add(new User.UtilDate(data, name));
            subscriptions.add(name);
            sDates.add(data);
        }
        else if (currentActivity.equals("creditCards")){
            user.creditCards.add(new User.UtilDate(data, name));
            creditCards.add(name);
            cDates.add(data);
        }
        else if (currentActivity.equals("expenses")){
            boolean contains = false;
            int index = 0;
            for (int i = 0; i < user.expenses.size(); i++){
                if(user.getExpenses().get(i).getName().equals(name)){
                    contains = true;
                    index = i;
                }
            }
            if(contains){
                user.getExpenses().get(index).AddExpense(data);
            }else{
                user.expenses.add(new User.Expense(name, data));
            }
            expenses.add(name);
            eAmounts.add(data);
        }
//        else if (currentActivity.equals("budgets")){
//            budgets.add(name);
//            bAmounts.add(data);
//        }
        setSkipNext();
    }

    // Method to repopulate local storage lists from the user instance when the activity starts
    private void rePopulateLists(List<User.UtilDate> userArray, ArrayList<String> name, ArrayList<String> data){
        if( userArray.size() > 0){
            for (int i = 0; i <= userArray.size() - 1; i++){
                User.UtilDate temp = userArray.get(i);
                name.add(temp.getName());
                data.add(temp.getData());
            }
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
                spinnerOptions.add("Mortgage / Rent"); spinnerOptions.add("Power"); spinnerOptions.add("Water");
                spinnerOptions.add("Cable / Satellite"); spinnerOptions.add("Internet"); spinnerOptions.add("Cell Phone"); spinnerOptions.add("Car Payment");
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
                spinnerOptions.add("Food / Drink"); spinnerOptions.add("Groceries"); spinnerOptions.add("Fuel");
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

        // Setting up amount text box to take financial input
        amount.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        addValueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                String data;
                if (currentActivity.equals("expenses") || currentActivity.equals("budgets")){
                    name = utilitySpinner.getSelectedItem().toString();
                    data = amount.getText().toString();
                }
                else if( utilitySpinner.getVisibility() == View.GONE){
                    name = otherName.getText().toString();
                    data = dueDateSpinner.getSelectedItem().toString();
                }
                else {
                    name = utilitySpinner.getSelectedItem().toString();
                    data = dueDateSpinner.getSelectedItem().toString();
                }
                populateLists(name, data);
                adapter.notifyDataSetChanged();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }



}