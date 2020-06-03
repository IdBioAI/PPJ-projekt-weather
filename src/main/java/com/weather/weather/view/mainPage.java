package com.weather.weather.view;


import com.weather.weather.Main;
import com.weather.weather.configurations.ConfigProperties;
import com.weather.weather.model.CityMg;
import com.weather.weather.model.CityMySQL;
import com.weather.weather.services.MongoDBService;
import com.weather.weather.services.MySQLService;
import com.weather.weather.services.OpenWeatherService;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Controller
public class MainPage {

    @Autowired
    ConfigProperties configProperties;
    @Autowired
    MySQLService mySQLService;
    @Autowired
    MongoDBService mongoDBService;
    @Autowired
    OpenWeatherService openWeatherService;

    VelocityEngine ve;
    Template t;
    VelocityContext vc;

    @PostConstruct
    public void Init() {
        ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
    }

    public String ShowMainPage(int week) throws Exception {

        String stateName = mySQLService.GetState().get(0).getStateName();
        //List<CityMg> cities = Main.getMongoDBService().SelectValues();
        List<CityMySQL> cities = mySQLService.GetAllCities();
        findWeather(cities, week);
        calculateAverageTemp(cities);
        UnixTimeToDate(cities);
        List<String> states = openWeatherService.GetAllCountries();
        List<String> citesOfState = openWeatherService.GetAllCities(stateName);
        t = ve.getTemplate("templates/index.vn", "UTF-8");

        vc = new VelocityContext();
        vc.put("hello", "Počasí pro stát " + stateName);
        vc.put("cities", cities);
        vc.put("states", states);
        vc.put("citiesOfC", citesOfState);
        vc.put("readOnly", configProperties.isReadOnly());

        StringWriter sw = new StringWriter();
        t.merge(vc, sw);
        return sw.toString();

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

    public long TimeToUnix(String timestamp) throws Exception{

        long epoch;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        Date dt = sdf.parse(timestamp);
        epoch = dt.getTime();
        return (epoch / 1000);
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
            c.setCities(mongoDBService.SelectValues(c.getCityName(), epoch));
        }
    }

}
