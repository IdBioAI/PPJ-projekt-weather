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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
//@EnableConfigurationProperties
@ConfigurationPropertiesScan("com.weather.weather")
public class Main implements CommandLineRunner{


    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args){ }

}
