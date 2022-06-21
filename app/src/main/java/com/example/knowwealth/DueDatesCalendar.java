package com.example.knowwealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
    RecyclerViewAdapter adapter;
    CalendarAdapter calendarAdapter;

    String dayText;
    ArrayList<String> name, data, daysInMonth, paid;
    ArrayList<Integer> eventOnDay;
    MaterialCheckBox checkBox;
    private GestureDetector gestureDetector;
    static String monthName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_dates_calendar);
        // Initialize local arrays
        name = new ArrayList<>(); // holds the name of the bill.
        data = new ArrayList<>(); // holds the date of the bill.
        paid = new ArrayList<>(); // holds status of paid check box.
        eventOnDay = new ArrayList<>(); // used to update with dates will have a dot.
        selectedDate = LocalDate.now(); // gets current date.
        TextView textView = findViewById(R.id.selectedDay);
        textView.setText(dayFromDate(selectedDate));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initWidgets();
        setMonthView();
        updateListview();
        gestureDetector = new GestureDetector(getApplicationContext(),this);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate)); // adds month and year to display
        monthName = selectedDate.getMonth().toString().toUpperCase(); // gets the name of the month based on the date stored in selectedDate.
        daysInMonth = daysInMonthArray(selectedDate); // gets the number of days in the month based on the date stored in selectedDate.

        //Initializes calendar.
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this, eventOnDay);
        //Initializes layout for the calendar
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date){
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        eventOnDay.clear(); // clears out array to reset for new month.
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
            eventOnDay.add(4); // adds 4s to the whole array, 4 sets the dot to invisible.
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
        selectedDate = selectedDate.minusMonths(1); // sets the month to the previous month
        monthName = selectedDate.getMonth().toString().toUpperCase(); // sets the monthName variable to the new months name
        setMonthView(); // resets the calendar recycler view to update to the new month.
        updateListview(); // resets the list view to the new month.
    }

    public void nextMonthAction(View view){
        selectedDate = selectedDate.plusMonths(1); // sets the month to the next month
        monthName = selectedDate.getMonth().toString().toUpperCase(); // sets the monthName variable to the new months name
        setMonthView(); // resets the calendar recycler view to update to the new month.
        updateListview(); // resets the list view to the new month.
    }
    @Override
    public void onItemClick(int position, String _dayText) {
        if (!_dayText.equals("")) { // checks for a click on a blank day.
            TextView textView = findViewById(R.id.selectedDay); //gets the day selected box from the xml file.
            textView.setText(_dayText); // sets the day selected box to the new selected date.
            dayText = _dayText; // updates variable for use latter.
            updateListview(); // resets the list view to the new selected day.
        }
    }

    private void addToList(User.UtilDate temp){
        int today;
        int numDays = selectedDate.lengthOfMonth(); // set numDays to the number of days in the month

        String[] tempDayNum = temp.getData().split(" "); // splits up the date into separate arrays. Example temp data "Every 1st"
        String dayNum = tempDayNum[1]; //sets dayNum array to the second string. "1st"
        int dayLength = dayNum.length(); // gets the length of the dayNum string. 3
        dayNum = dayNum.substring(0,dayLength -2); // removes the last to elements of the string. 1
        dayLength = Integer.parseInt(dayNum); //converts the string to int
        //change date to end of month if greater then end of month
       if(dayLength > numDays){
           dayLength = numDays;
           dayNum = Integer.toString(numDays);
       }
        if (dayText == null){
            today = Integer.parseInt(dayFromDate(selectedDate));
        }else {
            today = Integer.parseInt(dayText);
        }
        if (dayLength == today) {
            // fills in local array for displaying data
            name.add(temp.getName());
            data.add(temp.getData());
            paid.add(temp.getPaid());
        }
        int i = 0;
        // matches the eventOnDay array with the current month array
        while (!dayNum.equals(daysInMonth.get(i))){
            i++;
        }
        eventOnDay.set(i, 0); // changes the eventOnDay to 0 for the matching day of the month. 0 sets the dot to visible.
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateListview(){
        // clears the local arrays
        name.clear();
        data.clear();
        paid.clear();

        // loops through each of the user arrays for filling in the local arrays
        if(User.utilities.size() > 0) {
            for (int i = 0; i <= User.utilities.size() - 1; i++) {
                User.UtilDate temp = User.utilities.get(i);
                if(temp.getMonth().equals(monthName)) {
                    addToList(temp);
                }
            }
        }
        if(User.creditCards.size() > 0) {
            for (int i = 0; i <= User.creditCards.size() - 1; i++) {
                User.UtilDate temp = User.creditCards.get(i);
                if(temp.getMonth().equals(monthName)) {
                    addToList(temp);
                }
            }
        }
        if(User.subscriptions.size() > 0) {
            for (int i = 0; i <= User.subscriptions.size() - 1; i++) {
                User.UtilDate temp = User.subscriptions.get(i);
                if(temp.getMonth().equals(monthName)) {
                    addToList(temp);
                }
            }
        }

        if (name.size() > 0 && adapter == null) { // checks for entries in the local array and that the recycler view has not been initialized.
            itemList = (RecyclerView) findViewById(R.id.items_List); // selects the xml element for the recycler view
            checkBox = findViewById(R.id.checkBox); // find the check box element
            adapter = new RecyclerViewAdapter(this, name, data, paid, null, checkBox); // initializes the recycler view adapter
            layoutManager = new LinearLayoutManager(this); //sets the layout manager
            itemList.setAdapter(adapter);
            itemList.setLayoutManager(layoutManager);
        }else if(adapter != null){ // if adapter already exists. Updates the data in the list
            adapter.notifyDataSetChanged();
        }

    }

    public static String getMonthName() {return monthName;}
    // Switches to the menu if the menu icon is clicked
    public void Menu(View view) {
        startActivity(new Intent(DueDatesCalendar.this, Menu.class));
    }
    // gesture listener for catching swipe motion
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