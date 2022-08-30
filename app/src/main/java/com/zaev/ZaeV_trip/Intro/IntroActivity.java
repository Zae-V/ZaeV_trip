package com.zaev.ZaeV_trip.Intro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.zaev.ZaeV_trip.R;
import com.zaev.ZaeV_trip.Sign.SignActivity;
import com.zaev.ZaeV_trip.util.SignUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntroActivity extends AppCompatActivity {
    private static final String TAG = "IntroActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    Animation fadeIn;
    TextView introText;
    TextView privacyText;
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
        privacyText = findViewById(R.id.textView3);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

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
        privacyText.setText("앱을 시작할 시 서비스의 이용약관 및 개인정보 처리 방침에 동의하게 됩니다.");

        Linkify.TransformFilter mTransform = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return "";
            }
        };

        Pattern pattern1 = Pattern.compile("이용약관");
        Pattern pattern2 = Pattern.compile("개인정보 처리 방침");

        Linkify.addLinks(privacyText, pattern1, "https://thoughtful-dedication-7cf.notion.site/117f97229194496da219e17001c4bcba",null,mTransform);
        Linkify.addLinks(privacyText, pattern2, "https://thoughtful-dedication-7cf.notion.site/9635d2e3a26e466e85b80b31888d85b7",null,mTransform);


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
        
        kakaoJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUtil.kakaoSign(IntroActivity.this, 1);
                IntroActivity.this.finish();
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroActivity.this, SignActivity.class);
                startActivity(intent);
                IntroActivity.this.finish();
            }
        });
    }
}
