package com.example.ZaeV_trip.model;

public class Festival {
    private String addr1;
    private String firstImage;
    private Double mapX;
    private Double mapY;
    private String title;
    private String startDate;
    private String endDate;

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

    public Double getMapX() {
        return mapX;
    }

    public void setMapX(Double mapX) {
        this.mapX = mapX;
    }

    public Double getMapY() {
        return mapY;
    }

    public void setMapY(Double mapY) {
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

    public Festival(String addr1, String addr2, String firstImage, String firstImage2, Double mapX, Double mapY, String title, String startDate, String endDate) {
        this.addr1 = addr1;
        this.firstImage = firstImage;
        this.mapX = mapX;
        this.mapY = mapY;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
