package com.example.ZaeV_trip.Schedule;

public class TravelItem {
    int image;
    String name;
    String date;

    public TravelItem(int image, String date, String name){
        this.image = image;
        this.date = date;
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
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
