package com.weather.weather.services;

import com.weather.weather.Main;
import com.weather.weather.model.CityMg;
import com.weather.weather.model.CityMgRepository;
import com.weather.weather.view.mainPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@ComponentScan(basePackages={"com.weather"})
public class MongoDBService {

    @Autowired
    CityMgRepository cityMgRepository;

    @PostConstruct
    public void Init(){

        Main.setMongoDBService(this);
        System.out.println("Vytvarim....");
        //CityMg c = new CityMg("Varnsdorf", "2020-04-13 06:00:00", 10.45f, 75.2f, 3.11f, 292.2f);
        //cityMgRepository.save(c);

    }

    public List<CityMg> SelectValues(String name, int week){
        List<CityMg> values = null;
        try {
            values = new ArrayList<CityMg>();
            cityMgRepository.findByNameAndDateGreaterThan(name, week).forEach(values::add);
        }catch (Exception e){
            //WeatherApplication.WriteToLog("Error while select mongodb: " + e.getMessage());
            return null;
        }
        return values;
    }

    public void SaveData(CityMg cityMg){
        cityMgRepository.save(cityMg);
    }

    public void deleteCities(String city) {
        cityMgRepository.deleteByName(city);
    }

    public void deleteDate(String city, String date) {
        cityMgRepository.deleteByDateAndName(Long.valueOf(date).longValue(), city);
    }
}
