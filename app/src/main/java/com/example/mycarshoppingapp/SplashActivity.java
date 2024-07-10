package com.example.mycarshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mycarshoppingapp.Registerationprocess.EnterNumber;
import com.example.mycarshoppingapp.Utilities.FirebaseUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread t = new Thread(){
            @Override
            public  void run()
            {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //Intent
                    if(FirebaseUtil.isLoggedIn()){
                        startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    }else{
                        startActivity(new Intent(SplashActivity.this, EnterNumber.class));
                    }
                    finish();
                }
            }

        };
        t.start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },3000);

    }
}