package com.example.knowwealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashBoard extends AppCompatActivity implements GestureDetector.OnGestureListener{

    Button addExp;

    User user = LoginActivity.user;

    //Recycler view fields
    ArrayList<String> utilities, dates;
    RecyclerView utilityList;
    RecyclerView.LayoutManager  layoutManager;
    RecyclerView.Adapter adapter;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        Calendar calendar = Calendar.getInstance();
        String curDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        TextView viewDate = findViewById(R.id.view_date);
        viewDate.setText(curDate);

        String userName = User.getFirstName();
        TextView userFirstName = findViewById(R.id.view_Name);
        userFirstName.setText(userName);

        utilities = new ArrayList<>();
        dates = new ArrayList<>();

        if (user.utilities.size() > 0) {
            for (int i = 0; i <= user.utilities.size()-1; i++) {
                User.UtilDate temp = user.utilities.get(i);
                utilities.add(temp.getName());
                dates.add(temp.getDueDay());
            }
            utilityList = (RecyclerView) findViewById(R.id.Due_List);
            adapter = new RecyclerViewAdapter(this, utilities, dates, null);
            layoutManager = new LinearLayoutManager(this);
            utilityList.setAdapter(adapter);
            utilityList.setLayoutManager(layoutManager);
        }
        addExp = findViewById(R.id.add_expense_button);

        addExp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(DashBoard.this, MonthlyExpenses.class));
            }
        });

        gestureDetector = new GestureDetector(this);
    }

    public void Menu(View view) {
        startActivity(new Intent(DashBoard.this, Menu.class));
    }

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
        startActivity(new Intent(DashBoard.this, DueDatesCalendar.class));
    }

    private void onSwipeRight() {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}