package com.example.knowwealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DueDatesCalendar extends AppCompatActivity implements CalendarAdapter.OnItemListener, GestureDetector.OnGestureListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    RecyclerView itemList;
    RecyclerView.LayoutManager  layoutManager;
    RecyclerView.Adapter adapter;
    User user = LoginActivity.user;
    String dayText;
    ArrayList<String> name, data, daysInMonth;
    ArrayList<Integer> eventOnDay;
    MaterialCheckBox checkBox;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_dates_calendar);
        name = new ArrayList<>();
        data = new ArrayList<>();
        eventOnDay = new ArrayList<>();
        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();
        TextView textView = findViewById(R.id.selectedDay);
        textView.setText(dayFromDate(selectedDate));
        updateListview();
        gestureDetector = new GestureDetector(getApplicationContext(),this);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        daysInMonth = daysInMonthArray(selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this, eventOnDay);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date){
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        eventOnDay.clear();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++){
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek){
                daysInMonthArray.add("");
            }else{
                daysInMonthArray.add(String.valueOf(i-dayOfWeek));
            }
            eventOnDay.add(4);
        }
        return daysInMonthArray;
    }
    private String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }
    private String dayFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(("dd"));
        return date.format(formatter);
    }
    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calRecyclerView);
        monthYearText = findViewById(R.id.monthLabel);
    }

    public void previousMonthAction(View view){
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
        updateListview();
    }

    public void nextMonthAction(View view){
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
        updateListview();
    }
    @Override
    public void onItemClick(int position, String _dayText) {
        if (!_dayText.equals("")) {
            TextView textView = findViewById(R.id.selectedDay);
            textView.setText(_dayText);
            dayText = _dayText;
            updateListview();
        }
    }

    private void addToList(User.UtilDate temp){
        int today = 1;
        String[] tempdayNum = temp.getData().split(" ");
        String dayNum = tempdayNum[1];
        int dayLength = dayNum.length();
        dayNum = dayNum.substring(0,dayLength -2);
        dayLength = Integer.parseInt(dayNum);
        if (dayText == null){
            today = Integer.parseInt(dayFromDate(selectedDate));
        }else {
            today = Integer.parseInt(dayText);
        }
        if (dayLength == today) {
            name.add(temp.getName());
            data.add(temp.getData());
        }
        int i = -1;
        do {
            i++;
        }while (!dayNum.equals(daysInMonth.get(i)));
        eventOnDay.set(i, 0);
    }

    private void updateListview(){
        name.clear();
        data.clear();

        if(user.utilities.size() > 0) {
            for (int i = 0; i <= user.utilities.size() - 1; i++) {
                User.UtilDate temp = user.utilities.get(i);
                addToList(temp);
            }
        }
        if(user.creditCards.size() > 0) {
            for (int i = 0; i <= user.creditCards.size() - 1; i++) {
                User.UtilDate temp = user.creditCards.get(i);
                addToList(temp);
            }
        }
        if(user.subscriptions.size() > 0) {
            for (int i = 0; i <= user.subscriptions.size() - 1; i++) {
                User.UtilDate temp = user.subscriptions.get(i);
                addToList(temp);
            }
        }
//        if(user.expenses.size() > 0) {
//            for (int i = 0; i <= user.expenses.size() - 1; i++) {
//                User.UtilDate temp = user.expenses.get(i);
//                addToList(temp);
//            }
//        }
        if (name.size() > 0 && adapter == null) {
            itemList = (RecyclerView) findViewById(R.id.items_List);
            checkBox = findViewById(R.id.checkBox);
            adapter = new RecyclerViewAdapter(this, name, data, null, checkBox);
            layoutManager = new LinearLayoutManager(this);
            itemList.setAdapter(adapter);
            itemList.setLayoutManager(layoutManager);
        }else if(adapter != null){
            adapter.notifyDataSetChanged();
        }

    }
    public void Menu(View view) {
        startActivity(new Intent(DueDatesCalendar.this, Menu.class));
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
    }

    private void onSwipeRight() {
        startActivity(new Intent(DueDatesCalendar.this, MonthlyExpenses.class));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}