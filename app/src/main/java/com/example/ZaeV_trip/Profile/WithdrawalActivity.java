package com.example.ZaeV_trip.Profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.util.MySharedPreferences;
import com.example.ZaeV_trip.util.SignUtil;

import java.util.Objects;

public class WithdrawalActivity extends AppCompatActivity {

    public static Button withdrawalBtn;
    public static Button kakaoCertificationBtn;
    public static Button signInBtn;
    public static TextView errorTextView;
    public static TextView emailTextView;
    public static EditText editID;
    public static TextView passwordTextView;
    public static EditText editPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);

        String signType = MySharedPreferences.getUserSignType(WithdrawalActivity.this);

        kakaoCertificationBtn = findViewById(R.id.kakaoCertificationBtn);
        emailTextView = findViewById(R.id.emailTextView);
        editID = findViewById(R.id.editID);
        passwordTextView = findViewById(R.id.passwordTextView);
        editPW = findViewById(R.id.editPW);
        errorTextView = findViewById(R.id.errorTextView);
        signInBtn = findViewById(R.id.signInBtn);
        withdrawalBtn = findViewById(R.id.withdrawalBtn);

        Log.d("signType", signType);
        if (Objects.equals(signType, "kakao")) {
            emailTextView.setVisibility(View.GONE);
            editID.setVisibility(View.GONE);
            passwordTextView.setVisibility(View.GONE);
            editPW.setVisibility(View.GONE);
            signInBtn.setVisibility(View.GONE);
            errorTextView.setVisibility(View.GONE);
        } else {
            kakaoCertificationBtn.setVisibility(View.GONE);
        }

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editID.getText().toString().trim();
                String pwd = editPW.getText().toString().trim();
                SignUtil.emailSignIn(WithdrawalActivity.this, email, pwd, 2);
            }
        });

        kakaoCertificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUtil.kakaoSign(WithdrawalActivity.this, 2);
            }
        });

        withdrawalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWithdrawalDialog();
            }
        });
    }

    public void showWithdrawalDialog() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setIcon(R.mipmap.ic_launcher);//알림창 아이콘 설정
        dialog.setMessage("탈퇴 하시겠습니까?"); //알림창 메세지 설정

        dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = MySharedPreferences.getUserEmail(getApplicationContext());
                SignUtil.withdrawal(WithdrawalActivity.this, email);
            }
        });
        dialog.setNegativeButton("아니오",null);
        dialog.show();
    }
}
