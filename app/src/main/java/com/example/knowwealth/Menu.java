package com.example.knowwealth;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Menu extends AppCompatActivity {
    User user = LoginActivity.user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView homeLink = findViewById(R.id.dashBoardLink);
        TextView utilitiesLink = findViewById(R.id.utilitiesLink);
        TextView subsLInk = findViewById(R.id.subsLInk);
        TextView creditCardLink = findViewById(R.id.creditCardLink);
        TextView notifyLink = findViewById(R.id.notifyLink);
        TextView editAccountLink = findViewById(R.id.accountInfoLink);
        TextView signOutLink = findViewById(R.id.signOutLink);

        homeLink.setOnClickListener(v -> startActivity(new Intent(Menu.this, DashBoard.class)));
        utilitiesLink.setOnClickListener(v -> {
            user.setCurrentActivity("utility");
            startActivity(new Intent(Menu.this, ProcessingScreens.class));
        });
        subsLInk.setOnClickListener(v -> {
            user.setCurrentActivity("subscriptions");
            startActivity(new Intent(Menu.this, ProcessingScreens.class));
        });
        creditCardLink.setOnClickListener(v -> {
            user.setCurrentActivity("creditCards");
            startActivity(new Intent(Menu.this, ProcessingScreens.class));
        });
        notifyLink.setOnClickListener(v -> startActivity(new Intent(Menu.this, NotificationSettings.class)));
        editAccountLink.setOnClickListener(v -> startActivity(new Intent(Menu.this, AccountEdit.class)));
        signOutLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = null;
                Intent i = new Intent(Menu.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                FirebaseAuth.getInstance().signOut();
                startActivity(i);
            }
        });
    }

    public void backToPrevious(View view) {
        finish();
    }


}