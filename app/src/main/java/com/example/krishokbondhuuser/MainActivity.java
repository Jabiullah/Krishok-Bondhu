package com.example.krishokbondhuuser;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import session.SharedPrefManager;

public class MainActivity extends AppCompatActivity {
    private TextView splashScreenHeader;
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //animated splash screen and
        splashScreenHeader = findViewById(R.id.sp_head);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacks(this);
                splashScreenHeader.setVisibility(View.VISIBLE);
                @SuppressLint("ResourceType") Animation animFade = AnimationUtils.loadAnimation(getApplicationContext(),R.drawable.fade_in);
                splashScreenHeader.startAnimation(animFade);
            }
        },1000);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // checking the status of OnBoardScreen Page
                if (SharedPrefManager.getInstance(MainActivity.this).isDisplayedOrNot()) {
                    finish();
                    startActivity(new Intent(MainActivity.this, OtpPage.class));
                    return;
                }
                else{
                    Intent onBoarding = new Intent(MainActivity.this,NavigationActivity.class);
                    startActivity(onBoarding);
                    finish();
                }
            }
        },2000);



    }
}