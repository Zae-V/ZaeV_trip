package com.example.ZaeV_trip.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.ZaeV_trip.model.Users;

public class MySharedPreferences {
    static public SharedPreferences get_shared_preferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void saveUserInfo(Context ctx, Users user) {
        Log.d("로그인", "사용자 정보를 저장합니다.");
        SharedPreferences.Editor editor = get_shared_preferences(ctx).edit();

        editor.putString("userName", user.userName);
        editor.putString("userEmail", user.userEmail);
        editor.putString("userProfileImage", user.profileImage);
        editor.putString("signType", user.signType);

        editor.commit();

    }

    public static void clearUser(Context ctx) {
        SharedPreferences.Editor editor = get_shared_preferences(ctx).edit();

        editor.clear();
        editor.commit();
    }

    public static String getUserName(Context ctx) {
        return get_shared_preferences(ctx).getString("userName", "");
    }

    public static String getUserEmail(Context ctx) {
        return get_shared_preferences(ctx).getString("userEmail", "");
    }

    public static String getUserProfileImage(Context ctx) {
        return get_shared_preferences(ctx).getString("userProfileImage", "");
    }

    public static String getUserSignType(Context ctx) {
        return get_shared_preferences(ctx).getString("signType", "");
    }
    
}
