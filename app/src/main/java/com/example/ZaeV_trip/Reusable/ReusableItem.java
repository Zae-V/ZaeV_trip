package com.example.ZaeV_trip.Reusable;

public class ReusableItem {
    String name;
    String location;
    String x;
    String y;
    String reason;

    public ReusableItem(String name, String location, String x, String y, String reason) {
        this.name = name;
        this.location = location;
        this.x = x;
        this.y = y;
        this.reason = reason;
    }

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

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
