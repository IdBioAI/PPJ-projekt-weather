package com.weather.weather.rest;


import com.weather.weather.Main;
import com.weather.weather.configurations.ConfigProperties;
import com.weather.weather.configurations.FileStorageProperties;
import com.weather.weather.model.CityMySQL;
import com.weather.weather.rest.modelJSON.CityData;
import com.weather.weather.services.MongoDBService;
import com.weather.weather.services.MySQLService;
import com.weather.weather.view.MainPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RestAPIControl {

    @Autowired
    MainPage mainPage;
    @Autowired
    ConfigProperties configProperties;
    @Autowired
    FileStorageProperties fileStorageProperties;
    @Autowired
    private MySQLService mySQLService;
    @Autowired
    private MongoDBService mongoDBService;

    Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("api/day")
    public List<CityMySQL> GetStateInfo(HttpServletResponse response) {

        List<CityMySQL> data = new ArrayList<>();

        try {
            data = GetData(0);
            return data;
        }catch (Exception e){
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }

        return data;
    }

    @GetMapping("api/week")
    public List<CityMySQL> getWeekInfo(HttpServletResponse response) {

        List<CityMySQL> data = new ArrayList<>();

        try {
            data =  GetData(1);
            return data;
        }catch (Exception e){
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }

        return data;
    }

    @GetMapping("api/week2")
    public List<CityMySQL> getWeek2Info(HttpServletResponse response) {

        List<CityMySQL> data = new ArrayList<>();

        try {
            data =  GetData(2);
            return data;
        }catch (Exception e){
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }

        return data;
    }

    @RequestMapping(value = "api/state/change/{state}", method = RequestMethod.PUT)
    public int UpdateState(@PathVariable("state")  String state) {
        if(configProperties.isReadOnly()){
            return 403;
        }

        try {
            mySQLService.ChangeState(state);
            return 202;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 304;
        }
    }

    @RequestMapping(value = "api/city/add/{city}", method = RequestMethod.POST)
    public int AddCity(@PathVariable("city") String cityName) {

        if(configProperties.isReadOnly()){
            return 403;
        }

        try{
            mySQLService.AddCity(cityName);
            return 202;
        }catch (Exception e){
            log.error(e.getMessage());
            return 304;
        }
    }

    @RequestMapping(value = "api/city/delete/{city}", method = RequestMethod.DELETE)
    public int DeleteCity(@PathVariable("city")  String cityName) {

        if(configProperties.isReadOnly()){
            return 403;
        }

        try {
            mySQLService.DeleteCity(cityName);
            mongoDBService.deleteCities(cityName);
            return 202;
        }catch (Exception e){
            log.error(e.getMessage());
            return 304;
        }
    }

    /**
     * Odstranění záznamu z daného dne pro dané město
     * @param response
     * @param city
     * @param date
     */
    @RequestMapping(value = "api/temp/delete", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public int DeleteTemp(@RequestBody CityData cityData) {

        if(configProperties.isReadOnly()){
            return 403;
        }

        try {
            mongoDBService.deleteDate(cityData.getName(), cityData.getDate());
            return 202;
        }catch (Exception e){
            log.error(e.getMessage());
            return 304;
        }
    }

    private List<CityMySQL> GetData(int week){
        List<CityMySQL> cities = mySQLService.GetAllCities();
        mainPage.findWeather(cities, week);
        mainPage.calculateAverageTemp(cities);
        mainPage.UnixTimeToDate(cities);
        return cities;
    }

}
