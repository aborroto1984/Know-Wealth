package com.example.knowwealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class MonthlyExpenses extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private GestureDetector gestureDetector;

    User user;
    ArrayList<String> expenses, eAmounts, budgets;
    ArrayList<Integer> percent;

    Button addBudget, addExpense2;
    TextView expensesPageTitle;
    TextView expensesTotal;
    float total = 0;

    // RecyclerView elements for the background list
    ProgressBar expensesBar;
    RecyclerView expensesList;
    RecyclerView.LayoutManager  expensesLayoutManager;
    RecyclerView.Adapter expensesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_expenses);
        // Gesture feature
        gestureDetector = new GestureDetector(getApplicationContext(),this);

        // Getting user instance
        user = LoginActivity.user;

        // Setting up the page title
        expensesPageTitle = findViewById(R.id.expensesTitle);
        expensesTotal = findViewById(R.id.expensesTotal);
        addBudget = findViewById(R.id.addBudget);
        addExpense2 = findViewById(R.id.addExpense2);
        pageTitleSet();

        // Populating arrays from user instance
        expenses = new ArrayList<>();
        eAmounts = new ArrayList<>();
        budgets = new ArrayList<>();
        percent = new ArrayList<>();
        if( user.expenses.size() > 0){
            for (int i = 0; i <= user.expenses.size() - 1; i++){
                User.Expense temp = user.expenses.get(i);
                expenses.add(temp.getName());
                eAmounts.add(formatCurrency(temp.getAmount()));
                String text = formatCurrency(temp.getName());//String text = formatCurrency(temp.getBudget());
                if (Float.parseFloat(temp.getName()) > 0){//if (Float.parseFloat(temp.getBudget()) > 0){
                    budgets.add("BUDGET: " + text.replaceAll("[-]", ""));
                }else{
                    budgets.add("");
                }
                total += Float.parseFloat(temp.getAmount());
            }
            expensesTotal.setText(formatCurrency(String.valueOf(total)));
        }

        // Calculating the progress bar values
        calculateExpensePercent();

        // Setting up the expenses list
        expensesBar = findViewById(R.id.expenseProgressBar);
        expensesList = findViewById(R.id.expensesList);
        expensesAdapter = new ExpensesAdapter(this, expenses, eAmounts, percent, budgets, expensesBar);
        expensesLayoutManager = new LinearLayoutManager(this);
        expensesList.setAdapter(expensesAdapter);
        expensesList.setLayoutManager(expensesLayoutManager);
        expensesAdapter.notifyDataSetChanged();

        //----------------------------------------------------------------------------------------------------------------  BUTTONS
        addBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setCurrentActivity("budgets");
                startActivity(new Intent(MonthlyExpenses.this, ProcessingScreens.class));
            }
        });

        addExpense2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setCurrentActivity("expenses");
                startActivity(new Intent(MonthlyExpenses.this, ProcessingScreens.class));
            }
        });
    }

    //----------------------------------------------------------------------------------------------------------------  HELPER METHODS
    // Menu
    public void Menu(View view) {
        startActivity(new Intent(MonthlyExpenses.this, Menu.class));
    }
    // Currency formatter
    private static String formatCurrency(String number){
        DecimalFormat formatter = new DecimalFormat("-$ ###,###,##0.00");
        return formatter.format(Float.parseFloat(number));
    }

    public void pageTitleSet(){
        DateFormat dateFormat = new SimpleDateFormat("MMMM");
        Date date = new Date();
        expensesPageTitle.setText(dateFormat.format(date) + "\nExpenses");
    }

    public void calculateExpensePercent(){
        float greater = 0;
        for (int i = 0; i < eAmounts.size(); i++) {
            String amountNum = eAmounts.get(i).replaceAll("[$,,-]", "");
            float amount = Float.parseFloat(amountNum);
            if (amount > greater){
                greater = amount;
            }
        }
        for (int i = 0; i < eAmounts.size(); i++) {
            //float amount = Float.parseFloat(eAmounts.get(i));
            String amountNum = eAmounts.get(i).replaceAll("[$,,-]", "");
            float amount = Float.parseFloat(amountNum);
            percent.add((int) Math.ceil(amount*100/greater));
        }
    }
    //----------------------------------------------------------------------------------------------------------------  SWIPING METHODS
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent motionEvent, float x, float y) {
        boolean result = false;
        float diffY = motionEvent.getY() - downEvent.getY();
        float diffX = motionEvent.getX() - downEvent.getX();

        if (Math.abs(diffX) > 100 && Math.abs(x) > 100){
            if (diffX > 0){
                onSwipeRight();
            }else{
                onSwipeLeft();
            }
            result = true;
        }else{
            if (Math.abs(diffY) > 100 && Math.abs(y) > 100){
                if (diffX > 0){
                    onSwipeBottom();
                }else{
                    onSwipeTop();
                }
                result = true;
            }
        }

        return result;
    }

    private void onSwipeTop() {
    }

    private void onSwipeBottom() {
    }

    private void onSwipeLeft() {
        startActivity(new Intent(MonthlyExpenses.this, DueDatesCalendar.class));
    }

    private void onSwipeRight() {
        startActivity(new Intent(MonthlyExpenses.this, DashBoard.class));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}