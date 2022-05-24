package com.example.knowwealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.BreakIterator;

public class AccountEdit extends AppCompatActivity {
    User user = LoginActivity.user;
    Button appbtn = findViewById(R.id.apply_button);
    EditText fullNameField = findViewById(R.id.fullName);
    EditText userIdField = findViewById(R.id.userIdCreateAccount);
    EditText password = findViewById(R.id.passwordCreateAccount1);
    EditText passwordConfirm = findViewById(R.id.passwordCreateAccount2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);

        appbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(password.getText().toString().isEmpty()) {
                    Toast.makeText(AccountEdit.this, "Password cannot be blank.",
                            Toast.LENGTH_SHORT).show();
                }else if(!passwordConfirm.getText().toString().equals(password.getText().toString())) {
                    Toast.makeText(AccountEdit.this, "Passwords do not match.",
                            Toast.LENGTH_LONG).show();
                }else if(passwordConfirm.getText().toString().equals(password.getText().toString())){
                    changeAccountInfo();
                }
            }
        });
    }

    private void changeAccountInfo(){
        String fullName = user.getFullName();
        String userId = user.getEmail();

        if (!fullNameField.getText().toString().equals(fullName)){
            user.setFullName(fullName);
        }
        if(!userIdField.getText().toString().equals(userId)){
            user.setEmail(userId);
        }
    }

}