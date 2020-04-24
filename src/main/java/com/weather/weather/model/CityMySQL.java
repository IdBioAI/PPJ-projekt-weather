package com.weather.weather.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="city")
public class CityMySQL {

    //@Column(name="cityName")
    @Id
    @Column(name="cityName")
    String cityName;

    public CityMySQL(String cityName) {
        this.cityName = cityName;
    }

    public CityMySQL() {
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
