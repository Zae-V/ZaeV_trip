package com.example.ZaeV_trip.model;

public class Restaurant {
    private String contentID;
    private String addr1;
    private String addr2;
    private String firstImage;
    private String firstImage2;
    private String mapX;
    private String mapY;
    private String title;
    private String number;

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
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

    public String getMapX() {
        return mapX;
    }

    public void setMapX(String mapX) {
        this.mapX = mapX;
    }

    public String getMapY() {
        return mapY;
    }

    public void setMapY(String mapY) {
        this.mapY = mapY;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getNumber(){return number;}

    public void setNumber(String number) {this.number = number;}

    public Restaurant(String contentID, String addr1, String addr2, String firstImage, String firstImage2, String mapX, String mapY, String title, String number) {
        this.contentID = contentID;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.firstImage = firstImage;
        this.firstImage2 = firstImage2;
        this.mapX = mapX;
        this.mapY = mapY;
        this.title = title;
        this.number = number;
    }
}
