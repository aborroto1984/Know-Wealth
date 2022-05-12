package com.example.knowwealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class ProcessingScreens extends AppCompatActivity {

    Button addBtn;
    FloatingActionButton closeBtn;
    ImageView backArrow;
    TextView skipNext;
    TextView pageTitle;

    String currentActivity;
    boolean processingCompleted;

    ArrayList<String> utilities, uDates;
    ArrayList<String> subscriptions, sDates;
    ArrayList<String> creditCards, cDates;
    ArrayList<String> expenses, eAmounts;
    ArrayList<String> budgets, bAmounts;


    RecyclerView recyclerView;
    RecyclerView.LayoutManager  layoutManager;
    RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility_processing);

        backArrow = findViewById(R.id.backArrow2);
        skipNext = findViewById(R.id.skip_next);
        addBtn = findViewById(R.id.addButton);
        closeBtn = findViewById(R.id.closeButton);


        ////Need to create a global variable.

        recyclerView = (RecyclerView) findViewById(R.id.utilityDueDatesList);

        if (currentActivity == "utility"){

            utilities = new ArrayList<>();
            uDates = new ArrayList<>();
            pageTitle = findViewById(R.id.pageTitle);
            pageTitle.setText("Utilities");
            setRecyclerView(this, utilities, uDates, closeBtn);
        }
        else if (currentActivity == "subscriptions"){
            subscriptions = new ArrayList<>();
            sDates = new ArrayList<>();
            pageTitle = findViewById(R.id.pageTitle);
            pageTitle.setText("Monthly Subscriptions");
            setRecyclerView(this, subscriptions, sDates, closeBtn);
        }
        else if (currentActivity == "creditCards"){
            creditCards = new ArrayList<>();
            cDates = new ArrayList<>();
            pageTitle = findViewById(R.id.pageTitle);
            pageTitle.setText("Credit Card Due Dates");
            setRecyclerView(this, creditCards, cDates, closeBtn);
        }
        else if (currentActivity == "expenses"){
            expenses = new ArrayList<>();
            eAmounts = new ArrayList<>();
            pageTitle = findViewById(R.id.pageTitle);
            pageTitle.setText("Enter Expense");
            setRecyclerView(this, expenses, eAmounts, closeBtn);
        }
        else if (currentActivity == "budgets"){
            budgets = new ArrayList<>();
            bAmounts = new ArrayList<>();
            pageTitle = findViewById(R.id.pageTitle);
            pageTitle.setText("Add Budget");
            setRecyclerView(this, budgets, bAmounts, closeBtn);
        }


        skipNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (currentActivity == "utility"){
                   currentActivity = "subscriptions";
                }else if (currentActivity == "subscriptions"){
                    currentActivity = "creditCards";
                }else if (currentActivity == "creditCards"){
                    processingCompleted = true;
                    skipNext.setVisibility(View.GONE);
                }
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (processingCompleted){finish();}
                else if(currentActivity == "utility"){
                    finish();
                }else if (currentActivity == "subscriptions"){
                    currentActivity = "utility";
                }else if (currentActivity == "creditCards"){
                    currentActivity = "subscriptions";
                }
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showDialog();
            }
        });
    }

    private void setRecyclerView(Context ct, ArrayList<String> names, ArrayList<String> dueDates, FloatingActionButton closeBtn){
        adapter = new RecyclerViewAdapter(ct, names, dueDates, closeBtn);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void showDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_utility_dialog);
        // This closes the dialog if the user clicks outside the dialog
        dialog.setCancelable(true);
        dialog.show();

        //Initializing the views of the dialog
        Spinner utilitySpinner = dialog.findViewById(R.id.utilityTypeSpinner);
        Spinner dueDateSpinner = dialog.findViewById(R.id.dueDateSpinner);
        FloatingActionButton addValueBtn = dialog.findViewById(R.id.addValueButton);

        addValueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String utility = utilitySpinner.getSelectedItem().toString();
                String date = dueDateSpinner.getSelectedItem().toString();
                utilities.add(utility);
                uDates.add(date);
                skipNext.setText("Next");
                adapter.notifyDataSetChanged();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });



    }


}