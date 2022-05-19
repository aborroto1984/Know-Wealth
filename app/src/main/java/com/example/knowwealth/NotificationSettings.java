package com.example.knowwealth;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.media.RatingCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class NotificationSettings extends AppCompatActivity {

    SwitchMaterial notifyOnOff;
    Button days1;
    Button days2;
    Button days3;
    Button days4;
    Button apply;
    ImageView backArrow;
    User user = LoginActivity.user;
    Drawable mainBlue;
    Drawable mainWhite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        mainBlue = getDrawable(R.drawable.main_button);
        mainWhite = getDrawable(R.drawable.main_button_white);

        backArrow = findViewById(R.id.backArrow);

        days1 = findViewById(R.id.days_1);
        days2 = findViewById(R.id.days_2);
        days3 = findViewById(R.id.days_3);
        days4 = findViewById(R.id.days_4);
        notifyOnOff = findViewById(R.id.switch1);

        if(user.getSwitch1() == true) {
            notifyOnOff.setChecked(user.getSwitch1());
            if(notifyOnOff.isChecked()) {
                days1.setEnabled(true);
                days2.setEnabled(true);
                days3.setEnabled(true);
                days4.setEnabled(true);
            }
            days1.setBackground(user.getDays1());
            days2.setBackground(user.getDays2());
            days3.setBackground(user.getDays3());
            days4.setBackground(user.getDays4());
        }
        apply = findViewById(R.id.apply_button);

        notifyOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(notifyOnOff.isChecked()){
                    days1.setEnabled(true);
                    days2.setEnabled(true);
                    days3.setEnabled(true);
                    days4.setEnabled(true);
                }else{
                    days1.setEnabled(false);
                    days1.setBackground(mainWhite);
                    days2.setEnabled(false);
                    days2.setBackground(mainWhite);
                    days3.setEnabled(false);
                    days3.setBackground(mainWhite);
                    days4.setEnabled(false);
                    days4.setBackground(mainWhite);
                }
            }
        });
        days1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(days1.getBackground() == mainWhite) {
                    days1.setBackground(mainBlue);
                }else if(days1.getBackground() == mainBlue && days2.getBackground() == mainBlue){
                    days2.setBackground((mainWhite));
                    days3.setBackground((mainWhite));
                    days4.setBackground((mainWhite));
                }else{
                    days1.setBackground(mainWhite);
                }
            }
        });
        days2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(days2.getBackground() == mainWhite) {
                    days1.setBackground(mainBlue);
                    days2.setBackground(mainBlue);
                }else if(days2.getBackground() == mainBlue && days3.getBackground() == mainBlue){
                    days3.setBackground((mainWhite));
                    days4.setBackground((mainWhite));
                }else{
                    days2.setBackground((mainWhite));
                }
            }
        });
        days3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(days3.getBackground() == mainWhite) {
                    days1.setBackground(mainBlue);
                    days2.setBackground(mainBlue);
                    days3.setBackground(mainBlue);
                }else if(days3.getBackground() == mainBlue && days4.getBackground() == mainBlue){
                    days4.setBackground((mainWhite));
                }else{
                    days3.setBackground((mainWhite));
                }
            }
        });
        days4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(days4.getBackground() == mainWhite) {
                    days1.setBackground(mainBlue);
                    days2.setBackground(mainBlue);
                    days3.setBackground(mainBlue);
                    days4.setBackground(mainBlue);
                }else{
                    days4.setBackground((mainWhite));
                }
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setSwitch1(notifyOnOff.isChecked());
                user.setDays1(days1.getBackground());
                user.setDays2(days2.getBackground());
                user.setDays3(days3.getBackground());
                user.setDays4(days4.getBackground());
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotificationSettings.this, DashBoard.class));
            }
        });
    }

}