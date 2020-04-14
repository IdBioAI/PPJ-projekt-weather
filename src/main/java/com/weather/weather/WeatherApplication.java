package com.weather.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

@SpringBootApplication(scanBasePackages = {"com.weather.rest", "com.weather.weather"})
public class WeatherApplication {

    private static final Logger log = LoggerFactory.getLogger(WeatherApplication.class);

    private static MySqlService mySqlService;
    private static MongoDBService mongoDBService;

    public static void main(String[] args) {
       SpringApplication.run(WeatherApplication.class, args);
    }

    public static void SetMySqlService(MySqlService mySqlService) {
        WeatherApplication.mySqlService = mySqlService;
    }

    public static void SetMongoDBService(MongoDBService mongoDBService) {
        WeatherApplication.mongoDBService = mongoDBService;
    }

    public static void WriteToLog(String msg){
        log.info(msg);
    }


    public static MySqlService getMySqlService() {
        return mySqlService;
    }

    public static MongoDBService getMongoDBService() {
        return mongoDBService;
    }

}
