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
import java.util.List;

public class ProcessingScreens extends AppCompatActivity {

    Button addBtn;
    FloatingActionButton closeBtn;
    ImageView backArrow;
    TextView skipNext;
    TextView pageTitle;
    User user = LoginActivity.user;

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
        User user = LoginActivity.user;
        currentActivity = user.getCurrentActivity();


        recyclerView = (RecyclerView) findViewById(R.id.utilityDueDatesList);

        if (currentActivity.equals("utility")){

            utilities = new ArrayList<>();
            uDates = new ArrayList<>();
            rePopulateLists(user.utilities, utilities, uDates);
            pageTitle = findViewById(R.id.pageTitle);
            pageTitle.setText("Utilities");
            setRecyclerView(this, utilities, uDates, closeBtn);
        }
        else if (currentActivity.equals("subscriptions")){
            subscriptions = new ArrayList<>();
            sDates = new ArrayList<>();
            rePopulateLists(user.subscriptions, subscriptions, sDates);
            pageTitle = findViewById(R.id.pageTitle);
            pageTitle.setText("Monthly Subscriptions");
            setRecyclerView(this, subscriptions, sDates, closeBtn);
        }
        else if (currentActivity.equals("creditCards")){
            creditCards = new ArrayList<>();
            cDates = new ArrayList<>();
            rePopulateLists(user.creditCards, creditCards, cDates);
            pageTitle = findViewById(R.id.pageTitle);
            pageTitle.setText("Credit Card Due Dates");
            setRecyclerView(this, creditCards, cDates, closeBtn);
        }
        else if (currentActivity.equals("expenses")){
            expenses = new ArrayList<>();
            eAmounts = new ArrayList<>();
            rePopulateLists(user.expenses, expenses, eAmounts);
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


        skipNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (currentActivity.equals("utility")){
                   user.setCurrentActivity("subscriptions");
                    refreshActivity();
                }else if (currentActivity.equals("subscriptions")){
                    user.setCurrentActivity("creditCards");
                    refreshActivity();
                }else if (currentActivity.equals("creditCards")){
                    processingCompleted = true;
                    skipNext.setVisibility(View.GONE);
                    startActivity(new Intent(ProcessingScreens.this, DashBoard.class));
                }
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
                showDialog();
            }
        });
    }

    private void refreshActivity(){
        finish();
        startActivity(getIntent());
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

                String name = utilitySpinner.getSelectedItem().toString();
                String data = dueDateSpinner.getSelectedItem().toString();
                populateLists(name, data);
                skipNext.setText("Next");
                adapter.notifyDataSetChanged();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

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
            user.expenses.add(new User.UtilDate(data, name));
            expenses.add(name);
            eAmounts.add(data);
        }
        else if (currentActivity.equals("budgets")){
            budgets.add(name);
            bAmounts.add(data);
        }
    }

    private void rePopulateLists(List<User.UtilDate> userArray, ArrayList<String> name, ArrayList<String> data){
        if( userArray.size() > 0){
            for (int i = 0; i <= userArray.size() - 1; i++){
                User.UtilDate temp = userArray.get(i);
                name.add(temp.getName());
                data.add(temp.getDueDay());
            }
        }
    }

}