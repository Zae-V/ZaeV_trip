package com.zaev.ZaeV_trip.model;

public class Reusable {
    private String name;
    private String location;
    private String reason;
    private String mapX;
    private String mapY;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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


    public Reusable(String name, String location, String reason, String mapX, String mapY) {
        this.name = name;
        this.location = location;
        this.reason = reason;
        this.mapX = mapX;
        this.mapY = mapY;
    }
}
