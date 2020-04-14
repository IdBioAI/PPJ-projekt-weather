package com.weather.weather;

import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.HourlyWeatherForecast;
import org.springframework.stereotype.Component;


public class OpenWeatherService {


    public OpenWeatherService(){

        try{

            OWM owm = new OWM("82ca724eba719259cc2fa548dbe11898");
            owm.setUnit(OWM.Unit.METRIC);

            HourlyWeatherForecast cwd = owm.hourlyWeatherForecastByCityName("Varnsdorf", OWM.Country.CZECH_REPUBLIC);
            System.out.println("City: " + cwd.getCityData().getName());
            System.out.println("City: " + cwd.getDataCount());
            System.out.println("City: " + cwd.getMessage());


            System.out.println(HourlyWeatherForecast.toJson(cwd));
	        /*CurrentWeather cwd = owm.currentWeatherByCityName("Varnsdorf", OWM.Country.CZECH_REPUBLIC);
	        System.out.println("City: " + cwd.getCityName());*/

            // printing the max./min. temperature
            //System.out.println("Temperature: " + cwd.getMainData().getTempMax()
            //                   + "/" + cwd.getMainData().getTempMin() + "\' C");

        }catch(Exception ex){
            System.out.println(ex);
        }

    }

}
