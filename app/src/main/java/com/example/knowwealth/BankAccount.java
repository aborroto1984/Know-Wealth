package com.example.knowwealth;

import java.util.ArrayList;

public class BankAccount {

    // Member fields
    final String name;
    final String type;
    final String id;
    private String balance;
    private Boolean selected;
    private Boolean expanded;
    ArrayList<Transaction> transactions;

    // Constructor
    BankAccount(String name, String type, String balance, String id) {
        this.name = name;
        this.type = type;
        this.balance = balance;
        this.id = id;
        this.transactions = new ArrayList<>();
        this.expanded = false;
        this.selected = false;
    }

    // Accessors
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public String getBalance() {
        return balance;
    }
    public Boolean getSelected() {return selected;}

    // Mutators
    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }
}
