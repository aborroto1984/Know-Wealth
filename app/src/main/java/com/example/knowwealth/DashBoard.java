package com.example.knowwealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashBoard extends AppCompatActivity {

    Button addBtn;
    FloatingActionButton closeBtn;

    User user = LoginActivity.user;

    //Recycler view fields
    ArrayList<String> utilities, dates;
    RecyclerView utilityList;
    RecyclerView.LayoutManager  layoutManager;
    RecyclerView.Adapter adapter;

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
    }

    public void Menu(View view) {
        startActivity(new Intent(DashBoard.this, Menu.class));
    }

}