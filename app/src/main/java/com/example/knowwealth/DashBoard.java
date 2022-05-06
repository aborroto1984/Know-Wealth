package com.example.knowwealth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

public class DashBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        Calendar calendar = Calendar.getInstance();
        String curDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        TextView viewDate = findViewById(R.id.view_date);
        viewDate.setText(curDate);
    }
}