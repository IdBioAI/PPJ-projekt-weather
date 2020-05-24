package com.weather.weather.view;


import com.weather.weather.Main;
import com.weather.weather.configurations.ConfigProperties;
import com.weather.weather.model.CityMg;
import com.weather.weather.model.CityMySQL;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
public class MainPage {

    @Autowired
    ConfigProperties configProperties;

    public String ShowMainPage(int week){

        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        try {

            String stateName = Main.getMySQLService().GetState().get(0).getStateName();
            //List<CityMg> cities = Main.getMongoDBService().SelectValues();
            List<CityMySQL> cities = Main.getMySQLService().GetAllCities();
            findWeather(cities, week);
            calculateAverageTemp(cities);
            UnixTimeToDate(cities);
            List<String> states = Main.getOpenWeatherService().GetAllCountries();
            List<String> citesOfState = Main.getOpenWeatherService().GetAllCities(stateName);
            Template t = ve.getTemplate("templates/index.vn", "UTF-8");

            VelocityContext vc = new VelocityContext();
            vc.put("hello", "Počasí pro stát " + stateName);
            vc.put("cities", cities);
            vc.put("states", states);
            vc.put("citiesOfC", citesOfState);
            vc.put("readOnly", configProperties.isReadOnly());

            StringWriter sw = new StringWriter();
            t.merge(vc, sw);
            return sw.toString();
        }catch (Exception e){
            Main.getLog().error(e.getMessage());
        }
       // return readAllBytesJava7("templates/index.html");
        return null;
    }

    public void UnixTimeToDate(List<CityMySQL> cities) {
        Date date;
        SimpleDateFormat sdf;
        for(CityMySQL c : cities){
            for(CityMg cityData : c.getCities()) {
                date = new java.util.Date((long)cityData.getDate()*1000L);
                sdf = new java.text.SimpleDateFormat("dd-MM-yyy HH:mm:ss");
                cityData.setDateStr(sdf.format(date));
            }
        }
    }

    public static long TimeToUnix(String timestamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
            Date dt = sdf.parse(timestamp);
            long epoch = dt.getTime();
            return (epoch / 1000);
        } catch (Exception e) {
            Main.getLog().error(e.getMessage());
            return 0;
        }
    }

    public void calculateAverageTemp(List<CityMySQL> cities) {
        float avg = 0;
        int total = 0;
        for(CityMySQL c : cities){
            avg = 0;
            total = 0;
            for(CityMg cityData : c.getCities()) {
                avg += cityData.getTemp();
                total++;
            }
            avg /= total;
            c.setAverageTemp(Math.round(avg * 100) / 100f);
        }
    }

    public void findWeather(List<CityMySQL> cities, int week) {
        long epoch = 0;
        if(week == 0) {
            epoch = Instant.now().getEpochSecond() - 86400;
        }
        else if(week == 1) {
            epoch = Instant.now().getEpochSecond() - 604801;
        }

        else if(week == 2) {
            epoch = Instant.now().getEpochSecond() - 1209601;
        }

        for(CityMySQL c : cities){
            c.setCities(Main.getMongoDBService().SelectValues(c.getCityName(), epoch));
        }
    }

}
