package com.example.knowwealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.BreakIterator;

public class AccountEdit extends AppCompatActivity {
    FirebaseUser fbUser;
    User user = LoginActivity.user;
    Button appbtn;
    Button resetBtn;
    EditText fullNameField;
    EditText userIdField;
    DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://know-wealth-default-rtdb.firebaseio.com/").child("users/" + user.getuID());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);

         appbtn = findViewById(R.id.apply_button);
         resetBtn = findViewById(R.id.password_button);
         fullNameField = findViewById(R.id.fullName);
         userIdField = findViewById(R.id.userIdCreateAccount);
         fbUser = FirebaseAuth.getInstance().getCurrentUser();

        appbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    changeAccountInfo();
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = user.getEmail();

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AccountEdit.this, "Password Reset E-Mail Sent.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });

        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(AccountEdit.this, DashBoard.class));
            }
        });
    }

    private void changeAccountInfo(){
        String fullName = user.getFullName();
        String userId = user.getEmail();

        if (!fullNameField.getText().toString().equals(fullName) && !fullNameField.getText().toString().isEmpty()){
            user.setFullName(fullNameField.getText().toString());
            userDatabase.child("Full Name").setValue(fullNameField.getText().toString());
            userDatabase.child("First Name").setValue(user.getFirstName());
            Toast.makeText(AccountEdit.this, "Name updated.", Toast.LENGTH_SHORT).show();
        }
        if(!userIdField.getText().toString().equals(userId) && !userIdField.getText().toString().isEmpty()){
            user.setEmail(userIdField.getText().toString());
            userDatabase.child("Email").setValue(userIdField.getText().toString());
            fbUser.updateEmail(userIdField.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AccountEdit.this, "User ID updated.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}