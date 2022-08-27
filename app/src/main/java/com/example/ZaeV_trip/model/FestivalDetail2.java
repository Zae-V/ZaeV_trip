package com.example.ZaeV_trip.model;

public class FestivalDetail2 {
    String playtime;
    String eventPlace;
    String eventHomepage;
    String bookingPlace;

    public FestivalDetail2(String playtime, String eventPlace, String eventHomepage, String bookingPlace) {
        this.playtime = playtime;
        this.eventHomepage = eventHomepage;
        this.eventPlace = eventPlace;
        this.bookingPlace = bookingPlace;
    }

    public String getPlaytime() {
        return playtime;
    }

    public void setPlaytime(String playtime) {
        this.playtime = playtime;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    public String getEventHomepage() {
        return eventHomepage;
    }

    public void setEventHomepage(String eventHomepage) {
        this.eventHomepage = eventHomepage;
    }

    public String getBookingPlace() {
        return bookingPlace;
    }

    public void setBookingPlace(String bookingPlace) {
        this.bookingPlace = bookingPlace;
    }
}
