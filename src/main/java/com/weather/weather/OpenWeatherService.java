package com.weather.weather;

import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.HourlyWeatherForecast;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class OpenWeatherService {

    @Autowired
    OpenWeatherService openWeatherService;

  /*  @Autowired
    JSONParser parser;*/

    public OpenWeatherService(){

        //GetAllCities("Afghanistan");
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

    @PostConstruct
    public void Init() {
        WeatherApplication.SetOpenWeatherService(openWeatherService);
    }

    public List<String> GetAllCountries(){
        List<String> c = new ArrayList<String>();
        try {

            JSONArray jsonObject = new JSONArray (ReadFile("countries.json").toString());
            for (int i = 0; i < jsonObject.length(); i++) {
                c.add((String) jsonObject.getJSONObject(i).get("name"));
            }
            return c;

        } catch (Exception e) {
            WeatherApplication.WriteToLog("Error while loading file countries.json: " + e.getMessage());
        }
        return null;
    }

    private static String ReadFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public List<String> GetAllCities(String country) {
        List<String> c = new ArrayList<String>();
        try {

            JSONObject jsonObject = new JSONObject (ReadFile("countriesCities.json").toString());
           // JSONArray arrayCit = jsonObject.names();
            JSONArray arrayCit = jsonObject.getJSONArray(country);
            for (int i = 0; i < arrayCit.length(); i++) {
                c.add((String) arrayCit.getString(i));

            }

            return c;
            //return c;

        } catch (Exception e) {
            WeatherApplication.WriteToLog("Error while loading file countriesCities.json: " + e.getMessage());
        }
        return null;
    }
}
