package com.zaev.ZaeV_trip.model;

public class Cafe {
    String id;
    String name;
    String location;
    String category;
    String mapX;
    String mapY;
    String number;
    String menu;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public Cafe(String id, String name, String location, String category, String mapX, String mapY, String number, String menu) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.category = category;
        this.mapX = mapX;
        this.mapY = mapY;
        this.number = number;
        this.menu = menu;
    }
}


