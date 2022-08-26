package com.example.ZaeV_trip.Bookmark;

public class BookmarkItem {
    String image;
    String name;
    String location;
    String hours;

    public BookmarkItem(){

    }

    public BookmarkItem(String image, String name, String location, String hours){
        this.image = image;
        this.name = name;
        this.location = location;
        this.hours = hours;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
}