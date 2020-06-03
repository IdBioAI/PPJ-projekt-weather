package com.weather.weather.services;

import com.weather.weather.Main;
import com.weather.weather.model.CityMySQL;
import com.weather.weather.model.CityMySQLRepository;
import com.weather.weather.model.State;
import com.weather.weather.model.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@ComponentScan(basePackages={"com.weather"})
public class MySQLService {
    @Autowired
    StateRepository stateRepository;
    @Autowired
    CityMySQLRepository cityMySQLRepository;
    @Autowired
    MySQLService mySqlService;

    @PostConstruct
    public void Init() {
    }

    public void deleteAll(){
        cityMySQLRepository.deleteAll();
    }

   public List<State> GetState(){
       List<State> s = new ArrayList<>();
       for(State p : stateRepository.findAll()){
           s.add(p);
       }
        return s;
   }

    public List<CityMySQL> GetAllCities(){
        List<CityMySQL> s = new ArrayList<>();
        for(CityMySQL p : cityMySQLRepository.findAll()){
            s.add(p);
        }
        return s;
    }

   public void ChangeState(String name){
        State s = new State(1, name);
        stateRepository.save(s);
   }

   public void AddCity(String city){
       CityMySQL c = new CityMySQL(city);
       cityMySQLRepository.save(c);
   }

    public void DeleteCity(String city) {
        CityMySQL c = new CityMySQL(city);
        cityMySQLRepository.delete(c);
    }
}
