package com.example.knowwealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView editAccountLink = findViewById(R.id.accountInfoLink);
        TextView utilitiesLink = findViewById(R.id.utilitiesLink);
        TextView subsLInk = findViewById(R.id.subsLInk);
        TextView creditCardLink = findViewById(R.id.creditCardLink);
        TextView homeLink = findViewById(R.id.dashBoardLink);
        TextView notifyLink = findViewById(R.id.notifyLink);
        TextView signOutLink = findViewById(R.id.signOutLink);

        editAccountLink.setOnClickListener(v -> startActivity(new Intent(Menu.this, AccountEdit.class)));
        utilitiesLink.setOnClickListener(v -> startActivity(new Intent(Menu.this, ProcessingScreens.class)));
        subsLInk.setOnClickListener(v -> startActivity(new Intent(Menu.this, SubscriptionProcessing.class)));
        creditCardLink.setOnClickListener(v -> startActivity(new Intent(Menu.this, CreditDatesProcessing.class)));
        homeLink.setOnClickListener(v -> startActivity(new Intent(Menu.this, DashBoard.class)));
        notifyLink.setOnClickListener(v -> startActivity(new Intent(Menu.this, NotificationSettings.class)));
        signOutLink.setOnClickListener(v -> startActivity(new Intent(Menu.this, LoginActivity.class)));
    }

    public void backToPrevious(View view) {
        finish();
    }


}