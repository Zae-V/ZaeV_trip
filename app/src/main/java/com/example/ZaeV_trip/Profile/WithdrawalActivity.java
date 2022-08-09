package com.example.ZaeV_trip.Profile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.util.MySharedPreferences;

import java.util.Objects;

public class WithdrawalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);

        String signType = MySharedPreferences.getUserSignType(WithdrawalActivity.this);

        Button kakaoCertificationBtn = findViewById(R.id.kakaoCertificationBtn);
        TextView emailTextView = findViewById(R.id.emailTextView);
        EditText editID = findViewById(R.id.editID);
        TextView passwordTextView = findViewById(R.id.passwordTextView);
        EditText editPW = findViewById(R.id.editPW);
        Button signInBtn = findViewById(R.id.signInBtn);
        Button withdrawalBtn = findViewById(R.id.withdrawalBtn);

        Log.d("signType", signType);
        if (Objects.equals(signType, "kakao")) {
            emailTextView.setVisibility(View.GONE);
            editID.setVisibility(View.GONE);
            passwordTextView.setVisibility(View.GONE);
            editPW.setVisibility(View.GONE);
            signInBtn.setVisibility(View.GONE);
        } else {
            kakaoCertificationBtn.setVisibility(View.GONE);
        }
    }
}
