package com.weather.weather.rest.modelJSON;

public class CityData {
    String name;
    String date;

    public CityData(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public CityData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
