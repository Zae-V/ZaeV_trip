package com.zaev.ZaeV_trip.model;

public class TouristSpotDetail2 {
    private String infocenter;
    private String restdate;

    public TouristSpotDetail2(String infocenter, String restdate) {
        this.infocenter = infocenter;
        this.restdate = restdate;
    }

    public String getInfocenter() {
        return infocenter;
    }

    public void setInfocenter(String infocenter) {
        this.infocenter = infocenter;
    }

    public String getRestdate() {
        return restdate;
    }

    public void setRestdate(String restdate) {
        this.restdate = restdate;
    }
}
