package com.example.knowwealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DueDatesCalendar extends AppCompatActivity implements CalendarAdapter.OnItemListener{

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    RecyclerView itemList;
    RecyclerView.LayoutManager  layoutManager;
    RecyclerView.Adapter adapter;
    User user = LoginActivity.user;
    String dayText;
    ArrayList<String> name, data;
    MaterialCheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_dates_calendar);
        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();
        TextView textView = findViewById(R.id.selectedDay);
        textView.setText(dayFromDate(selectedDate));

        name = new ArrayList<>();
        data = new ArrayList<>();
        updateListview();
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date){
        ArrayList<String> daysInMonthArray = new ArrayList<>();
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
    }

    public void nextMonthAction(View view){
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
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
        String[] tempdayNum = temp.getDueDay().split(" ");
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
            data.add(temp.getDueDay());
        }
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
        if(user.expenses.size() > 0) {
            for (int i = 0; i <= user.expenses.size() - 1; i++) {
                User.UtilDate temp = user.expenses.get(i);
                addToList(temp);
            }
        }
        if (name.size() > 0 && adapter == null) {
            itemList = (RecyclerView) findViewById(R.id.items_List);
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
}