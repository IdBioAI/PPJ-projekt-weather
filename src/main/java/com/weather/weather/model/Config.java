package com.weather.weather.model;

import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="config")
public class Config {

    @Id
    String name;
    @Column(name="value")
    String value;

    public Config(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Config() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
