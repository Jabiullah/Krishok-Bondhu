package com.example.krishokbondhuuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.format.TextStyle;
import java.util.ArrayList;

import Model.User;
import session.SharedPrefManager;
import java.util.Calendar;
import java.util.Locale;


public class homePage extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ImageButton field, buyingProduct, seba;
    private ImageView   rain,wind,sun,cloud;

    private TextView    weather,sky,info;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //homepage Slider
        ImageSlider i = findViewById(R.id.image_slider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.homesliderpic, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.homesliderpicnext, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.homesliderpiclast, ScaleTypes.FIT));

        i.setImageList(slideModels,ScaleTypes.FIT);
        i.startSliding(4000);        // time delay

        //bottomNav View
        bottomNavigationView = findViewById(R.id.bottomNavHome);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.notification:
                        Intent onNotification = new Intent(homePage.this,Notification.class);
                        startActivity(onNotification);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
                        finish();
                        return true;
                    case R.id.profile:
                        Intent onProfile = new Intent(homePage.this,Profile.class);
                        startActivity(onProfile);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
                        finish();
                        return true;
                }
                return false;
            }
        });

        //imageview
        field           = findViewById(R.id.field);
        buyingProduct   = findViewById(R.id.buyProduct);
        seba            = findViewById(R.id.necessaryInfo);

        //image of weather
        sun             = findViewById(R.id.sunnyWeather);
        wind            = findViewById(R.id.windyWeather);
        rain            = findViewById(R.id.rainyWeather);
        cloud           = findViewById(R.id.cloudWeather);

        weather         = findViewById(R.id.temperature);
        sky             = findViewById(R.id.skyUpdate);
        info            = findViewById(R.id.TodayInfo);

        animationInfo();

        // data is here of sky and weather
        String Temp         = "৩০° সেলসিয়াস";
        String skySituation = "আকাশ মেঘাছন্ন";// আকাশ রৌদ্রময় - বাতাসময় - বৃষ্টির দিন

        weather.setText(Temp);
        sky.setText(skySituation);

        //image toggle
        if(skySituation.contains("আকাশ মেঘাছন্ন")){
            cloud.setVisibility(View.VISIBLE);
            sun.setVisibility(View.GONE);
            wind.setVisibility(View.GONE);
            rain.setVisibility(View.GONE);
        }else if(skySituation.contains("আকাশ রৌদ্রময়")){
            cloud.setVisibility(View.GONE);
            sun.setVisibility(View.VISIBLE);
            wind.setVisibility(View.GONE);
            rain.setVisibility(View.GONE);
        }else if(skySituation.contains("বাতাসময়")){
            cloud.setVisibility(View.GONE);
            sun.setVisibility(View.GONE);
            wind.setVisibility(View.VISIBLE);
            rain.setVisibility(View.GONE);
        }else {
            cloud.setVisibility(View.GONE);
            sun.setVisibility(View.GONE);
            wind.setVisibility(View.GONE);
            rain.setVisibility(View.VISIBLE);
        }

        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent onField = new Intent(homePage.this,filedPage.class);
                startActivity(onField);
            }
        });
        buyingProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent onProduct = new Intent(homePage.this,productPage.class);
                startActivity(onProduct);
            }
        });
        seba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent onSeba = new Intent(homePage.this,sebaHistoryDisplay.class);
                startActivity(onSeba);
            }
        });
    }
    private void animationInfo(){
        Animation animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);
        info.startAnimation(animSlideDown);
    }
}