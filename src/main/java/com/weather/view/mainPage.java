package com.weather.view;

import com.weather.model.CityMg;
import com.weather.weather.WeatherApplication;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class mainPage {

    public String ShowMainPage(){

        VelocityEngine ve = new VelocityEngine();
        ve.init();

        try {

            String stateName = WeatherApplication.GetMySqlService().SelectState().get(0).getStateName();
            List<CityMg> cities = WeatherApplication.GetMongoDBService().SelectValues();
            List<String> states = WeatherApplication.GetOpenWeatherService().GetAllCountries();
            List<String> citesOfState = WeatherApplication.GetOpenWeatherService().GetAllCities(stateName);

            Template t = ve.getTemplate("template/index.vn");

            VelocityContext vc = new VelocityContext();
            vc.put("hello", "Počasí pro stát " + stateName);
            vc.put("cities", cities);
            vc.put("states", states);
            vc.put("citiesOfC", citesOfState);

            StringWriter sw = new StringWriter();
            t.merge(vc, sw);
            return sw.toString();
        }catch (Exception e){
            WeatherApplication.WriteToLog("Error mainPage: " + e.getMessage());
        }
       // return readAllBytesJava7("templates/index.html");
        return null;
    }

}
