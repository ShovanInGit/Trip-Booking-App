package com.example.escape;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.escape.onboarding.Onboarding;

public class SplashScreen extends AppCompatActivity {

    public  static  int SPLASH_SCREEN=5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this, Onboarding.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}