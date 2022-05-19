package com.example.knowwealth;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.media.RatingCompat;
import android.view.View;
import android.widget.Button;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class NotificationSettings extends AppCompatActivity {

    SwitchMaterial notifyOnOff;
    Button days1;
    Button days2;
    Button days3;
    Button days4;
    User user = LoginActivity.user;
    Drawable mainBlue;
    Drawable mainWhite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        mainBlue = getDrawable(R.drawable.main_button);
        mainWhite = getDrawable(R.drawable.main_button_white);
        days1 = findViewById(R.id.days_1);
        days2 = findViewById(R.id.days_2);
        days3 = findViewById(R.id.days_3);
        days4 = findViewById(R.id.days_4);
        notifyOnOff = findViewById(R.id.switch1);
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
                }else if(days2.getBackground() == mainBlue && days2.getBackground() == mainBlue){
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
    }
}