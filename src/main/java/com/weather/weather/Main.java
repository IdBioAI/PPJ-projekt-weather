package com.weather.weather;

import com.weather.weather.configurations.ConfigProperties;
import com.weather.weather.model.State;
import com.weather.weather.services.MongoDBService;
import com.weather.weather.services.MySQLService;
import com.weather.weather.services.OpenWeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
//@EnableConfigurationProperties
@ConfigurationPropertiesScan("com.weather.weather.configurations")
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private static MySQLService mySQLService;
    private static MongoDBService mongoDBService;
    private static OpenWeatherService openWeatherService;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    public static MySQLService getMySQLService() {
        return mySQLService;
    }

    public static void setMySQLService(MySQLService mySQLService) {
        Main.mySQLService = mySQLService;
    }

    public static MongoDBService getMongoDBService() {
        return mongoDBService;
    }

    public static void setMongoDBService(MongoDBService mongoDBService) {
        Main.mongoDBService = mongoDBService;
    }

    public static OpenWeatherService getOpenWeatherService() {
        return openWeatherService;
    }

    public static void setOpenWeatherService(OpenWeatherService openWeatherService) {
        Main.openWeatherService = openWeatherService;
    }

    public static Logger getLog(){
        return log;
    }
}
