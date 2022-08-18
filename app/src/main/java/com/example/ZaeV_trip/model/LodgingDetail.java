package com.example.ZaeV_trip.model;

public class LodgingDetail {
    private String firstImage;
    private String firstImage2;
    private String homepage;
    private String mapX;
    private String mapY;
    private String overview;

    public LodgingDetail() {}

    public LodgingDetail(String firstImage, String firstImage2, String homepage, String mapX, String mapY, String overview) {
        this.firstImage = firstImage;
        this.firstImage2 = firstImage2;
        this.homepage = homepage;
        this.mapX = mapX;
        this.mapY = mapY;
        this.overview = overview;
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

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
