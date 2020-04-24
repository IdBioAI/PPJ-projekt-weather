package com.weather.weather.view;


import com.weather.weather.Main;
import com.weather.weather.model.CityMg;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.util.List;


public class mainPage {

    public String ShowMainPage(){

        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        try {

            String stateName = Main.getMySQLService().GetState().get(0).getStateName();
            List<CityMg> cities = Main.getMongoDBService().SelectValues();
            List<String> states = Main.getOpenWeatherService().GetAllCountries();
            List<String> citesOfState = Main.getOpenWeatherService().GetAllCities(stateName);

            Template t = ve.getTemplate("templates/index.vn", "UTF-8");

            VelocityContext vc = new VelocityContext();
            vc.put("hello", "Počasí pro stát " + stateName);
            vc.put("cities", cities);
            vc.put("states", states);
            vc.put("citiesOfC", citesOfState);

            StringWriter sw = new StringWriter();
            t.merge(vc, sw);
            return sw.toString();
        }catch (Exception e){
           // WeatherApplication.WriteToLog("Error mainPage: " + e.getMessage());
            System.out.println("Error mainPage: " + e.getMessage());
        }
       // return readAllBytesJava7("templates/index.html");
        return null;
    }

}
