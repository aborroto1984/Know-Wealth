package com.example.knowwealth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.messaging.FirebaseMessaging;

import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    final String TAG = "LoginActivity";
    public static User user;
    EditText userNameInput;

    DatabaseReference userDatabase= FirebaseDatabase.getInstance().getReferenceFromUrl("https://know-wealth-default-rtdb.firebaseio.com/").child("users");
    String processing = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Button loginButton = findViewById(R.id.button);
        final EditText passwordInput = findViewById(R.id.password);
        userNameInput = findViewById(R.id.UserName);

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                signIn(userNameInput.getText().toString(),passwordInput.getText().toString());
            }
        });

        TextView registerLink = findViewById(R.id.signUpText);
        registerLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(LoginActivity.this, CreateAccount.class));
            }
        });
    }

    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser authUser = mAuth.getCurrentUser();

                            user = new User(authUser.getUid());
                            userDatabase.child(authUser.getUid()).child("Email").setValue(userNameInput.getText().toString());

                            userDatabase.child(authUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                    User.snapshot = snapshot;
                                    if(snapshot.child("Processing Completed").exists()) {
                                        processing = snapshot.child("Processing Completed").getValue().toString();
                                    }else{
                                        processing = "false";
                                    }
                                    if(snapshot.child("Notification").exists()){
                                        String tempSwitch = snapshot.child("Notification").getValue().toString();
                                        if(tempSwitch.equals("true")){
                                            user.setSwitch1(true);
                                            user.setSliderPosition(Float.parseFloat(snapshot.child("NotifyTime").getValue().toString()));
                                        }else{
                                            user.setSwitch1(false);
                                        }
                                    }
                                    if (processing.equals("true")) {
                                        user.setProcessingCompleted(true);
                                    } else {
                                            user.setProcessingCompleted(false);
                                            user.setCurrentActivity("utility");
                                    }
                                    user.setEmail(snapshot.child("Email").getValue().toString());
                                    user.setFullName(snapshot.child("Full Name").getValue().toString());
                                    //budgets
                                    for (DataSnapshot b : snapshot.child("Budgets").getChildren()) {
                                        user.budgets.add(b.getValue().toString());
                                    }
                                    Month month = Month.JANUARY;
                                    String monthStr = month.name();
                                    for (int i = 0; i < 12; i++) {
                                        for (DataSnapshot j : snapshot.child(monthStr).getChildren()) {
                                            if(j.getKey().equals("Credit Cards")){
                                                for(DataSnapshot k : snapshot.child(monthStr + "/Credit Cards").getChildren()){
                                                    user.creditCards.add(new User.UtilDate(monthStr, k.getKey(),k.child("Due Date").getValue().toString(),k.child("Paid").getValue().toString()));
                                                };
                                            }
                                            if(j.getKey().equals("Subscriptions")){
                                                for(DataSnapshot k : snapshot.child(monthStr + "/Subscriptions").getChildren()){
                                                    user.subscriptions.add(new User.UtilDate(monthStr, k.getKey(),k.child("Due Date").getValue().toString(),k.child("Paid").getValue().toString()));
                                                };
                                            }
                                            if(j.getKey().equals("Utilities")){
                                                for(DataSnapshot k : snapshot.child(monthStr + "/Utilities").getChildren()){
                                                    user.utilities.add(new User.UtilDate(monthStr, k.getKey(),k.child("Due Date").getValue().toString(),k.child("Paid").getValue().toString()));
                                                };
                                            }
                                        }
                                        month = month.plus(1);
                                        monthStr = month.name();
                                    }
                                    if (processing.equals("false")) {
                                        startActivity(new Intent(LoginActivity.this, ProcessingScreens.class));
                                    }else{
                                        startActivity(new Intent(LoginActivity.this, DashBoard.class));
                                    }
                    //                expenses = new ArrayList<>();
                                }


                                @Override
                                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                                    //add toast for error message
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, CreateAccount.class));
                        }

                    }
                });
        }
}