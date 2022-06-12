package com.example.knowwealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class DashBoard extends AppCompatActivity implements GestureDetector.OnGestureListener{

    Button addExp;

    User user = LoginActivity.user;

    //Recycler view fields
    ArrayList<String> name, data;
    ArrayList<String> budgetCategory, budgetData;
    RecyclerView dueList;
    RecyclerView overBudgetList;
    RecyclerView.LayoutManager  layoutManager;
    RecyclerView.Adapter adapter;
    RecyclerView.Adapter budgetAdapter;
    Calendar calendar = Calendar.getInstance();
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                    }
                });

        String curDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView viewDate = findViewById(R.id.view_date);
        viewDate.setText(curDate);

        //Added user name to display
        String userName = User.getFirstName(); // need to change to pull first name from database
        TextView userFirstName = findViewById(R.id.view_Name);
        userFirstName.setText(userName);

        name = new ArrayList<>();
        data = new ArrayList<>();
        budgetCategory = new ArrayList<>();
        budgetData = new ArrayList<>();

        addExp = findViewById(R.id.add_expense_button);

    //----------------------------------------------------------------------------------------------------------------  BUTTONS
        addExp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                user.setCurrentActivity("expenses");
                startActivity(new Intent(DashBoard.this, ProcessingScreens.class));
            }
        });

        gestureDetector = new GestureDetector(getApplicationContext(),this);
    }

    //----------------------------------------------------------------------------------------------------------------  HELPER METHODS
    @Override
    protected void onResume() {
        super.onResume();
        name.clear();
        data.clear();
        PopulateLists();
        if (name.size() > 0) {
            dueList = (RecyclerView) findViewById(R.id.Due_List);
            adapter = new RecyclerViewAdapter(this, name, data, null, null, null);
            layoutManager = new LinearLayoutManager(this);
            dueList.setAdapter(adapter);
            dueList.setLayoutManager(layoutManager);
        }
        if (budgetCategory.size() > 0){
            overBudgetList = findViewById(R.id.budget_List);
            budgetAdapter = new RecyclerViewAdapter(this, budgetCategory, budgetData, null, null, null);
            layoutManager = new LinearLayoutManager(this);
            overBudgetList.setAdapter(budgetAdapter);
            overBudgetList.setLayoutManager(layoutManager);
        }
        adapter.notifyDataSetChanged();
    }

    private void PopulateLists(){
        if(user.utilities.size() > 0) {
            for (int i = 0; i <= user.utilities.size() - 1; i++) {
                User.UtilDate temp = user.utilities.get(i);
                if(temp.getMonth().equals(LocalDate.now().getMonth().toString())) {
                    addToList(temp);
                }
            }
        }
        if(user.creditCards.size() > 0) {
            for (int i = 0; i <= user.creditCards.size() - 1; i++) {
                User.UtilDate temp = user.creditCards.get(i);
                if(temp.getMonth().equals(LocalDate.now().getMonth().toString())) {
                    addToList(temp);
                }
            }
        }
        if(user.subscriptions.size() > 0) {
            for (int i = 0; i <= user.subscriptions.size() - 1; i++) {
                User.UtilDate temp = user.subscriptions.get(i);
                if(temp.getMonth().equals(LocalDate.now().getMonth().toString())) {
                    addToList(temp);
                }
            }
        }
        if(user.expenses.size() > 0) {
            for (int i = 0; i <= user.expenses.size() - 1; i++) {
                User.Expense temp = user.expenses.get(i);

                float amount = Float.parseFloat(temp.getAmount().replaceAll("[^0-9, .]", ""));
                float budget = Float.parseFloat(temp.getBudget().replaceAll("[^0-9, .]", ""));

                if (Float.parseFloat(temp.getBudget()) > 0){
                    budgetCategory.add(temp.getName());
                    budgetData.add(formatCurrency(String.valueOf(temp.getAmount()), true) + "    /    " + formatCurrency(String.valueOf(temp.getBudget()), false));
                }
            }
        }
    }
    // Currency formatter
    private static String formatCurrency(String number, boolean signed){
        DecimalFormat formatter;
        if (signed){
            formatter = new DecimalFormat("-$ ###,###,##0.00");
        }
        else{
            formatter = new DecimalFormat("$ ###,###,##0.00");
        }
        return formatter.format(Float.parseFloat(number));
    }

    private void addToList(User.UtilDate temp){
        String[] tempdayNum = temp.getData().split(" ");
        String dayNum = tempdayNum[1];
        int dayLength = dayNum.length();
        dayNum = dayNum.substring(0,dayLength -2);
        dayLength = Integer.parseInt(dayNum);
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        if (dayLength >= today && dayLength < today + 7) {
            name.add(temp.getName());
            data.add(temp.getData());
        }
    }
    // Overwriting click event for the phone back button
    @Override
    public void onBackPressed(){

        }

    public void Menu(View view) {
        startActivity(new Intent(DashBoard.this, Menu.class));
    }

    //----------------------------------------------------------------------------------------------------------------  SWIPE METHOD
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
        startActivity(new Intent(DashBoard.this, MonthlyExpenses.class));
    }

    private void onSwipeRight() {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}