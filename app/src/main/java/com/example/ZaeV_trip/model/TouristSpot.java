package com.example.ZaeV_trip.model;

public class TouristSpot {
    public String addr1;
    public String addr2;
    public String firstImage;
    public String firstImage2;
    public Double mapX;
    public Double mapY;
    public String title;

    public TouristSpot(String addr1, String addr2, String firstImage, String firstImage2, Double mapX, Double mapY, String title) {
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.firstImage = firstImage;
        this.firstImage2 = firstImage2;
        this.mapX = mapX;
        this.mapY = mapY;
        this.title = title;
    }

    public TouristSpot() {}

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) { this.addr2 = addr2; }

    public String getTitle() { return title;}

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
    }

    public String getFirstImage2() {
        return firstImage2;
    }

    public void setFirstImage2(String firstImage2) {
        this.firstImage2 = firstImage2;
    }

    public Double getMapX() { return mapX; }

    public void setMapX(Double mapX) {
        this.mapX = mapX;
    }

    public Double getMapY() { return mapY; }

    public void setMapY(Double mapY) {
        this.mapX = mapY;
    }

}
