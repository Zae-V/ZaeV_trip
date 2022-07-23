package com.example.ZaeV_trip.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ZaeV_trip.MainActivity;
import com.example.ZaeV_trip.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView gif_image = (ImageView) findViewById(R.id.gif_image);
        Glide.with(this).load(R.raw.animation_bird).into(gif_image);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (MySharedPreferences.getUserEmail(SplashActivity.this).length() != 0) {
//                    Toast.makeText(getApplicationContext(), "자동 로그인 되었습니다.", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
//                    startActivity(intent);
//                }
                Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

    }

}
