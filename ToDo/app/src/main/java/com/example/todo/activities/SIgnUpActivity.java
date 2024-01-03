package com.example.todo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.todo.helpers.PrefManager;
import com.example.todo.R;

public class SIgnUpActivity extends AppCompatActivity {

    EditText usernameEt;
    Button signUpBtn;

    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        usernameEt = findViewById(R.id.username_et);
        signUpBtn = findViewById(R.id.sign_up_btn);

        prefManager= new PrefManager(this);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEt.getText().toString().trim();
                if(username.isEmpty()){
                    usernameEt.setError("Please Enter Username!");
                    usernameEt.requestFocus();
                    return;
                }else{
                    prefManager.setUsername(username);
                    prefManager.setNotFirstTime();
                    startActivity(new Intent(SIgnUpActivity.this, DashboardActivity.class));
                    finish();
                }
            }
        });

    }
}