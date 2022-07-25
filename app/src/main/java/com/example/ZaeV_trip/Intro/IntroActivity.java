package com.example.ZaeV_trip.Intro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ZaeV_trip.MainActivity;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.Sign.SignActivity;
import com.example.ZaeV_trip.model.Users;
import com.example.ZaeV_trip.util.MySharedPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class IntroActivity extends AppCompatActivity {
    private static final String TAG = "IntroActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

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

        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if (oAuthToken != null) {
                    UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
                        @Override
                        public Unit invoke(User user, Throwable throwable) {
                            if (user != null) {
                                Log.d(TAG, "invoke: id=" + user.getId());
                                Log.d(TAG, "invoke: email=" + user.getKakaoAccount().getEmail());

                                String userName = user.getKakaoAccount().getProfile().getNickname();
                                String userEmail = user.getKakaoAccount().getEmail();
                                String userPassword = user.getId() + user.getKakaoAccount().getEmail();
                                ArrayList bookmarkList = new ArrayList();
                                ArrayList currentPosition = new ArrayList();
                                String profileImage = user.getKakaoAccount().getProfile().getProfileImageUrl();
                                Boolean notification = false;

                                Users newUser = new Users(userName, userEmail, bookmarkList, currentPosition, profileImage, notification);

                                // 이메일 중복 체크
                                mFirestore.collection("User").document(userEmail)
                                        .get().
                                        addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        checkedEmailDuplicate(true, newUser, userPassword);
                                                    } else {
                                                        checkedEmailDuplicate(false, newUser, userPassword);
                                                    }
                                                } else {
                                                    Log.d(TAG, "get failed with ", task.getException());
                                                }
                                            }
                                        });
                            }
                            if (throwable != null) {
                                Log.d(TAG, "invoke: " + throwable.getLocalizedMessage());
                            }
                            return null;
                        }
                    });
                }
                if (throwable != null) {
                    // TBD
                    Log.d(TAG, "invoke: " + throwable.getLocalizedMessage());
                }
                return null;
            }
        };

        kakaoJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(IntroActivity.this)) {
                    UserApiClient.getInstance().loginWithKakaoTalk(IntroActivity.this, callback);
                } else {
                    UserApiClient.getInstance().loginWithKakaoAccount(IntroActivity.this, callback);
                }

            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroActivity.this, SignActivity.class);
                startActivity(intent);
            }
        });
    }

    public void signIn(Users user, String password) {
        mAuth.signInWithEmailAndPassword(user.userEmail, password).addOnCompleteListener(IntroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "로그인 성공");
                    MySharedPreferences.saveUserInfo(IntroActivity.this, user);
                    Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Log.d(TAG, "로그인 실패");
                    Log.d(TAG, String.valueOf(task.getException()));

                    Toast.makeText(getApplicationContext(), "이미 존재하는 이메일이 있습니다.", Toast.LENGTH_SHORT).show();
                    // 이미 아이디 존재할 때 따로 처리 //
                }
            }
        });
    }

    public void signUp(Users user, String password) {
        mAuth.createUserWithEmailAndPassword(user.userEmail, password).addOnCompleteListener(IntroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "회원가입 성공");
                    mFirestore.collection("User").document(user.userEmail).set(user);
                    MySharedPreferences.saveUserInfo(IntroActivity.this, user);

                    Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Log.d(TAG, "회원가입 실패");
                    Log.d(TAG, String.valueOf(task.getException()));
                }
            }
        });
    }

    public void checkedEmailDuplicate(Boolean emailDuplicated,Users user, String password) {
        if (emailDuplicated) {
            Log.d(TAG, "중복된 이메일이 있습니다");
            signIn(user, password);
        } else {
            Log.d(TAG, "중복된 이메일이 없습니다.");
            signUp(user, password);
        }
    }
}
