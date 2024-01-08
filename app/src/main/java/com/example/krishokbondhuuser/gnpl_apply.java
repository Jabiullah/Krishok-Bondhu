package com.example.krishokbondhuuser;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class gnpl_apply extends AppCompatActivity {
    private Timer timer;
    private Timer timerIntent;
    ProgressBar progressBar;
    TextView msg;
    ImageView img;
    @SuppressLint("MissingInflatedId")


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gnpl_apply);
        progressBar = findViewById(R.id.simpleProgressBar_gnpl);
        img         = findViewById(R.id.success_gnpl);
        msg         = findViewById(R.id.msgDisplay_gnpl);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacks(this);
                progressBar.setVisibility(View.GONE);
                display();
            }
        },3000); // 3 sec delay


    }
    private void display(){
        img.setVisibility(View.VISIBLE);
        msg.setVisibility(View.VISIBLE);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // checking the status of OnBoardScreen Page
                Intent onBoarding = new Intent(gnpl_apply.this,homePage.class);
                startActivity(onBoarding);
                finish();
            }
        },1000);

    }
}