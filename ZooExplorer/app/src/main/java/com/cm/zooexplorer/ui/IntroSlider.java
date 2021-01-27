package com.cm.zooexplorer.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import com.cm.zooexplorer.MainActivity;
import com.cm.zooexplorer.R;
import com.cm.zooexplorer.adapters.IntroViewPagerAdapter;
import com.cm.zooexplorer.models.ScreenItem;

public class IntroSlider extends AppCompatActivity {
    public static final String PREFERENCES_NAME = "com.cm.zooexplorer.preferences";
    public static final String IS_FIRST_TIME = "isFirstTime";
    private ViewPager viewPager;
    private IntroViewPagerAdapter introViewPagerAdapter;
    private TabLayout tabLayout;
    private Button btnNext, btnSkip, btnGetStarted;
    private List<ScreenItem> introSlides = new ArrayList<>();
    private boolean swipeEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if(!isFirstTime())
            startApplication();
        
        setContentView(R.layout.activity_intro_slider);
        getSupportActionBar().hide();

        tabLayout = findViewById(R.id.tabLayout);
        btnNext = findViewById(R.id.butNext);
        btnSkip = findViewById(R.id.butSkip);
        btnGetStarted = findViewById(R.id.butGetStarted);

        // add slides to the list
        introSlides.add(new ScreenItem(getString(R.string.map), getString(R.string.intro_map), R.drawable.intro_map));
        introSlides.add(new ScreenItem(getString(R.string.habitats), getString(R.string.intro_habitats), R.drawable.intro_list));
        introSlides.add(new ScreenItem(getString(R.string.habitat), getString(R.string.intro_habitat), R.drawable.intro_info));
        introSlides.add(new ScreenItem(getString(R.string.gallery), getString(R.string.intro_gallery), R.drawable.intro_photos));
        introSlides.add(new ScreenItem("", "", R.drawable.logo));

        viewPager = findViewById(R.id.viewPager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, introSlides);
        viewPager.setAdapter(introViewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        /*viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return swipeEnabled && IntroSlider.super.onTouchEvent(event);
            }
        });*/

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int position = viewPager.getCurrentItem();

                if(position < introSlides.size())
                    viewPager.setCurrentItem(++position);

                if(position == introSlides.size()-1)
                    loadLastScreen();
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewPager.getCurrentItem();

                if(position < introSlides.size()-1)
                    viewPager.setCurrentItem(introSlides.size()-1);

                if(position == introSlides.size()-1)
                    loadLastScreen();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == introSlides.size()-1)
                    loadLastScreen();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePrefsData();
                startApplication();
            }
        });
    }

    private boolean isFirstTime() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        return prefs.getBoolean(IS_FIRST_TIME, true);
    }

    private void savePrefsData(){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(IS_FIRST_TIME, false);
        editor.apply();
    }

    private void startApplication(){
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
        finish();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void loadLastScreen(){

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        btnNext.setVisibility(View.INVISIBLE);
        btnSkip.setVisibility(View.INVISIBLE);
        tabLayout.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
    }
}
