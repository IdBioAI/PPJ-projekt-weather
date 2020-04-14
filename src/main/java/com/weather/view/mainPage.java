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

            List<CityMg> cities = WeatherApplication.getMongoDBService().SelectValues();

            Template t = ve.getTemplate("template/index.vn");

            VelocityContext vc = new VelocityContext();
            vc.put("hello", "Počasí pro stát " + WeatherApplication.getMySqlService().SelectState().get(0).getStateName());
            vc.put("cities", cities);

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
