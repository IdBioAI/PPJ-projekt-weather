package com.weather.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.io.PrintWriter;
import java.io.StringWriter;

@SpringBootApplication
//@EnableConfigurationProperties
@ConfigurationPropertiesScan("com.weather.weather")
public class Main implements CommandLineRunner{

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    public static String logError(Exception e, String message){
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return message + " - " + e.getMessage() + ": " + sw.toString();
    }

    @Override
    public void run(String... args){ }

}
