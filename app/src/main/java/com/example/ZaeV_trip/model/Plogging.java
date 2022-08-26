package com.example.ZaeV_trip.model;

import java.io.Serializable;

public class Plogging implements Serializable {
    private String crsKorNm; // 코스명
    private String crsDstnc; // 코스 길이 (단위:km)
    private String crsTotlRqrmHour; // 총 소요시간 (단위:분)
    private String crsLevel; // 난이도 (1:하/2:중/3:상)
    private String crsContents; // 코스 설명
    private String crsSummary; // 코스 개요
    private String crsTourInfo; // 관광 포인트
    private String travelerinfo; // 여행자 정보
    private String sigun; // 행정구역
    private String brdDiv; // 걷기&자전거 구분 (DNWW:걷기길, DNBW:자전거길)
    private String gpxpath;

    public Plogging() {};

    public Plogging(String crsKorNm, String crsDstnc, String crsTotlRqrmHour, String crsLevel, String crsContents, String crsSummary, String crsTourInfo, String travelerinfo, String sigun, String brdDiv, String gpxpath) {
        this.crsKorNm = crsKorNm;
        this.crsDstnc = crsDstnc;
        this.crsTotlRqrmHour = crsTotlRqrmHour;
        this.crsLevel = crsLevel;
        this.crsContents = crsContents;
        this.crsSummary = crsSummary;
        this.crsTourInfo = crsTourInfo;
        this.travelerinfo = travelerinfo;
        this.sigun = sigun;
        this.brdDiv = brdDiv;
        this.gpxpath = gpxpath;
    }

    public String getCrsKorNm() {
        return crsKorNm;
    }

    public void setCrsKorNm(String crsKorNm) {
        this.crsKorNm = crsKorNm;
    }

    public String getCrsDstnc() {
        return crsDstnc;
    }

    public void setCrsDstnc(String crsDstnc) {
        this.crsDstnc = crsDstnc;
    }

    public String getCrsTotlRqrmHour() {
        return crsTotlRqrmHour;
    }

    public void setCrsTotlRqrmHour(String crsTotlRqrmHour) {
        this.crsTotlRqrmHour = crsTotlRqrmHour;
    }

    public String getCrsLevel() {
        return crsLevel;
    }

    public void setCrsLevel(String crsLevel) {
        this.crsLevel = crsLevel;
    }

    public String getCrsContents() {
        return crsContents;
    }

    public void setCrsContents(String crsContents) {
        this.crsContents = crsContents;
    }

    public String getCrsSummary() {
        return crsSummary;
    }

    public void setCrsSummary(String crsSummary) {
        this.crsSummary = crsSummary;
    }

    public String getCrsTourInfo() {
        return crsTourInfo;
    }

    public void setCrsTourInfo(String crsTourInfo) {
        this.crsTourInfo = crsTourInfo;
    }

    public String getTravelerinfo() {
        return travelerinfo;
    }

    public void setTravelerinfo(String travelerinfo) {
        this.travelerinfo = travelerinfo;
    }

    public String getSigun() {
        return sigun;
    }

    public void setSigun(String sigun) {
        this.sigun = sigun;
    }

    public String getBrdDiv() {
        return brdDiv;
    }

    public void setBrdDiv(String brdDiv) {
        this.brdDiv = brdDiv;
    }

    public String getGpxpath() {return gpxpath; }

    public void setGpxpath(String gpxpath) { this.gpxpath = gpxpath; }
}
