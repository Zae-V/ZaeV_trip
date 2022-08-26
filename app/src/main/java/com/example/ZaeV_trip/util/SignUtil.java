package com.example.ZaeV_trip.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ZaeV_trip.Intro.IntroActivity;
import com.example.ZaeV_trip.Profile.ModifyActivity;
import com.example.ZaeV_trip.Profile.ProfileModifyDetailFragment;
import com.example.ZaeV_trip.Profile.ProfileModifyFragment;
import com.example.ZaeV_trip.Profile.WithdrawalActivity;
import com.example.ZaeV_trip.Main.MainActivity;
import com.example.ZaeV_trip.Sign.SignActivity;
import com.example.ZaeV_trip.Sign.SignInFragment;
import com.example.ZaeV_trip.Sign.SignUpFragment;
import com.example.ZaeV_trip.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

// SignUtil - 로그인 관련 함수 모음
// type 1 - 로그인, type 2 - 탈퇴, type 3 - 정보 변경
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
                                ArrayList currentPosition = new ArrayList();
                                String profileImage = user.getKakaoAccount().getProfile().getProfileImageUrl();
                                Boolean notification = false;
                                String signType = "kakao";

                                Users newUser = new Users(userName, userEmail, currentPosition, profileImage, notification, signType);

                                // 이메일 중복 체크
                                mFirestore.collection("User").document(userEmail)
                                        .get().
                                        addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();

                                                    if (document.exists()) {
                                                        HashMap userInfo = (HashMap) document.getData();
                                                        String userName = (String) userInfo.get("userName");
                                                        newUser.userName = userName;
                                                        
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

    public static void kakaoSignIn(Context ctx, Integer type, Users user, String password) {
        mAuth.signInWithEmailAndPassword(user.userEmail, password).addOnCompleteListener((Activity) ctx, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "로그인 성공");

                    if (type == 1) {
                        MySharedPreferences.saveUserInfo(ctx, user);
                        Intent intent = new Intent(ctx, MainActivity.class);
                        ctx.startActivity(intent);
                    } else if (type == 2) {
                        ((WithdrawalActivity)WithdrawalActivity.ctx).setWithdrawal(2);
                    } else {
                        ((ModifyActivity)ModifyActivity.ctx).replaceFragment(ProfileModifyDetailFragment.newInstance());
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

    public static void kakaoSignUp(Context ctx, Integer type, Users user, String password) {
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

    public static void withdrawal(Context ctx, String email) {
        MySharedPreferences.clearUser(ctx.getApplicationContext()); // SharedPreferences 정보 삭제

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); // Authentication 정보 삭제
        String uid = firebaseUser.getUid();

        String[] docs = {"cafe", "festival", "lodging", "plogging", "restaurant", "reusable", "touristSpot", "zeroWaste"};

        mFirestore.collection("User").document(email).delete(); // Firestore 정보 삭제 - User

        // Firestore 정보 삭제 - Schedule
        mFirestore.collection("Schedule").document(uid).collection("schedule").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().size() > 0) {
                    for (QueryDocumentSnapshot dc : task.getResult()) {
                        dc.getReference().delete();
                    }
                }
            }
        });

        // Firestore 정보 삭제 - BookmarkItem
        for (String doc : docs) {
            mFirestore.collection("BookmarkItem").document(uid).collection(doc).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.getResult().size() > 0) {
                        for (QueryDocumentSnapshot dc : task.getResult()) {
                            dc.getReference().delete();
                        }
                    }
                }
            });
        }

        // Firestore 정보 삭제 - Authentication
        firebaseUser.delete()
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
    public static void signOut(Context ctx) {
        FirebaseAuth.getInstance().signOut();
        MySharedPreferences.clearUser(ctx.getApplicationContext());

        Toast.makeText(ctx.getApplicationContext(),"로그아웃 되었습니다!",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ctx, IntroActivity.class);
        ctx.startActivity(intent);
    }

    public static void checkedEmailDuplicate(Context ctx, Integer type, Boolean emailDuplicated,Users user, String password) {
        if (emailDuplicated) {
            Log.d(TAG, "중복된 이메일이 있습니다");
            kakaoSignIn(ctx, type, user, password);
        } else {
            Log.d(TAG, "중복된 이메일이 없습니다.");
            kakaoSignUp(ctx, type, user, password);
        }
    }

    public static void emailSignIn(Context ctx, String email, String pwd, Integer type) {
        if (!email.equals("") && !pwd.equals("")) {
            mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener((Activity) ctx, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        if (!Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {
                            SignInFragment.msg.setText("이메일 인증을 완료해주십시오.");
                        } else {
                            ArrayList currentPosition = new ArrayList();
                            String profileImage = new String();

                            mFirestore.collection("User").document(email)
                                    .get().
                                    addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (type == 1) {
                                                    DocumentSnapshot document = task.getResult();
                                                    HashMap userInfo = (HashMap) document.getData();
                                                    String userName = (String) userInfo.get("userName");
                                                    String signType = "email";

                                                    Users user = new Users(userName, email, currentPosition, profileImage, false, signType);
                                                    MySharedPreferences.saveUserInfo(ctx.getApplicationContext(), user);

                                                    Intent intent = new Intent(ctx, MainActivity.class);
                                                    ctx.startActivity(intent);
                                                } else if (type == 2) {
                                                    ((WithdrawalActivity)WithdrawalActivity.ctx).setVisibility(1);
                                                    ((WithdrawalActivity)WithdrawalActivity.ctx).setVisibility(2);
                                                    ((WithdrawalActivity)WithdrawalActivity.ctx).setVisibility(3);
                                                    ((WithdrawalActivity)WithdrawalActivity.ctx).setVisibility(4);
                                                    ((WithdrawalActivity)WithdrawalActivity.ctx).setVisibility(6);
                                                    ((WithdrawalActivity)WithdrawalActivity.ctx).setWithdrawal(1);
                                                } else {
                                                    ((ModifyActivity)ModifyActivity.ctx).replaceFragment(ProfileModifyDetailFragment.newInstance());
                                                }

                                            } else {
                                                Log.d("ERROR", "get failed with ", task.getException());
                                            }
                                        }
                                    });
                        }
                    } else {
                        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                if (!task.isSuccessful()) {
                                    if (type == 1) {
                                        SignInFragment.msg.setText("가입되지 않은 이메일입니다.");
                                    } else if (type == 2) {
                                        ((WithdrawalActivity)WithdrawalActivity.ctx).setErrorText("가입되지 않은 이메일입니다.");
                                    } else {
                                        ProfileModifyFragment.setErrorText("가입되지 않은 이메일입니다.");
                                    }

                                } else {
                                    if (type == 1) {
                                        SignInFragment.msg.setText("비밀번호를 확인해주십시오.");
                                    } else if (type == 2) {
                                        ((WithdrawalActivity)WithdrawalActivity.ctx).setErrorText("비밀번호를 확인해주십시오.");
                                    } else {
                                        ProfileModifyFragment.setErrorText("비밀번호를 확인해주십시오.");
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    public static void emailSignUp(Context ctx, String password, String pwCheck, String username, String email) {
        ArrayList currentPosition = new ArrayList();
        String profileImage = new String();

        if(username.length() == 0 || email.length() == 0 || pwCheck.length() == 0 || password.length() == 0){
            SignUpFragment.msg.setText("항목을 모두 입력해주세요.");
        }
        else if(password.equals(pwCheck)) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) ctx, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        user.sendEmailVerification().addOnCompleteListener((Activity) ctx, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ctx,"인증 이메일이 전송되었습니다.",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        String signType = "email";
                        Users userInfo = new Users(username, email, currentPosition, profileImage,false, signType);

                        mFirestore.collection("User").document(userInfo.userEmail).set(userInfo);
                        MySharedPreferences.saveUserInfo(ctx.getApplicationContext(), userInfo);

                        Intent intent = new Intent(ctx, SignActivity.class);
                        ctx.startActivity(intent);
                    } else {
                        if(password.length()<6){
                            SignUpFragment.msg.setText("비밀번호는 6자리 이상으로 설정하십시오.");
                        }
                        else if( task.getException() instanceof FirebaseAuthUserCollisionException){
                            SignUpFragment.msg.setText("이미 가입되어있는 이메일입니다.");
                        }else if(!emailFormatCheck(email)){
                            SignUpFragment.msg.setText("이메일을 이메일 형식으로 입력하십시오.");
                        }else{
                            SignUpFragment.msg.setText("서버와의 연결이 불안정합니다. 나중에 다시 시도해주세요.");
                        }
                    }
                }
            });
        }else{
            SignUpFragment.msg.setText("비밀번호를 잘못 입력하셨습니다.");
        }
    }

    public static Boolean emailFormatCheck(String inputEmail){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(inputEmail.matches(emailPattern)){
            return true;
        }else{
            return false;
        }
    }

    public static void updateEmailPassword(String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                        }
                    }
                });
    }

    public static void findPassword(Context ctx, String email) {
        if (email != "") {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ctx.getApplicationContext(),"비밀번호 재설정 이메일이 전송되었습니다.",Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ctx.getApplicationContext(),"가입된 계정이 존재하지 않습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(ctx.getApplicationContext(),"",Toast.LENGTH_SHORT).show();
        }
    }
}

