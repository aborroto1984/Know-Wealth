package com.example.knowwealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DashBoard extends AppCompatActivity {

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
    }

    public void Menu(View view) {
        startActivity(new Intent(DashBoard.this, Menu.class));
    }

}