package com.zaev.ZaeV_trip.Profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zaev.ZaeV_trip.R;
import com.zaev.ZaeV_trip.util.MySharedPreferences;
import com.zaev.ZaeV_trip.util.SignUtil;

import java.util.Objects;

public class WithdrawalActivity extends AppCompatActivity {

    public static Context ctx;
    Button withdrawalBtn;
    Button kakaoCertificationBtn;
    Button signInBtn;
    TextView errorTextView;
    TextView emailTextView;
    EditText editID;
    TextView passwordTextView;
    EditText editPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);
        ctx = this;

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
            setVisibility(1);
            setVisibility(2);
            setVisibility(3);
            setVisibility(4);
            setVisibility(5);
            setVisibility(6);
        } else {
            setVisibility(7);
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
    public void setVisibility(Integer idx) {
        switch (idx) {
            case 1:
                emailTextView.setVisibility(View.GONE);
                break;
            case 2:
                editID.setVisibility(View.GONE);
                break;
            case 3:
                passwordTextView.setVisibility(View.GONE);
                break;
            case 4:
                editPW.setVisibility(View.GONE);
                break;
            case 5:
                signInBtn.setVisibility(View.GONE);
                break;
            case 6:
                errorTextView.setVisibility(View.GONE);
                break;
            case 7:
                kakaoCertificationBtn.setVisibility(View.GONE);
                break;
            default:
                Log.d("WithdrawalActivity", "default");
        }
    }

    public void setWithdrawal(Integer idx) {
        switch (idx) {
            case 1:
                signInBtn.setText("로그인 완료");
                withdrawalBtn.setBackground(ctx.getDrawable(R.drawable.rounded_shape));
                withdrawalBtn.setEnabled(true);
                break;
            case 2:
                kakaoCertificationBtn.setText("계정 인증 완료");
                withdrawalBtn.setBackground(ctx.getDrawable(R.drawable.rounded_shape));
                withdrawalBtn.setEnabled(true);
                break;
        }
    }

    public void setErrorText(String message) {
        errorTextView.setText(message);
    }
}
