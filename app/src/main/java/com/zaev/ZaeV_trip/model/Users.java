package com.zaev.ZaeV_trip.model;
import java.util.ArrayList;

public class Users {
    public String userName;
    public String userEmail;
    public ArrayList currentPosition;
    public String profileImage;
    public Boolean notification;
    public String signType;
    public String status;

    public Users() {
        ArrayList currentPosition = new ArrayList <>();

        this.userName = "userName";
        this.userEmail = "userEmail";
        this.currentPosition = currentPosition;
        this.profileImage = "profileImage";
        this.notification = false;
        this.signType = "signType";
        this.status = "default";

    }
    public Users(String userName, String userEmail, ArrayList currentPosition, String profileImage, Boolean notification, String signType, String status) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.currentPosition = currentPosition;
        this.profileImage = profileImage;
        this.notification = notification;
        this.signType = signType;
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}