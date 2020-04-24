package com.weather.weather.services;

import com.weather.weather.Main;
import com.weather.weather.model.CityMg;
import com.weather.weather.model.CityMgRepository;
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
    }

    public List<CityMg> SelectValues(){
        List<CityMg> values = null;
        try {
            values = new ArrayList<CityMg>();
            cityMgRepository.findAll().forEach(values::add);
        }catch (Exception e){
            //WeatherApplication.WriteToLog("Error while select mongodb: " + e.getMessage());
            return null;
        }
        return values;
    }

}
