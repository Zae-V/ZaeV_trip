package com.zaev.ZaeV_trip.Profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.captaindroid.tvg.Tvg;
import com.zaev.ZaeV_trip.Bookmark.BookmarkActivity;
import com.zaev.ZaeV_trip.Main.MainActivity;
import com.zaev.ZaeV_trip.R;
import com.zaev.ZaeV_trip.Schedule.TravelActivity;
import com.zaev.ZaeV_trip.util.MySharedPreferences;
import com.zaev.ZaeV_trip.util.SignUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.profile);


        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.profile:
                    return true;
                case R.id.bookmark:
                    Intent intent = new Intent(getApplicationContext(), BookmarkActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    ProfileActivity.this.finish();
                    return true;

                case R.id.travel:
                    Intent intent1 = new Intent(getApplicationContext(), TravelActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent1);
                    ProfileActivity.this.finish();

                    return true;

                case R.id.home:
                    Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent2);
                    ProfileActivity.this.finish();

            }
            return false;
        });

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
                showLogoutDialog();
            }
        });

        View withdrawalTextView = findViewById(R.id.withdrawalTextView);
        withdrawalTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, WithdrawalActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        ProfileActivity.this.finish();
    }

    public void showLogoutDialog(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setIcon(R.mipmap.ic_launcher);//알림창 아이콘 설정
        dialog.setMessage("로그아웃 하시겠습니까?"); //알림창 메세지 설정

        dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SignUtil.signOut(ProfileActivity.this);
                ProfileActivity.this.finish();
            }
        });
        dialog.setNegativeButton("아니오",null);
        dialog.show();
    }
}
