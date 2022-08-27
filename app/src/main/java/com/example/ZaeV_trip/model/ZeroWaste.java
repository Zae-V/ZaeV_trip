package com.example.ZaeV_trip.model;

public class ZeroWaste {
    private String name;
    private String Addr1;
    private String Addr2;
    private String mapX;
    private String mapY;
    private String telephone;
    private String contentID;
    private String themeSubID;
    private String image;
    private String keyword;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr1() {
        return Addr1;
    }

    public void setAddr1(String Addr1) {
        this.Addr1 = Addr1;
    }

    public String getAddr2() {
        return Addr2;
    }

    public void setAddr2(String Addr2) {
        this.Addr2 = Addr2;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }
    public String getThemeSubID() {
        return themeSubID;
    }

    public void setThemeSubID(String themeSubID) {
        this.themeSubID = themeSubID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ZeroWaste(String name, String Addr1, String Addr2, String mapX, String mapY, String telephone, String contentID, String themeSubID, String image, String keyword) {
        this.name = name;
        this.Addr1 = Addr1;
        this.Addr2 = Addr2;
        this.mapX = mapX;
        this.mapY = mapY;
        this.telephone = telephone;
        this.contentID = contentID;
        this.themeSubID = themeSubID;
        this.image = image;
        this.keyword = keyword;
        }
}
