package com.weather.weather.services;

import com.weather.weather.Main;
import org.aspectj.apache.bcel.classfile.Module;
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
            Main.getOpenWeatherService().openWeatherService.update();
        } catch (Exception e) {
            Main.getLog().error(e.getMessage());
        }
    }

}
