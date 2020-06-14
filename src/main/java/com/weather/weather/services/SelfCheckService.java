package com.weather.weather.services;

import com.weather.weather.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;

@Service
public class SelfCheckService {

    @Autowired
    OpenWeatherService openWeatherService;
    @Autowired
    private ApplicationContext appContext;

    Logger log = LoggerFactory.getLogger(getClass());

    @PostConstruct
    public void init(){
        try {

            File file = new File("webFiles//countries.json");
            String json = openWeatherService.readFile(file.toString());

            if (!openWeatherService.checkJSONArray(json)) {
                log.error("Chyba json souboru - countries.json");
                SpringApplication.exit(appContext, () -> 1);
            }

            file = new File("webFiles//countriesCities.json");
            json = openWeatherService.readFile(file.toString());

            if (!openWeatherService.checkJSONObject(json)) {
                log.error("Chyba json souboru - countriesCities.json");
                SpringApplication.exit(appContext, () -> 1);
            }

        }catch (Exception ex){
            log.error(Main.getStackTrace(ex));
            SpringApplication.exit(appContext, () -> 1);
        }
    }

}
