package com.zaev.ZaeV_trip.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Restaurant implements Parcelable, Serializable {
    String id;
    String name;
    String location;
    String category;
    String mapX;
    String mapY;
    String number;
    String menu;
    String authType;

    protected Restaurant(Parcel in) {
        id = in.readString();
        name = in.readString();
        location = in.readString();
        category = in.readString();
        mapX = in.readString();
        mapY = in.readString();
        number = in.readString();
        menu = in.readString();
        authType = in.readString();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

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

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public Restaurant(String id, String name, String location, String category, String mapX, String mapY, String number, String menu, String authType) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.category = category;
        this.mapX = mapX;
        this.mapY = mapY;
        this.number = number;
        this.menu = menu;
        this.authType = authType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(location);
        parcel.writeString(category);
        parcel.writeString(mapX);
        parcel.writeString(mapY);
        parcel.writeString(number);
        parcel.writeString(menu);
        parcel.writeString(authType);
    }
}
