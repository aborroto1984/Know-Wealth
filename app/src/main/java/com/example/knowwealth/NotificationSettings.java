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

public class NotificationSettings extends AppCompatActivity {

    SwitchMaterial notifyOnOff;
    Slider slider;
    Button apply;
    ImageView backArrow;
    User user = LoginActivity.user;

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
                user.setSwitch1(notifyOnOff.isChecked());
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