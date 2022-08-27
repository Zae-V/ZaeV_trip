package com.example.ZaeV_trip.model;

import java.io.Serializable;

public class Festival implements Serializable {
    private String addr1;
    private String firstImage;
    private String mapX;
    private String mapY;
    private String title;
    private String startDate;
    private String endDate;
    private String id;
    private String tel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Festival(String id, String addr1, String firstImage, String mapX, String mapY, String title, String startDate, String endDate, String tel) {
        this.id = id;
        this.addr1 = addr1;
        this.firstImage = firstImage;
        this.mapX = mapX;
        this.mapY = mapY;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tel = tel;
    }

    public String getTel() { return tel; }

    public void setTel(String tel) { this.tel = tel; }
}
