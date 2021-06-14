package com.example.maze_bank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button btnNext;
    int position=0;
    Button btnGetStarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);
        btnNext=findViewById(R.id.btn_next);

        btnGetStarted=findViewById(R.id.btn_finish);

        tabIndicator=findViewById(R.id.tab_indicator);
        List<Screenitem> mList = new ArrayList<>();
        mList.add(new Screenitem("BANK","EFFICIENT|RELY|INVEST",R.drawable.onboardpic));
        mList.add(new Screenitem("SECURITY","SECURE WAY TO TRANSFER MONEY",R.drawable.onboardic2));
        mList.add(new Screenitem("INVESTMENTS","EASY INVESTMENT FACILITIES IN STOCKS AND FD'S",R.drawable.onboardpic3));


        screenPager=findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);



        tabIndicator.setupWithViewPager(screenPager);
        position=screenPager.getCurrentItem();

        screenPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=screenPager.getCurrentItem();
                if(position<mList.size())
                {
                    position++;
                    screenPager.setCurrentItem(position);
                }

                if(position==mList.size()-1)
                {
                    loadLastScreen();
                }
            }
        });
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),Login.class);
                startActivity(i);
                saveContext();
            }
        });
    }


    private void saveContext() {
        SharedPreferences SP= getApplicationContext().getSharedPreferences("PREF",MODE_PRIVATE);
        SharedPreferences.Editor editor= SP.edit();
        editor.putBoolean("IsOpened",true);
        editor.commit();

    }
    private void loadLastScreen() {
        btnGetStarted.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}