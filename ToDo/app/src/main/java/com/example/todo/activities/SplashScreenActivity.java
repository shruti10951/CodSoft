package com.example.todo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.todo.helpers.PrefManager;
import com.example.todo.R;

public class SplashScreenActivity extends AppCompatActivity {
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        prefManager = new PrefManager(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(prefManager.getUsername() != null){
                    startActivity(new Intent(SplashScreenActivity.this, DashboardActivity.class));
                }else{
                    startActivity(new Intent(SplashScreenActivity.this, StartActivity.class));
                }
                finish();
            }
        }, 5000);
    }
}