package com.example.knowwealth;

public class BankAccount {

    // Member fields
    final String name;
    final String type;
    String balance;
    Boolean selected = false;

    // Constructor
    BankAccount(String name, String type, String balance) {
        this.name = name;
        this.type = type;
        this.balance = balance;
    }

    // Accessors
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

}
