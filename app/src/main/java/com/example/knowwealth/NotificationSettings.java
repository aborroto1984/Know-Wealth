package com.example.knowwealth;

import androidx.annotation.ColorRes;
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

import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotificationSettings extends AppCompatActivity {

    SwitchMaterial notifyOnOff;
    Slider slider;
    Button apply;
    ImageView backArrow;
    User user = LoginActivity.user;
    DatabaseReference userDatabase= FirebaseDatabase.getInstance().getReferenceFromUrl("https://know-wealth-default-rtdb.firebaseio.com/").child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        backArrow = findViewById(R.id.backArrow);
        notifyOnOff = findViewById(R.id.switch1);
        slider = findViewById(R.id.days);

        if(user.getSwitch1() == true) {
            notifyOnOff.setChecked(user.getSwitch1());
            if(notifyOnOff.isChecked()) {
                slider.setEnabled(true);
                slider.setValue(user.getSliderPosition());
                slider.setThumbTintList(AppCompatResources.getColorStateList(this,R.color.main_color));
            }
        }
        apply = findViewById(R.id.apply_button);

        notifyOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(notifyOnOff.isChecked()){
                    slider.setEnabled(true);
                    slider.setThumbTintList(AppCompatResources.getColorStateList(NotificationSettings.this,R.color.main_color));
                }else{
                    slider.setEnabled(false);
                    slider.setValue(1);
                    slider.setThumbTintList(AppCompatResources.getColorStateList(NotificationSettings.this, com.google.android.gms.base.R.color.common_google_signin_btn_text_dark_disabled));
                }
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String notify;
                user.setSwitch1(notifyOnOff.isChecked());
                if (notifyOnOff.isChecked()){
                    notify = "true";
                }else{
                    notify = "false";
                }
                userDatabase.child(user.getuID() + "/Notification").setValue(notify);
                String sliderValue;
                sliderValue = Float.toString(slider.getValue()) ;
                userDatabase.child(user.getuID() + "/NotifyTime").setValue(sliderValue);
                user.setSliderPosition(slider.getValue());


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