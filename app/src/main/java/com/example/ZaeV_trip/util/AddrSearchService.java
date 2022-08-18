package com.example.ZaeV_trip.util;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface AddrSearchService {
    @GET("v2/local/search/keyword.json")
    Call<Location> searchAddressList(
            @Query("query") String query,
            @Query("x") String x,
            @Query("y") String y,
            @Header("Authorization") String apikey);
}


