package com.example.ZaeV_trip.Profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.captaindroid.tvg.Tvg;
import com.example.ZaeV_trip.Intro.IntroActivity;
import com.example.ZaeV_trip.R;
import com.example.ZaeV_trip.Sign.SignActivity;
import com.example.ZaeV_trip.util.MySharedPreferences;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    String userName;
    String userEmail;
    String userProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName = MySharedPreferences.getUserName(getApplicationContext());
        userEmail = MySharedPreferences.getUserEmail(getApplicationContext());
        userProfileImage = MySharedPreferences.getUserProfileImage(getApplicationContext());

        // 텍스트 Gradient 적용
        TextView profileTextView = findViewById(R.id.profileText);
        Tvg.change(profileTextView, Color.parseColor("#6C92F4"),  Color.parseColor("#41E884"));

        TextView mainNameTextView = findViewById(R.id.mainNameTextView);
        mainNameTextView.setText(userName);

        TextView nameTextView = findViewById(R.id.nameTextView);
        nameTextView.setText(userName);

        TextView emailTextView = findViewById(R.id.emailTextView);
        emailTextView.setText(userEmail);

        CircleImageView userProfileImageView = findViewById(R.id.profileImageView);

        Glide.with(this)
                .load(userProfileImage)
                .placeholder(R.drawable.default_profile_image)
                .error(R.drawable.default_profile_image)
                .fallback(R.drawable.default_profile_image)
                .into(userProfileImageView);

        TextView modifyProfileTextView = findViewById(R.id.modifyProfileTextView);
        modifyProfileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ModifyActivity.class);
                startActivity(intent);
            }
        });

        View logoutTextView = findViewById(R.id.logoutTextView);
        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    public void showDialog(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setIcon(R.mipmap.ic_launcher);//알림창 아이콘 설정
        dialog.setMessage("로그아웃 하시겠습니까?"); //알림창 메세지 설정

        dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"로그아웃 되었습니다!",Toast.LENGTH_SHORT).show();
                MySharedPreferences.clearUser(getApplicationContext());
                Intent intent = new Intent(ProfileActivity.this, IntroActivity.class);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("아니오",null);
        dialog.show();
    }
}
