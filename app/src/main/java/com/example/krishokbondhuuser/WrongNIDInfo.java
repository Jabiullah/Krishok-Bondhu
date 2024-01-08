package com.example.krishokbondhuuser;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class WrongNIDInfo extends AppCompatActivity {

    private Button backToStart;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_nidinfo);

        backToStart = findViewById(R.id.BackStart);

        backToStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBackToStart = new Intent(WrongNIDInfo.this, OtpPage.class);
                startActivity(intentBackToStart);
                finish();
            }
        });
    }
}