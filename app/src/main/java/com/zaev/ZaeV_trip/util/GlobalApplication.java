package com.zaev.ZaeV_trip.util;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

import com.zaev.ZaeV_trip.R;

public class GlobalApplication extends Application {
    private static GlobalApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // 네이티브 앱 키로 초기화
        KakaoSdk.init(this, getString(R.string.kakao_native_key));
    }
}