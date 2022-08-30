package com.example.ZaeV_trip.model;

import java.io.Serializable;

public class ZeroWasteDetail implements Serializable {
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
    private String conts_01;
    private String conts_02;
    private String conts_03;
    private String conts_04;
    private String conts_05;
    private String conts_06;

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

    public String getContents_01() {
        return conts_01;
    }

    public void setContents_01(String conts_01) {
        this.conts_01 = conts_01;
    }

    public String getContents_02() {
        return conts_02;
    }

    public void setContents_02(String conts_02) {
        this.conts_02 = conts_02;
    }

    public String getContents_03() {
        return conts_03;
    }

    public void setContents_03(String conts_03) {
        this.conts_03 = conts_03;
    }

    public String getContents_04() {
        return conts_04;
    }

    public void setContents_04(String conts_04) {
        this.conts_04 = conts_04;
    }

    public String getContents_05() {
        return conts_05;
    }

    public void setContents_05(String conts_05) {
        this.conts_05 = conts_05;
    }

    public String getContents_06() {
        return conts_06;
    }

    public void setContents_06(String conts_06) {
        this.conts_06 = conts_06;
    }


//    public ZeroWaste(String name, String Addr1, String contentID, String themeSubID) {
//        this.name = name;
//        this.Addr1 = Addr1;
//        this.contentID = contentID;
//        this.themeSubID = themeSubID;
//    }

    //For Fragment
    public ZeroWasteDetail(String name, String Addr1, String Addr2, String mapX, String mapY, String telephone, String contentID, String themeSubID, String image, String conts_01, String conts_02, String conts_03, String conts_04, String conts_05, String conts_06) {
        this.name = name;
        this.Addr1 = Addr1;
        this.Addr2 = Addr2;
        this.mapX = mapX;
        this.mapY = mapY;
        this.telephone = telephone;
        this.contentID = contentID;
        this.themeSubID = themeSubID;
        this.image = image;
        this.conts_01 = conts_01;
        this.conts_02 = conts_02;
        this.conts_03 = conts_03;
        this.conts_04 = conts_04;
        this.conts_05 = conts_05;
        this.conts_06 = conts_06;
    }

}
