package com.weather.weather.services;

import com.weather.weather.Main;
import com.weather.weather.configurations.ConfigProperties;
import com.weather.weather.model.CityMg;
import com.weather.weather.model.CityMgRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@ComponentScan(basePackages={"com.weather"})
public class MongoDBService {

    @Autowired
    CityMgRepository cityMgRepository;
    @Autowired
    ConfigProperties configProperties;

    @PostConstruct
    public void Init(){
    }

    public void deleteAll(){
        cityMgRepository.deleteAll();
    }

    public List<CityMg> selectValues(String name, long week){
        List<CityMg> values = new ArrayList<>();
        cityMgRepository.findByNameAndDateGreaterThan(name, week).forEach(values::add);
        return values;
    }

    public List<CityMg> selectValuesByName(String name){
        List<CityMg> values = new ArrayList<>();
        cityMgRepository.findByName(name).forEach(values::add);
        return values;
    }

    public void saveData(CityMg cityMg){
        cityMgRepository.save(cityMg);
    }

    public void deleteCities(String city) {
        cityMgRepository.deleteByName(city);
    }

    public void expiration() {
        cityMgRepository.deleteByDateLessThan(Instant.now().getEpochSecond() - (configProperties.getExpirationTime() * 24 * 60 * 60));
    }

    public void deleteDate(String city, String date) {
        cityMgRepository.deleteByDateAndName(Long.valueOf(date).longValue(), city);
    }
}
