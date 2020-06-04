package com.weather.weather.services;

import com.weather.weather.configurations.ConfigProperties;
import com.weather.weather.model.CityMg;
import com.weather.weather.model.CityMySQL;
import com.weather.weather.model.ConfigRepository;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

@Service
public class OpenWeatherService {

    @Autowired
    OpenWeatherService openWeatherService;
    @Autowired
    ConfigRepository configRepository;
    @Autowired
    ConfigProperties configProperties;
    @Autowired
    MySQLService mySQLService;
    @Autowired
    MongoDBService mongoDBService;
    @Autowired
    UpdateWeatherService timerTask;

    Logger log = LoggerFactory.getLogger(getClass());

    int time = 0;

    public OpenWeatherService(){

    }

    public void startUpdating(){
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, time*1000);
    }

    public void updateTime(){
        time = configProperties.getUpdateTime();
        startUpdating();
    }

    public void update() throws Exception{

        if(!configProperties.isUpdate()){
            return;
        }

        List<CityMySQL> cities = mySQLService.getAllCities();
        float tmp = 0;
        float hum = -1;
        float win = -1;
        float deg = -1;

        for (CityMySQL city : cities) {

            OWM owm = new OWM(configProperties.getApiKey());
            owm.setUnit(OWM.Unit.METRIC);
            CurrentWeather cwd = owm.currentWeatherByCityName(city.getCityName());

            if(cwd.getMainData().getTemp() != null)
                tmp = cwd.getMainData().getTemp().floatValue();
            else return;
            if(cwd.getMainData().getHumidity() != null)
                hum = cwd.getMainData().getHumidity().floatValue();
            if(cwd.getWindData().getSpeed() != null)
                win = cwd.getWindData().getSpeed().floatValue();
            if(cwd.getWindData().getDegree() != null)
                deg = cwd.getWindData().getDegree().floatValue();

            CityMg cityMg = new CityMg(city.getCityName(), Instant.now().getEpochSecond(), tmp, hum, win, deg);
            mongoDBService.saveData(cityMg);
            log.info(city.getCityName() + " update");

        }

        mongoDBService.expiration();
    }


    @PostConstruct
    public void init() {
        updateTime();
    }

    public boolean checkJSONArray(String test) {
        try {
            new JSONArray(test);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean checkJSONObject(String test) {
        try {
            new JSONObject(test);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
        return true;
    }

    public String readFile(String filePath) throws Exception {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    public List<String> getAllCountries() throws Exception {

        List<String> c = new ArrayList<>();
        File file = new File("webFiles//countries.json");
        String json = readFile(file.toString());

        if(checkJSONArray(json)) {

            JSONArray jsonObject = new JSONArray(json);
            for (int i = 0; i < jsonObject.length(); i++) {
                c.add((String) jsonObject.getJSONObject(i).get("name"));
            }
        }

        return c;
    }

    public List<String> getAllCities(String country) throws Exception{

        List<String> c = new ArrayList<>();
        File file = new File("webFiles//countriesCities.json");
        String json = readFile(file.toString());

        if(checkJSONObject(json)) {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray arrayCit = jsonObject.getJSONArray(country);

            for (int i = 0; i < arrayCit.length(); i++) {
                c.add(arrayCit.getString(i));
            }
        }

        return c;
    }
}
