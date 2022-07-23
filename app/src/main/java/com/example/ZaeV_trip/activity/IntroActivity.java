package com.example.ZaeV_trip.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ZaeV_trip.R;

public class IntroActivity extends AppCompatActivity {
    private static final String TAG = "IntroActivity";

    Animation fadeIn;
    TextView introText;
    Button kakaoJoinBtn;
    Button joinBtn;
    ImageView introImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        introText = findViewById(R.id.introText);
        introImg = findViewById(R.id.introImg);
        Glide.with(this).load(R.raw.animation_bird).into(introImg);
        kakaoJoinBtn = findViewById(R.id.kakaoJoinBtn);
        joinBtn = findViewById(R.id.joinBtn);

        // 제비 글자색 바꾸기
        String content = introText.getText().toString();
        SpannableString spannableString = new SpannableString(content);

        String word1 = "제";
        String word2 = "비";

        int loc1 = content.indexOf(word1);
        int loc2 = content.indexOf(word2);

        Log.d("MYTAG", String.valueOf(loc1));
        Log.d("MYTAG", String.valueOf(loc2));
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#47FFC5")),
                loc1, loc1 + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#47FFC5")),
                loc2, loc2 + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        introText.setText(spannableString);

        // fade in 애니메이션 적용
        fadeIn= AnimationUtils.loadAnimation(this,R.anim.fadein);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        introText.startAnimation(fadeIn);
    }
}
