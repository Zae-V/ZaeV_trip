package com.example.ZaeV_trip.Search.Category;

public class RestaurantCategoryItem {
    int image;
    String name;
    String menu;

    public RestaurantCategoryItem(int image, String name, String menu){
        this.image = image;
        this.name = name;
        this.menu = menu;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }


}

