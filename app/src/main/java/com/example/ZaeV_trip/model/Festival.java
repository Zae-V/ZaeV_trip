package com.example.ZaeV_trip.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Festival implements Parcelable, Serializable {
    private String addr1;
    private String firstImage;
    private String mapX;
    private String mapY;
    private String title;
    private String startDate;
    private String endDate;
    private String id;
    private String tel;

    protected Festival(Parcel in) {
        addr1 = in.readString();
        firstImage = in.readString();
        mapX = in.readString();
        mapY = in.readString();
        title = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        id = in.readString();
        tel = in.readString();
    }

    public static final Creator<Festival> CREATOR = new Creator<Festival>() {
        @Override
        public Festival createFromParcel(Parcel in) {
            return new Festival(in);
        }

        @Override
        public Festival[] newArray(int size) {
            return new Festival[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(addr1);
        parcel.writeString(firstImage);
        parcel.writeString(mapX);
        parcel.writeString(mapY);
        parcel.writeString(title);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeString(id);
        parcel.writeString(tel);
    }
}
