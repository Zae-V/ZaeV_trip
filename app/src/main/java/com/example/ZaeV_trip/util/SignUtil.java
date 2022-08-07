package com.example.ZaeV_trip.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ZaeV_trip.Intro.IntroActivity;
import com.example.ZaeV_trip.MainActivity;
import com.example.ZaeV_trip.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

// SignUtil - 로그인 관련 함수 모음
// type 1 - 로그인, type 2 - 탈퇴
public class SignUtil {
    private static final String TAG = "SignUtil";
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    public static void kakaoSign(Context ctx, Integer type) {
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
                                                        checkedEmailDuplicate(ctx, type, true, newUser, userPassword);
                                                    } else {
                                                        checkedEmailDuplicate(ctx, type, false, newUser, userPassword);
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

        if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(ctx)) {
            UserApiClient.getInstance().loginWithKakaoTalk(ctx, callback);
        } else {
            UserApiClient.getInstance().loginWithKakaoAccount(ctx, callback);
        }
    }

    public static void signIn(Context ctx, Integer type, Users user, String password) {
        mAuth.signInWithEmailAndPassword(user.userEmail, password).addOnCompleteListener((Activity) ctx, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "로그인 성공");

                    if (type == 1) {
                        MySharedPreferences.saveUserInfo(ctx, user);
                        Intent intent = new Intent(ctx, MainActivity.class);
                        ctx.startActivity(intent);
                    } else {

                        MySharedPreferences.clearUser(ctx.getApplicationContext()); // SharedPreferences 정보 삭제
                        mFirestore.collection("User").document(user.userEmail).delete(); // Firestore 정보 삭제

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // Authentication 정보 삭제
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User account deleted.");
                                        }
                                    }
                                });

                        Intent intent = new Intent(ctx, IntroActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        ctx.startActivity(intent);
                    }

                }else{
                    Log.d(TAG, "로그인 실패");
                    Log.d(TAG, String.valueOf(task.getException()));

                    Toast.makeText(ctx.getApplicationContext(), "이미 존재하는 이메일이 있습니다.", Toast.LENGTH_SHORT).show();
                    // 이미 아이디 존재할 때 따로 처리 //
                }
            }
        });
    }

    public static void signUp(Context ctx, Integer type, Users user, String password) {
        mAuth.createUserWithEmailAndPassword(user.userEmail, password).addOnCompleteListener((Activity) ctx, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "회원가입 성공");
                    mFirestore.collection("User").document(user.userEmail).set(user);
                    MySharedPreferences.saveUserInfo(ctx, user);

                    Intent intent = new Intent(ctx, MainActivity.class);
                    ctx.startActivity(intent);
                } else {
                    Log.d(TAG, "회원가입 실패");
                    Log.d(TAG, String.valueOf(task.getException()));
                }
            }
        });
    }

    public static void checkedEmailDuplicate(Context ctx, Integer type, Boolean emailDuplicated,Users user, String password) {
        if (emailDuplicated) {
            Log.d(TAG, "중복된 이메일이 있습니다");
            signIn(ctx, type, user, password);
        } else {
            Log.d(TAG, "중복된 이메일이 없습니다.");
            signUp(ctx, type, user, password);
        }
    }
}

