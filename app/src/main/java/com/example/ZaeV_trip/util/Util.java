package com.example.ZaeV_trip.util;

import android.content.Context;

public class Util {
    public static int ConvertDPtoPX(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
