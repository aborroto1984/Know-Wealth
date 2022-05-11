package com.example.knowwealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class UtilityProcessing extends AppCompatActivity {

    Button addBtn;
    FloatingActionButton closeBtn;
    ImageView backArrow;
    TextView skipNext;

    //Recycler view fields
    ArrayList<String> utilities, dates;
    RecyclerView utilityList;
    RecyclerView.LayoutManager  layoutManager;
    RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility_processing);

        utilities = new ArrayList<>();
        dates = new ArrayList<>();

        backArrow = findViewById(R.id.backArrow2);
        skipNext = findViewById(R.id.skip_next);
        addBtn = findViewById(R.id.addButton);
        closeBtn = findViewById(R.id.closeButton);
        utilityList = (RecyclerView) findViewById(R.id.utilityDueDatesList);

        adapter = new RecyclerViewAdapter(this, utilities, dates, closeBtn);
        layoutManager = new LinearLayoutManager(this);
        utilityList.setAdapter(adapter);
        utilityList.setLayoutManager(layoutManager);

        skipNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(UtilityProcessing.this, CreditDatesProcessing.class));
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UtilityProcessing.this, CreateAccount.class));
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showDialog();
            }
        });
    }
    public void Menu(View view) {
        startActivity(new Intent(UtilityProcessing.this, Menu.class));
    }

    private void showDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_utility_dialog);
        // This closes the dialog if the user clicks outside the dialog
        dialog.setCancelable(true);
        dialog.show();

        //Initializing the views of the dialog
        Spinner utilitySpinner = dialog.findViewById(R.id.utilityTypeSpinner);
        Spinner dueDateSpinner = dialog.findViewById(R.id.dueDateSpinner);
        FloatingActionButton addValueBtn = dialog.findViewById(R.id.addValueButton);

        addValueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String utility = utilitySpinner.getSelectedItem().toString();
                String date = dueDateSpinner.getSelectedItem().toString();
                utilities.add(utility);
                dates.add(date);
                skipNext.setText("Next");
                adapter.notifyDataSetChanged();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });



    }


}