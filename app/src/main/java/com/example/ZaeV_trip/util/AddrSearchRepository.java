package com.example.ZaeV_trip.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.ZaeV_trip.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddrSearchRepository {
    private static AddrSearchRepository INSTANCE;
    private  static String API_KEY;

    public static AddrSearchRepository getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AddrSearchRepository();
        }
        API_KEY = context.getResources().getString(R.string.kakao_api_key);
        return INSTANCE;
    }

    public void getAddressList(String address, String x, String y, AddressResponseListener listener) {
        if (address != null) {
            Log.d("테스트", "키: "+ API_KEY);
            Call<Location> call = RetrofitNet.getRetrofit().getSearchAddrService().searchAddressList(address, x, y, "KakaoAK " + API_KEY);
            call.enqueue(new Callback<Location>() {
                @Override
                public void onResponse(Call<Location> call, Response<Location> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            for (int i = 0; i < response.body().documentsList.size(); i++) {
                                Log.d("테스트", "[GET] getAddressList : " + response.body().documentsList.get(i).getId());
                            }
                            listener.onSuccessResponse(response.body());
                        }
                    }
                }

                @Override
                public void onFailure(Call<Location> call, Throwable t) {
                    listener.onFailResponse();
                }
            });
        }
    }

    public interface AddressResponseListener{
        void onSuccessResponse(Location locationData);
        void onFailResponse();
    }

}
