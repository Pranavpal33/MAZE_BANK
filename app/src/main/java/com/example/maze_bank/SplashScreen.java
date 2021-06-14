package com.example.maze_bank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    Animation top_Anim,bottom_Anim;
    ImageView img1,img2;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        //To move to next page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(RestorePref())
                {
                    Intent i = new Intent(getApplicationContext(),Login.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Intent i = new Intent(SplashScreen.this, IntroActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        },5000);
        //Animations
        top_Anim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom_Anim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        //hooks
        img1=findViewById(R.id.imageView);
        img2=findViewById(R.id.imageView2);
        txt=findViewById(R.id.textView);
        //Setting the animations
        img1.setAnimation(top_Anim);
        img2.setAnimation(bottom_Anim);
        txt.setAnimation(bottom_Anim);
    }
    private boolean RestorePref() {
        SharedPreferences SP= getApplicationContext().getSharedPreferences("PREF",MODE_PRIVATE);
        boolean opened=SP.getBoolean("IsOpened",false);
        return opened;

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
