package com.example.knowwealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //Navigation------------------------------------------------------------------------

        // Variable for the SignUp text at the bottom of the page
        TextView signInText = (TextView) findViewById(R.id.loginText);

        //Click event to open Create Account activity
//        signInText.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent intent = new Intent(CreateAccount.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}



