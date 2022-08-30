package com.zaev.ZaeV_trip.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.zaev.ZaeV_trip.model.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class MySharedPreferences {
    private static final String TAG = "MySharedPreferences";
    private static FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

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
        editor.putString("status", user.status);

        editor.commit();

    }

    public static void modifyUserName(Context ctx, String userEmail, String userName) {
        Log.d("로그인", "사용자 정보를 수정합니다.");

        SharedPreferences.Editor editor = get_shared_preferences(ctx).edit();
        editor.putString("userName", userName);
        editor.commit();

        mFirestore.collection("User").document(userEmail)
                .update("userName", userName)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
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

    public static String getUserStatus(Context ctx) {
        return get_shared_preferences(ctx).getString("status", "");
    }

}
