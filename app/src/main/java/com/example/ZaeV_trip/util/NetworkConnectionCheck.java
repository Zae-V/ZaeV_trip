package com.example.ZaeV_trip.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ZaeV_trip.Error.ErrorActivity;
import com.example.ZaeV_trip.Intro.SplashActivity;

public class NetworkConnectionCheck  extends ConnectivityManager.NetworkCallback {

    private Context context;
    private NetworkRequest networkRequest;
    private ConnectivityManager connectivityManager;

    public NetworkConnectionCheck(Context context){
        this.context=context;
        networkRequest =
                new NetworkRequest.Builder()                                        // addTransportType : 주어진 전송 요구 사항을 빌더에 추가
                        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)   // TRANSPORT_CELLULAR : 이 네트워크가 셀룰러 전송을 사용함을 나타냅니다.
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)       // TRANSPORT_WIFI : 이 네트워크가 Wi-Fi 전송을 사용함을 나타냅니다.
                        .build();
        this.connectivityManager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE); // CONNECTIVITY_SERVICE : 네트워크 연결 관리 처리를 검색
    }

    public void register() { this.connectivityManager.registerNetworkCallback(networkRequest, this);}

    public void unregister() {
        this.connectivityManager.unregisterNetworkCallback(this);
    }

    @Override
    public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);

        if(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)){
            Intent intent = new Intent(context, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);
        }
    }

    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);

        // 네트워크가 연결되었을 때 할 동작

        Toast.makeText(this.context, "network available", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);

        // 네트워크 연결이 끊겼을 때 할 동작
        Toast.makeText(this.context, "network lost", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, ErrorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);

    }


}
