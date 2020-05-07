package com.weather.weather.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.TimerTask;

@Service
@ComponentScan(basePackages={"com.weather.weather.services"})
public class UpdateWeatherService extends TimerTask {

    @Override
    public void run() {
        update();
    }

    private void update() {
        try {
            OpenWeatherService.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
