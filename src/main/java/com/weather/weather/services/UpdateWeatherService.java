package com.weather.weather.services;

import com.weather.weather.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.TimerTask;

@Service
@ComponentScan(basePackages={"com.weather.weather.services"})
public class UpdateWeatherService extends TimerTask {

    @Autowired
    OpenWeatherService openWeatherService;

    Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void run() {
        update();
    }

    private void update() {
        try {
            openWeatherService.openWeatherService.update();
        } catch (Exception e) {
            log.error("error while update weather", e);
        }
    }

}
