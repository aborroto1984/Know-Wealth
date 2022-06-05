package com.example.knowwealth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Locale;

public class CreateAccount extends AppCompatActivity {
    private FirebaseAuth mAuth;
    final String TAG = "CreateAccount";
    String userEmail;
    String userFullName;
    public static User userName;
    Button createAccountButton;

    DatabaseReference userDatabase;

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance();

        createAccountButton = findViewById(R.id.createAccountButton);
        final EditText userIdCreateAccount = findViewById(R.id.userIdCreateAccount);
        final EditText passwordCreateAccount1 = findViewById(R.id.passwordCreateAccount1);
        final EditText passwordVerify = findViewById(R.id.passwordCreateAccount2);
        final EditText fullName = findViewById(R.id.fullName);

        createAccountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(userIdCreateAccount.getText().toString().isEmpty()){
                    Toast.makeText(CreateAccount.this, "Email cannot be blank.",
                            Toast.LENGTH_SHORT).show();
                }else if(passwordCreateAccount1.getText().toString().isEmpty()) {
                    Toast.makeText(CreateAccount.this, "Password cannot be blank.",
                            Toast.LENGTH_SHORT).show();
                }else if(!passwordCreateAccount1.getText().toString().equals(passwordVerify.getText().toString())){
                    Toast.makeText(CreateAccount.this, "Passwords do not match.",
                            Toast.LENGTH_LONG).show();
                } else{
                    userEmail = userIdCreateAccount.getText().toString();
                    userFullName = fullName.getText().toString();
                    userName = new User(userFullName, userEmail);
                    createAccount(userEmail, passwordCreateAccount1.getText().toString());

                }
            }
        });

        TextView signInLink = findViewById(R.id.SignInText);
        signInLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(CreateAccount.this, LoginActivity.class));
            }
        });
        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(CreateAccount.this, LoginActivity.class));
            }
        });
    }

    public void createAccount(String email, String password){
        //todo add validation for password and email

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Setup calander information to fill out json tree
                            String month;

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(CreateAccount.this, "Authentication Success." + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();

                            userDatabase= FirebaseDatabase.getInstance().getReferenceFromUrl("https://know-wealth-default-rtdb.firebaseio.com/").child("users");

                            userDatabase.child(mAuth.getUid() + "/Email").setValue(userEmail);
                            userDatabase.child(mAuth.getUid() + "/Full Name").setValue(userFullName);
                            userDatabase.child(mAuth.getUid() + "/First Name").setValue(User.getFirstName());
                            Month month1 = Month.JANUARY;
                            for (int i = 0; i < 12; i++) {
                                month = month1.toString();
                                fillInFirebase(month);
                                month1 = month1.plus(1);
                            }

                            startActivity(new Intent(CreateAccount.this, LoginActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccount.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
        private void fillInFirebase(String month){
            userDatabase.child(mAuth.getUid() + "/" + month + "/Credit Cards").setValue("0");
            userDatabase.child(mAuth.getUid() + "/" + month + "/Subscriptions").setValue("0");
            userDatabase.child(mAuth.getUid() + "/" + month + "/Utilities").setValue("0");

        }

    }



