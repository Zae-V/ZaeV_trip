package com.example.ZaeV_trip.Schedule;

import android.net.Uri;

public class TravelItem {
    String image;
    String name;
    String date;

    public TravelItem(String image, String date, String name){
        this.image = image;
        this.date = date;
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
