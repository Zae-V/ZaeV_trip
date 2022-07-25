package com.example.ZaeV_trip.Sign;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.captaindroid.tvg.Tvg;

import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.model.Users;
import com.example.ZaeV_trip.util.MySharedPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SignUpFragment extends Fragment {

    TextView greetingText;
    EditText editUsernameEnter;
    EditText editEmailEnter;
    EditText editPWEnter;
    EditText editPWCheck;
    TextView msg;

    android.widget.Button signUpBtn;

    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;


    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        greetingText = v.findViewById(R.id.greetingText);
        editUsernameEnter = v.findViewById(R.id.editUsernameEnter);
        editEmailEnter =v.findViewById(R.id.editEmailEnter);
        editPWEnter = v.findViewById(R.id.editPWEnter);
        editPWCheck =v.findViewById(R.id.editPWCheck);
        msg = v.findViewById(R.id.error_message);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        // 텍스트 글자색 그라데이션 적용
        Tvg.change(greetingText, Color.parseColor("#6C92F4"),  Color.parseColor("#41E884"));

        signUpBtn = v.findViewById(R.id.signUpBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
        return v;
    }

    public void signUp() {
        String password = editPWEnter.getText().toString().trim();
        String pwCheck = editPWCheck.getText().toString().trim();
        String username = editUsernameEnter.getText().toString().trim();
        String email = editEmailEnter.getText().toString().trim();

        ArrayList bookmarkList = new ArrayList();
        ArrayList currentPosition = new ArrayList();
        String profileImage = new String();

        if(username.length() == 0 || email.length() == 0 || pwCheck.length() == 0 || password.length() == 0){
            msg.setText("항목을 모두 입력해주세요.");
        }
        else if(password.equals(pwCheck)) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        user.sendEmailVerification().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getActivity(),"인증 이메일이 전송되었습니다.",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        Users userInfo = new Users(username, email, bookmarkList, currentPosition, profileImage,false);

                        mFirestore.collection("User").document(userInfo.userEmail).set(userInfo);
                        MySharedPreferences.saveUserInfo(getActivity().getApplicationContext(), userInfo);

                        Intent intent = new Intent(getActivity(), SignActivity.class);
                        startActivity(intent);
                    } else {
                        if(password.length()<6){
                            msg.setText("비밀번호는 6자리 이상으로 설정하십시오.");
                        }
                        else if( task.getException() instanceof FirebaseAuthUserCollisionException){
                            msg.setText("이미 가입되어있는 이메일입니다.");
                        }else if(!emailFormatCheck(email)){
                            msg.setText("이메일을 이메일 형식으로 입력하십시오.");
                        }else{
                            msg.setText("서버와의 연결이 불안정합니다. 나중에 다시 시도해주세요.");
                        }
                    }
                }
            });
        }else{
            msg.setText("비밀번호를 잘못 입력하셨습니다.");
        }
    }

    public Boolean emailFormatCheck(String inputEmail){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(inputEmail.matches(emailPattern)){
            return true;
        }else{
            return false;
        }
    }
}
