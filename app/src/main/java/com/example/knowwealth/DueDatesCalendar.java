package com.example.knowwealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class DueDatesCalendar extends AppCompatActivity implements CalendarAdapter.OnItemListener{

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_dates_calendar);
        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();
        TextView textView = findViewById(R.id.selectedDay);
        textView.setText(dayFromDate(selectedDate));
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
    public void onItemClick(int position, String dayText) {
        if (!dayText.equals("")) {
            TextView textView = findViewById(R.id.selectedDay);
            textView.setText(dayText);
        }
    }
    public void Menu(View view) {
        startActivity(new Intent(DueDatesCalendar.this, Menu.class));
    }
}