package com.example.knowwealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    TextView skipNext;
    CheckBox selected;

    // Initializing local storage
    ArrayList<BankAccount> accounts;


    // RecyclerView elements for the accounts list
    RecyclerView accountsList;
    RecyclerView.LayoutManager  layoutManager;
    RecyclerView.Adapter adapter;

    // User holder
    User user;

    // Global variables holders
    boolean processingCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_link_demo);
        // Initializing user and global variables and storage
        accounts = new ArrayList<>();
        user = LoginActivity.user;
        //processingCompleted = user.getProcessingCompleted();

        // Connecting screen elements
        link = findViewById(R.id.linkBtn);
        accountsList = findViewById(R.id.bankAccountsList);
        skipNext = findViewById(R.id.skipNextBankLink);
        selected = findViewById(R.id.accountSelect);

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
                BankAccount account = new BankAccount(name, type, formatCurrency(balance));
                accounts.add(account);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ArrayList<String> test = new ArrayList<>();
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    for (int i = 0; i < accounts.size(); i++) {
                        if (accounts.get(i).getSelected()){
                            test.add(accounts.get(i).getName());
                        }
                    }
                    String names = "";
                    for (int i = 0; i < test.size(); i++) {
                        names += names;
                    }
                    if (!names.isEmpty()){
                        Toast.makeText(BankLinkDemo.this, "you selected " + names,Toast.LENGTH_SHORT);
                    }else{
                        Toast.makeText(BankLinkDemo.this, "please select one",Toast.LENGTH_SHORT);
                    }

                } catch (Exception e) {

                }

            }
        });


    }

    //----------------------------------------------------------------------------------------------------------------  HELPER METHODS

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
        DecimalFormat formatter = new DecimalFormat("-$ ###,###,##0.00");
        return formatter.format(Float.parseFloat(number));
    }
}
