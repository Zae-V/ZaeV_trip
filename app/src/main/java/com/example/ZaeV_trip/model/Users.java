package com.example.ZaeV_trip.model;
import java.util.ArrayList;

public class Users {
    public String userName;
    public String userEmail;
    public ArrayList bookmarkList;
    public ArrayList currentPosition;
    public String profileImage;
    public Boolean notification;
    public String signType;

    public Users() {
        ArrayList bookmarkList = new ArrayList <>();
        ArrayList currentPosition = new ArrayList <>();

        this.userName = "userName";
        this.userEmail = "userEmail";
        this.bookmarkList = bookmarkList;
        this.currentPosition = currentPosition;
        this.profileImage = "profileImage";
        this.notification = false;
        this.signType = "signType";

    }
    public Users(String userName, String userEmail, ArrayList bookmarkList, ArrayList currentPosition, String profileImage, Boolean notification, String signType) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.bookmarkList = bookmarkList;
        this.currentPosition = currentPosition;
        this.profileImage = profileImage;
        this.notification = notification;
        this.signType = signType;
    }

}