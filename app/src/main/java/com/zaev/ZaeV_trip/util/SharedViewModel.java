package com.zaev.ZaeV_trip.util;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public final class SharedViewModel extends ViewModel {
    @NotNull
    private final MutableLiveData message = new MutableLiveData();
    private final ArrayList<String> visitedCities = new ArrayList<String>();

    @NotNull
    public final MutableLiveData getMessage() {
        return this.message;
    }

    public final void sendMessage(@NotNull String text) {
        this.message.setValue(text);
    }

    public final ArrayList<String> getVisitedCities(){
        return this.visitedCities;
    }

    public final void setVisitedCities(@NotNull String city){
        if(visitedCities.size() > 3){
            visitedCities.remove(3);
        }
        if(!visitedCities.contains(city)){
            visitedCities.add(0,city);
        }else{
            visitedCities.remove(city);
            visitedCities.add(0,city);
        }
    }
}