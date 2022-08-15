package com.example.ZaeV_trip.Schedule;

public class SelectItem {
    String img;
    String name;
    String location;
    String info;

    public SelectItem(String img, String name, String location, String info) {
        this.img = img;
        this.name = name;
        this.location = location;
        this.info = info;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
