package com.example.krishokbondhuuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import session.SharedPrefManager;

public class OtpPage extends AppCompatActivity {
    TextView phnNum;
    Button btnAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_page);

        phnNum = findViewById(R.id.PhoneNumber);
        btnAuth= findViewById(R.id.OtpWork);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, homePage.class));
            return;
        }

        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phnNum.getText().toString().trim().length()!=10){
                    phnNum.requestFocus();
                    phnNum.setError("আপনার ১০ ডিজিটের ফোন নাম্বার লিখুন");
                    phnNum.setText("");
                }else{
                    String phnValue = "+880"+phnNum.getText().toString().trim();
                    Bundle bundle = new Bundle();
                    bundle.putString("phone",phnValue);
                    Intent intentAuth = new Intent(OtpPage.this, Auth.class);
                    intentAuth.putExtras(bundle);
                    startActivity(intentAuth);
                }

            }
        });

    }
}