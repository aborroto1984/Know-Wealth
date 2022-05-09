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

        TextView dashBoardLink = findViewById(R.id.dashBoardLink);
        TextView utilitiesLink = findViewById(R.id.utilitiesLink);
        TextView subsLInk = findViewById(R.id.subsLInk);
        TextView creditCardLink = findViewById(R.id.creditCardLink);
        TextView budgetLink = findViewById(R.id.budgetLink);
        TextView notifyLink = findViewById(R.id.notifyLink);
        TextView signOutLink = findViewById(R.id.signOutLink);

        dashBoardLink.setOnClickListener(v -> startActivity(new Intent(Menu.this, DashBoard.class)));
        utilitiesLink.setOnClickListener(v -> startActivity(new Intent(Menu.this, UtilityProcessing.class)));
        subsLInk.setOnClickListener(v -> startActivity(new Intent(Menu.this, SubscriptionProcessing.class)));
        creditCardLink.setOnClickListener(v -> startActivity(new Intent(Menu.this, CreditDatesProcessing.class)));
        budgetLink.setOnClickListener(v -> startActivity(new Intent(Menu.this, BudgetAdd.class)));
        notifyLink.setOnClickListener(v -> startActivity(new Intent(Menu.this, NotificationSettings.class)));
        signOutLink.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    public void backToPrevious(View view) {
        finish();
    }


}