package com.weather.weather.view;


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
    public void init() {
        ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
    }

    /**
     *
     * @param days - o kolik dní zpět se mají nahrát data
     * @return
     * @throws Exception
     */
    public String showMainPage(int days) throws Exception {

        String stateName = mySQLService.getState().get(0).getStateName();
        //List<CityMg> cities = Main.getMongoDBService().SelectValues();
        List<CityMySQL> cities = mySQLService.getAllCities();
        addWeatherToCities(cities, days);
        calculateAverageTemp(cities);
        unixTimeToDate(cities);
        List<String> states = openWeatherService.getAllCountries();
        List<String> citesOfState = openWeatherService.getAllCities(stateName);
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

    public void unixTimeToDate(List<CityMySQL> cities) {

        Date date;
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyy HH:mm:ss");

        for(CityMySQL c : cities){
            for(CityMg cityData : c.getCities()) {
                date = new java.util.Date((long)cityData.getDate()*1000L);
                cityData.setDateStr(sdf.format(date));
            }
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

    public long daysToSeconds(int days){
        return 24 * 60 * 60 *  days;
    }

    /**
     *
     * @param cities
     * @param days - o kolik dní zpět se mají nahrát data
     */
    public void addWeatherToCities(List<CityMySQL> cities, int days) {

        long epoch = Instant.now().getEpochSecond() - daysToSeconds(days);

        for(CityMySQL c : cities){
            c.setCities(mongoDBService.selectValues(c.getCityName(), epoch));
        }

    }

}
