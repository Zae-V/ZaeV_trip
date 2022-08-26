package com.example.ZaeV_trip.model;
import java.util.ArrayList;

public class Users {
    public String userName;
    public String userEmail;
    public ArrayList currentPosition;
    public String profileImage;
    public Boolean notification;
    public String signType;

    public Users() {
        ArrayList currentPosition = new ArrayList <>();

        this.userName = "userName";
        this.userEmail = "userEmail";
        this.currentPosition = currentPosition;
        this.profileImage = "profileImage";
        this.notification = false;
        this.signType = "signType";

    }
    public Users(String userName, String userEmail, ArrayList currentPosition, String profileImage, Boolean notification, String signType) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.currentPosition = currentPosition;
        this.profileImage = profileImage;
        this.notification = notification;
        this.signType = signType;
    }

}