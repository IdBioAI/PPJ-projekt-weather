package com.weather.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.weather.rest", "com.weather.weather"})
public class WeatherApplication {

    private static final Logger log = LoggerFactory.getLogger(WeatherApplication.class);

    private static MySqlService mySqlService = new MySqlService();

    public static void main(String[] args) {
       SpringApplication.run(WeatherApplication.class, args);
    }


    public static void WriteToLog(String msg){
        log.info(msg);
    }


    public static MySqlService getMySqlService() {
        return mySqlService;
    }


}
