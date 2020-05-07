/*package com.weather.weather.services;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DBConfiguration {

    private String driverClassName;
    private String url;
    private String username;

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Profile("dev")
    @Bean
    public String devDatabaseConnection(){
        System.out.println("dev: " + url);
        return "DB - DEV";
    }

    @Profile("test")
    @Bean
    public String testDatabaseConnection(){
        System.out.println("test: " + url);
        return "DB - TEST";
    }

    @Profile("prod")
    @Bean
    public String prodDatabaseConnection(){
        System.out.println("prod: " + url);
        return "DB - PROD";
    }

}
*/