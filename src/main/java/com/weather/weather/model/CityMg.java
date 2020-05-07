package com.weather.weather.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Transient;

@Document("values")
public class CityMg {

    @Id
    String id;
    String name;
    long date; //UTC+2 hodiny
    float temp;
    float humidity;
    float windSpeed;
    float deg;
    @Transient
    String dateStr;

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public CityMg(String name, long date, float temp, float humidity, float windSpeed, float deg) {
        this.name = name;
        this.date = date;
        this.temp = temp;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.deg = deg;
    }

    public CityMg(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public float getDeg() {
        return deg;
    }

    public void setDeg(float deg) {
        this.deg = deg;
    }
}
