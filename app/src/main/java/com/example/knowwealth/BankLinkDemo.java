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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BankLinkDemo extends AppCompatActivity {

    // Initializing screen elements
    Button link;
    TextView nameRow;
    TextView typeRow;
    TextView balanceRow;
    TextView skipNext;
    TextView pageTitle;
    TextView instructions;
    TextView showHide;
    Boolean showing;
    CheckBox selected;

    // Initializing local storage
    ArrayList<BankAccount> accounts;


    // RecyclerView elements for the accounts list
    RecyclerView accountsList;
    RecyclerView transactionsList;
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
        user = LoginActivity.user;
        //user = new User("Alfredo Borroto", "aborroto1984@gmail.com");

        // Show - hide all accounts
        showing = true;

        // Connecting screen elements
        link = findViewById(R.id.linkBtn);
        if(!readyToLink){ link.setText("Fetch Accounts");}

        accountsList = findViewById(R.id.bankAccountsList);
        transactionsList = findViewById(R.id.accountTrasactions);
        skipNext = findViewById(R.id.skipNextBankLink);
        selected = findViewById(R.id.accountSelect);
        pageTitle = findViewById(R.id.bankLinkTitle);
        pageTitle.setText("Hello " + user.getFirstName());
        nameRow = findViewById(R.id.nameRow);
        typeRow = findViewById(R.id.typeRow);
        balanceRow = findViewById(R.id.balanceRow);
        instructions = findViewById(R.id.instructions);
        showHide = findViewById(R.id.showAccounts);

        // Setting up the accounts list recyclerView
        adapter = new BankLinkAdapter(this, accounts, transactionsList, selected, false);
        layoutManager = new LinearLayoutManager(this);
        accountsList.setAdapter(adapter);
        accountsList.setLayoutManager(layoutManager);


        try {
            JSONObject bankInfo = new JSONObject(getDataFromAsset());
            JSONArray accountsInfo = bankInfo.getJSONArray("accounts");

            for (int i = 0; i < accountsInfo.length(); i++) {

                JSONObject accountData = accountsInfo.getJSONObject(i);
                String id = accountData.getString("account_id");
                String name = accountData.getString("name");
                String str = accountData.getString("subtype");
                String type = str.substring(0, 1).toUpperCase() + str.substring(1);
                JSONObject balances = accountData.getJSONObject("balances");
                String balance = balances.getString("current");
                BankAccount account = new BankAccount(name, type, formatCurrency(balance), id);
                if (type.equals("Checking") || type.equals("Credit card")) {
                    account.setShow(true);
                }
                accounts.add(account);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject bankInfo = new JSONObject(getDataFromAsset());
            JSONArray transactions = bankInfo.getJSONArray("transactions");

            for (int i = 0; i < transactions.length(); i++) {

                JSONObject transactionData = transactions.getJSONObject(i);
                String id = transactionData.getString("account_id");
                String name = transactionData.getString("name");

                String amount = transactionData.getString("amount");
                String date = transactionData.getString("date");
                JSONArray categories = transactionData.getJSONArray("category");

                ArrayList<String> transactionCategories = new ArrayList<>();

                for (int j = 0; j < categories.length(); j++) {
                    transactionCategories.add(categories.getString(j));
                }

                Transaction transaction = new Transaction(id, name, formatCurrency(amount), date, transactionCategories);
                for (int k = 0; k < accounts.size(); k++) {
                    if (accounts.get(k).getId().equals(transaction.getId())){
                        accounts.get(k).transactions.add(transaction);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //----------------------------------------------------------------------------------------------------------------  BUTTONS
        showHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accounts.size() > 0 && showing){
                    for (int i = 0; i < accounts.size(); i++) {
                        accounts.get(i).setShow(true);
                    }
                    showHide.setText("View only linkable accounts");
                }else{
                    for (int i = 0; i < accounts.size(); i++) {
                        if (accounts.get(i).getType().equals("Checking") || accounts.get(i).getType().equals("Credit card")){
                            accounts.get(i).setShow(true);
                        }else{
                            accounts.get(i).setShow(false);
                        }
                    }
                    showHide.setText("Show all accounts");
                }
                showing = !showing;
                adapter.notifyDataSetChanged();
            }
        });

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
                    DateFormat dateFormat = new SimpleDateFormat("MMMM");
                    Date date = new Date();
                    String month = dateFormat.format(date);

                    if (accounts.size() > 0){
                        for (int i = 0; i < accounts.size(); i++) {
                            if (accounts.get(i).getSelected() && (accounts.get(i).getType().equals("Checking") || accounts.get(i).getType().equals("Credit card"))){
                                ArrayList<Transaction> temp = accounts.get(i).transactions;
                                for (int j = 0; j < temp.size(); j++) {
                                    String transDate = temp.get(j).getDate();
                                    String[] getMonth = transDate.split(" ");
                                    if (getMonth[1].equals(month)){
                                        String name = temp.get(j).getCategory();
                                        String amount = temp.get(j).getAmount();
                                        String amountNum = amount.replaceAll("[$,,,-]", "");
                                        boolean contains = false;
                                        int index = 0;
                                        for (int k = 0; k < user.expenses.size(); k++){
                                            if(user.getExpenses().get(k).getName().equals(name)){
                                                contains = true;
                                                index = k;
                                            }
                                        }
                                        if(contains){
                                            user.getExpenses().get(index).AddExpense(amountNum);
                                        }else{
                                            if (name != null){
                                                user.expenses.add(new User.Expense(name, amountNum));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    user.setProcessingCompleted(true);
                    user.userDatabase.child(User.getuID()).child("Processing Completed").setValue("true");
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
        showHide.setVisibility(View.VISIBLE);
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
        showHide.setVisibility(View.GONE);
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
            loadInstructions();
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
