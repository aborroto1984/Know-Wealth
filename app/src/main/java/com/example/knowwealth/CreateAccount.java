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

public class CreateAccount extends AppCompatActivity {
    private FirebaseAuth mAuth;
    final String TAG = "CreateAccount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance();

        Button createAccountButton = findViewById(R.id.createAccountButton);
        final EditText userIdCreateAccount = findViewById(R.id.userIdCreateAccount);
        final EditText passwordCreateAccount1 = findViewById(R.id.passwordCreateAccount1);
        final EditText passwordVerify = findViewById(R.id.passwordCreateAccount2);
        final EditText fullName = findViewById(R.id.fullName);

        createAccountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
//                if(userIdCreateAccount.getText().toString().isEmpty()){
//                    Toast.makeText(CreateAccount.this, "Email cannot be blank.",
//                            Toast.LENGTH_SHORT).show();
//                }else if(passwordCreateAccount1.getText().toString().isEmpty()) {
//                    Toast.makeText(CreateAccount.this, "Password cannot be blank.",
//                            Toast.LENGTH_SHORT).show();
//                }else if(!passwordCreateAccount1.getText().toString().equals(passwordVerify.getText().toString())){
//                    Toast.makeText(CreateAccount.this, "Passwords do not match.",
//                            Toast.LENGTH_LONG).show();
//                } else{
//                    userName = new User(fullName.getText().toString(), userIdCreateAccount.getText().toString());
//                    createAccount(userIdCreateAccount.getText().toString(), passwordCreateAccount1.getText().toString());
//                }

                startActivity(new Intent(CreateAccount.this, ProcessingScreens.class));
            }
        });

        TextView signInLink = findViewById(R.id.SignInText);
        signInLink.setOnClickListener(new View.OnClickListener(){
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
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(CreateAccount.this, "Authentication Success." + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();

                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccount.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        }

    }



