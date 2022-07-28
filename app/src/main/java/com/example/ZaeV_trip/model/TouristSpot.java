package com.example.ZaeV_trip.model;

public class TouristSpot {
    public String add1;
    public String add2;
    public String firstImage;
    public String firstImage2;
    public Double mapX;
    public Double mapY;
    public String title;

    public TouristSpot(String add1, String add2, String firstImage, String firstImage2, Double mapX, Double mapY, String title) {
        this.add1 = add1;
        this.add2 = add2;
        this.firstImage = firstImage;
        this.firstImage2 = firstImage2;
        this.mapX = mapX;
        this.mapY = mapY;
        this.title = title;
    }

    public TouristSpot() {}

    public String getAdd1() {
        return add1;
    }

    public String getTitle() {
        return title;
    }

}
