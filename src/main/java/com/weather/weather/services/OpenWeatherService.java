package com.weather.weather.services;

import com.weather.weather.Main;
import com.weather.weather.configurations.ConfigProperties;
import com.weather.weather.model.CityMg;
import com.weather.weather.model.CityMySQL;
import com.weather.weather.model.Config;
import com.weather.weather.model.ConfigRepository;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import net.aksingh.owmjapis.model.HourlyWeatherForecast;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;

@Service
public class OpenWeatherService {

    @Autowired
    OpenWeatherService openWeatherService;
    @Autowired
    ConfigRepository configRepository;
    @Autowired
    ConfigProperties configProperties;

    TimerTask timerTask;

    static int time = 0;

  /*  @Autowired
    JSONParser parser;*/

    public OpenWeatherService(){

    }

    public void startUpdating(){
        timerTask = new UpdateWeatherService();
        //running timer task as daemon thread
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, time*1000);
    }

    public void updateTime(){
        //time = Integer.parseInt(configRepository.findByName("timeUpdate").getValue());
        time = configProperties.getUpdateTime();
        startUpdating();
    }

    public static void update(){

        try{
            List<CityMySQL> cities = Main.getMySQLService().GetAllCities();
            OWM owm = new OWM("82ca724eba719259cc2fa548dbe11898");
            owm.setUnit(OWM.Unit.METRIC);
            CurrentWeather cwd;
            CityMg cityMg;
            for (CityMySQL city : cities) {
                cwd = owm.currentWeatherByCityName(city.getCityName());
                // System.out.println("City: " + cwd.getCityData().getName());
                // System.out.println("City: " + cwd.get);
                // System.out.println("City: " + cwd.getMessage());


                System.out.println(CurrentWeather.toJson(cwd));
	        /*CurrentWeather cwd = owm.currentWeatherByCityName("Varnsdorf", OWM.Country.CZECH_REPUBLIC);
	        System.out.println("City: " + cwd.getCityName());*/

                // printing the max./min. temperature
                //System.out.println("Temperature: " + cwd.getMainData().getTempMax()
                //                   + "/" + cwd.getMainData().getTempMin() + "\' C");
                cityMg = new CityMg(city.getCityName(), Instant.now().getEpochSecond(), cwd.getMainData().getTemp().floatValue(), cwd.getMainData().getHumidity().floatValue(), cwd.getWindData().getSpeed().floatValue(), cwd.getWindData().getDegree().floatValue());
                Main.getMongoDBService().SaveData(cityMg);
            }
        }catch(Exception ex){
            System.out.println(ex);
        }

    }


    @PostConstruct
    public void Init() {
      //  WeatherApplication.SetOpenWeatherService(openWeatherService);
        Main.setOpenWeatherService(this);
        updateTime();
    }

    public List<String> GetAllCountries(){
        List<String> c = new ArrayList<String>();
        ClassLoader classLoader = new OpenWeatherService().getClass().getClassLoader();
        File file = new File(classLoader.getResource("countries.json").getFile());
        try {

            JSONArray jsonObject = new JSONArray (ReadFile(file.toString()));
            for (int i = 0; i < jsonObject.length(); i++) {
                c.add((String) jsonObject.getJSONObject(i).get("name"));
            }
            return c;

        } catch (Exception e) {
           // WeatherApplication.WriteToLog("Error while loading file countries.json: " + e.getMessage());
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
        ClassLoader classLoader = new OpenWeatherService().getClass().getClassLoader();
        File file = new File(classLoader.getResource("countriesCities.json").getFile());
        try {

            JSONObject jsonObject = new JSONObject (ReadFile(file.toString()));
           // JSONArray arrayCit = jsonObject.names();
            JSONArray arrayCit = jsonObject.getJSONArray(country);
            for (int i = 0; i < arrayCit.length(); i++) {
                c.add((String) arrayCit.getString(i));

            }

            return c;
            //return c;

        } catch (Exception e) {
           // WeatherApplication.WriteToLog("Error while loading file countriesCities.json: " + e.getMessage());
        }
        return null;
    }
}
