package com.example.krishokbondhuuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import session.SharedPrefManager;

public class NavigationActivity extends AppCompatActivity {
    ViewPager slideViewPager;
    LinearLayout doIndicator;
    ViewPagerAdapter viewPagerAdapter;

    Button nextButton;
    TextView[] dots;

    private String OnBoardStatus = "True" ;

    ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setDoIndicator(position);
            if(position==2){
                nextButton.setText("শুরু করা যাক");
            }else{
                nextButton.setText("পরবর্তী");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    //getter method of 'OnBoardStatus' value.
    public String getOnBoardStatus(){
        return OnBoardStatus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);


        //storing the user in shared preferences
        SharedPrefManager.getInstance(getApplicationContext()).onBoardCheck();

        nextButton = findViewById(R.id.Next);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getItem(0)<2){
                    slideViewPager.setCurrentItem(getItem(1),true);
                }else{
                    Intent intent = new Intent(NavigationActivity.this,OtpPage.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        slideViewPager  = (ViewPager) findViewById(R.id.slideViewPager);
        doIndicator     = (LinearLayout) findViewById(R.id.doIndicator);

        viewPagerAdapter= new ViewPagerAdapter(this);
        slideViewPager.setAdapter(viewPagerAdapter);

        setDoIndicator(0);
        slideViewPager.addOnPageChangeListener(viewPagerListener);
    }

    public void setDoIndicator(int position){
        dots = new TextView[3];
        doIndicator.removeAllViews();
        for(int i=0; i<dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226",Html.FROM_HTML_MODE_LEGACY));
            dots[i].setTextSize(35);
            doIndicator.addView(dots[i]);
        }
        dots[position].setTextColor(getResources().getColor(R.color.ash,getApplicationContext().getTheme()));
    }

    private int getItem(int i){
        return slideViewPager.getCurrentItem()+i;
    }

}