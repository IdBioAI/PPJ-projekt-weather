package com.weather.weather.services;

import com.weather.weather.configurations.ConfigProperties;
import com.weather.weather.model.CityMg;
import com.weather.weather.model.CityMySQL;
import com.weather.weather.model.ConfigRepository;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import org.json.JSONArray;
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
    float tmp = 0, hum = -1, win = -1, deg = -1;
    List<CityMySQL> cities;
    CurrentWeather cwd;
    CityMg cityMg;
    OWM owm;

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

        cities = mySQLService.getAllCities();
        tmp = 0;
        hum = -1;
        win = -1;
        deg = -1;

        for (CityMySQL city : cities) {

            cwd = owm.currentWeatherByCityName(city.getCityName());

            if(cwd.getMainData().getTemp() != null)
                tmp = cwd.getMainData().getTemp().floatValue();
            else return;
            if(cwd.getMainData().getHumidity() != null)
                hum = cwd.getMainData().getHumidity().floatValue();
            if(cwd.getWindData().getSpeed() != null)
                win = cwd.getWindData().getSpeed().floatValue();
            if(cwd.getWindData().getDegree() != null)
                deg = cwd.getWindData().getDegree().floatValue();

            cityMg = new CityMg(city.getCityName(), Instant.now().getEpochSecond(), tmp, hum, win, deg);
            mongoDBService.saveData(cityMg);
            log.info(city.getCityName() + " update");

        }

        mongoDBService.expiration();
    }


    @PostConstruct
    public void init() {
        owm = new OWM(configProperties.getApiKey());
        owm.setUnit(OWM.Unit.METRIC);
        updateTime();
    }

    public List<String> getAllCountries() throws Exception {

        List<String> c = new ArrayList<String>();
        File file = new File("webFiles//countries.json");

        JSONArray jsonObject = new JSONArray (readFile(file.toString()));
        for (int i = 0; i < jsonObject.length(); i++) {
            c.add((String) jsonObject.getJSONObject(i).get("name"));
        }

        return c;
    }

    public String readFile(String filePath) throws Exception {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    public List<String> getAllCities(String country) throws Exception{

        List<String> c = new ArrayList<>();
        File file = new File("webFiles//countriesCities.json");

        JSONObject jsonObject = new JSONObject (readFile(file.toString()));
        JSONArray arrayCit = jsonObject.getJSONArray(country);

        for (int i = 0; i < arrayCit.length(); i++) {
            c.add((String) arrayCit.getString(i));
        }

        return c;
    }
}
