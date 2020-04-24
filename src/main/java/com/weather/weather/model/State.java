package com.weather.weather.model;


import javax.persistence.*;

@Entity
public class State {

    @Id
    int id;
    @Column(name="stateName")
    String stateName;

    public State(int id, String stateName) {
        this.id = id;
        this.stateName = stateName;
    }

    public State() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public String toString() {
        return ("id: " + id + " Name: " + stateName);
    }
}
