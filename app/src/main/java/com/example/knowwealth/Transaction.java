package com.example.knowwealth;

import java.util.ArrayList;

public class Transaction {
    final String id;
    final String name;
    final String amount;
    final String date;
    String category;

    // Accessors
    public String getId() {return id;}
    public String getName() {return name;}
    public String getAmount() {return amount;}
    public String getDate() {return date;}
    public String getCategory() {return category;}

    // Constructor
    Transaction(String id, String name, String amount, String dateOld, ArrayList categories) {
        this.id = id;
        this.name = name;
        this.amount = amount;

        // Converting to current date
        String[] currentDate;
        currentDate = dateOld.split("-");
        String[] months = new String[]{ "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        currentDate[0] = "2022";

        Integer year = 0;
        Integer month = 0;
        Integer day = 0;

        day = Integer.valueOf(currentDate[2]) - 2;
        if (day <= 0){ month -= 1; day += 30; }
        currentDate[2] = String.valueOf((day));

        month = Integer.valueOf(currentDate[1]) - 6 + month;
        if (month <= 0){ year -= 1; month += 12; }
        currentDate[1] = months[month - 1];

        year = Integer.valueOf(currentDate[0]) + year;
        currentDate[0] = String.valueOf((year));

        this.date = currentDate[2] + " " + currentDate[1] + " " + currentDate[0];

        if (categories.contains("Travel") || categories.contains("Taxi") || categories.contains("Airlines and Aviation Services")){
            this.category = "Transportation";
        }else if (categories.contains("Recreation") || categories.contains("Entertainment")){
            this.category = "Entertainment";
        }else if (categories.contains("Fast Food") || categories.contains("Coffee Shop")){
            this.category = "Food - Drink";
        }else if (categories.contains("Restaurants") && !categories.contains("Coffee Shop") && !categories.contains("Fast Food")){
            this.category = "Restaurant";
        }else if (categories.contains("Fuel") || categories.contains("Gas Station")){
            this.category = "Fuel";
        }
    }
}
// 2021-09-22 date format
// subtract 6 to the month to make it current
// subtract 2 to the day to make it current