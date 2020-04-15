package com.weather.weather;

import com.weather.model.CityMg;
import com.weather.model.State;
import net.aksingh.owmjapis.model.param.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class MongoDBService {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    CityMgRepository cityMgRepository;

    @PostConstruct
    public void Init(){
        WeatherApplication.SetMongoDBService(this);
    }

    public List<CityMg> SelectValues(){
        List<CityMg> values = null;
        try {
            values = new ArrayList<CityMg>();
            cityMgRepository.findAll().forEach(values::add);
        }catch (Exception e){
            WeatherApplication.WriteToLog("Error while select mongodb: " + e.getMessage());
            return null;
        }
        return values;
    }


    public void AddCity(String city) {
    }

    public void DeleteCity(String city) {
    }
}
