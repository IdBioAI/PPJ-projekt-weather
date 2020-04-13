package com.weather.models;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
public class State {

    int id;
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
