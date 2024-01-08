package com.example.krishokbondhuuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import Model.User;
import session.SharedPrefManager;

public class Profile extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    TextView name,phone,address,birthplace,nidNumber, krishi,abohawa,mati;
    ImageView logout;
    TextView sessionOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Textview
        name        = findViewById(R.id.userName);
        phone       = findViewById(R.id.userPhone);
        address     = findViewById(R.id.LocationData);
        birthplace  = findViewById(R.id.PlaceOfBirthData);
        nidNumber   = findViewById(R.id.NID_Data);

        krishi      = findViewById(R.id.krishiOdhidoptor);
        mati        = findViewById(R.id.matiOdhidoptor);
        abohawa     = findViewById(R.id.weatherOdhidoptor);

        //logout
        logout      = findViewById(R.id.sessionOut);
        sessionOut  = findViewById(R.id.sessionOutText);

        // sharedPref data
        User user = SharedPrefManager.getInstance(this).getUser();

        name.setText(user.getName());
        phone.setText(user.getPhone());
        address.setText(user.getAddress());
        birthplace.setText(user.getUser_birthPlace()); //
        nidNumber.setText(user.getNID());

        krishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:16123"));
                startActivity(intent);
            }
        });

        abohawa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+880248112456"));
                startActivity(intent);
            }
        });

        mati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+880255100035"));
                startActivity(intent);
            }
        });

        //logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionExit();
            }
        });
        sessionOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionExit();
            }
        });

        //bottomNav View
        bottomNavigationView = findViewById(R.id.bottomNavProfile);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent onHome = new Intent(Profile.this,homePage.class);
                        startActivity(onHome);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
                        finish();
                        return true;

                    case R.id.notification:
                        Intent onNotification = new Intent(Profile.this,Notification.class);
                        startActivity(onNotification);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
                        finish();
                        return true;

                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });
    }
    //logout Function
    private void sessionExit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle("আপনি লগ আউট করতে চান ?").setMessage("এটি আপনার সমস্ত লগইন তথ্যও পরিষ্কার করবে")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        SharedPrefManager.getInstance(getApplicationContext()).logout();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }

}