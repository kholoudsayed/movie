package com.example.kholoud.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;


// it is Just For Show the Logo For app

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
//to Open  Logo At frist
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent dashIntent = new Intent(MainActivity.this, Dashboard.class);
                startActivity(dashIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
