package com.example.knowwealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class BankLinkDemo extends AppCompatActivity {

    // Initializing screen elements
    Button link;
    TextView nameRow;
    TextView typeRow;
    TextView balanceRow;
    TextView skipNext;
    TextView pageTitle;
    TextView instructions;
    CheckBox selected;


    // Initializing local storage
    ArrayList<BankAccount> accounts;


    // RecyclerView elements for the accounts list
    RecyclerView accountsList;
    RecyclerView.LayoutManager  layoutManager;
    RecyclerView.Adapter adapter;

    // User holder
    User user;

    // Switch button state
    boolean readyToLink = false;

    // Global variables holders
    boolean processingCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_link_demo);

        // Initializing user and global variables and storage
        accounts = new ArrayList<>();
        //user = LoginActivity.user;
        user = new User("Alfredo Borroto", "aborroto1984@gmail.com");

        // Connecting screen elements
        link = findViewById(R.id.linkBtn);
        if(!readyToLink){ link.setText("Fetch Accounts");}

        accountsList = findViewById(R.id.bankAccountsList);
        skipNext = findViewById(R.id.skipNextBankLink);
        selected = findViewById(R.id.accountSelect);
        pageTitle = findViewById(R.id.bankLinkTitle);
        pageTitle.setText("Hello " + user.getFirstName());
        nameRow = findViewById(R.id.nameRow);
        typeRow = findViewById(R.id.typeRow);
        balanceRow = findViewById(R.id.balanceRow);
        instructions = findViewById(R.id.instructions);

        // Setting up the accounts list recyclerView
        adapter = new BankLinkAdapter(this, accounts, selected);
        layoutManager = new LinearLayoutManager(this);
        accountsList.setAdapter(adapter);
        accountsList.setLayoutManager(layoutManager);

        try {
            JSONObject bankInfo = new JSONObject(getDataFromAsset());
            JSONArray accountsInfo = bankInfo.getJSONArray("accounts");

            for (int i = 0; i < accountsInfo.length(); i++) {

                JSONObject accountData = accountsInfo.getJSONObject(i);
                String name = accountData.getString("name");
                String str = accountData.getString("subtype");
                String type = str.substring(0, 1).toUpperCase() + str.substring(1);
                JSONObject balances = accountData.getJSONObject("balances");
                String balance = balances.getString("current");
                if (type.equals("Checking") || type.equals("Credit card")) {
                    BankAccount account = new BankAccount(name, type, formatCurrency(balance));
                    accounts.add(account);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //----------------------------------------------------------------------------------------------------------------  BUTTONS
        skipNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setProcessingCompleted(true);
                startActivity(new Intent(BankLinkDemo.this, DashBoard.class));
            }
        });
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!readyToLink){
                    loadAccounts();
                }
                else{
                    user.setProcessingCompleted(true);
                    startActivity(new Intent(BankLinkDemo.this, DashBoard.class));
                }
            }
        });


    }

    //----------------------------------------------------------------------------------------------------------------  HELPER METHODS

    // Loading accounts page
    public void loadAccounts(){
        readyToLink = !readyToLink;
        pageTitle.setText("Select Accounts");
        nameRow.setVisibility(View.VISIBLE);
        typeRow.setVisibility(View.VISIBLE);
        balanceRow.setVisibility(View.VISIBLE);
        accountsList.setVisibility(View.VISIBLE);
        instructions.setVisibility(View.GONE);
        skipNext.setText("Done");
        link.setText("Link Accounts");
    }

    // Loading instructions page
    public void loadInstructions(){
        readyToLink = !readyToLink;
        pageTitle.setText("Hello " + user.getFirstName());
        nameRow.setVisibility(View.GONE);
        typeRow.setVisibility(View.GONE);
        balanceRow.setVisibility(View.GONE);
        accountsList.setVisibility(View.GONE);
        instructions.setVisibility(View.VISIBLE);
        skipNext.setText("Skip");
        link.setText("Fetch Accounts");
    }

    // Overwriting click event for the phone back button
    @Override
    public void onBackPressed(){
        if (!readyToLink){
            user.setCurrentActivity("creditCards");
            startActivity(new Intent(BankLinkDemo.this, ProcessingScreens.class));
        }else{

        }
    }

    // Method to reload the activity
    private void refreshActivity(){
        finish();
        startActivity(getIntent());
    }

    private String getDataFromAsset() {
        String json = null;
        try {
            InputStream iStream = getAssets().open("transactionsDemo.json");
            int sizeOfFile = iStream.available();
            byte[] bufferData = new byte[sizeOfFile];
            iStream.read(bufferData);
            iStream.close();
            json = new String(bufferData,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    private static String formatCurrency(String number){
        DecimalFormat formatter = new DecimalFormat("$ ###,###,##0.00");
        return formatter.format(Float.parseFloat(number));
    }
}
