package com.weather.weather.rest;


import com.weather.weather.Main;
import com.weather.weather.configurations.ConfigProperties;
import com.weather.weather.configurations.FileStorageProperties;
import com.weather.weather.model.CityMySQL;
import com.weather.weather.model.CityData;
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
    public List<CityMySQL> getStateInfo(HttpServletResponse response) {

        List<CityMySQL> data = new ArrayList<>();

        try {
            data = getData(0);
            return data;
        }catch (Exception e){
            log.error(Main.getStackTrace(e));
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }

        return data;
    }

    @GetMapping("api/week")
    public List<CityMySQL> getWeekInfo(HttpServletResponse response) {

        List<CityMySQL> data = new ArrayList<>();

        try {
            data =  getData(1);
            return data;
        }catch (Exception e){
            log.error(Main.getStackTrace(e));
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }

        return data;
    }

    @GetMapping("api/week2")
    public List<CityMySQL> getWeek2Info(HttpServletResponse response) {

        List<CityMySQL> data = new ArrayList<>();

        try {
            data =  getData(2);
            return data;
        }catch (Exception e){
            log.error(Main.getStackTrace(e));
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }

        return data;
    }

    @RequestMapping(value = "api/state/change/{state}", method = RequestMethod.PUT)
    public int updateState(@PathVariable("state")  String state) {
        if(configProperties.isReadOnly()){
            return HttpServletResponse.SC_FORBIDDEN;
        }

        try {
            mySQLService.changeState(state);
            return HttpServletResponse.SC_ACCEPTED;
        } catch (Exception e) {
            log.error(Main.getStackTrace(e));
            return HttpServletResponse.SC_NOT_MODIFIED;
        }
    }

    @RequestMapping(value = "api/city/add/{city}", method = RequestMethod.POST)
    public int addCity(@PathVariable("city") String cityName) {

        if(configProperties.isReadOnly()){
            return HttpServletResponse.SC_FORBIDDEN;
        }

        try{
            mySQLService.addCity(cityName);
            return HttpServletResponse.SC_ACCEPTED;
        }catch (Exception e){
            log.error(Main.getStackTrace(e));
            return HttpServletResponse.SC_NOT_MODIFIED;
        }
    }

    @RequestMapping(value = "api/city/delete/{city}", method = RequestMethod.DELETE)
    public int deleteCity(@PathVariable("city")  String cityName) {

        if(configProperties.isReadOnly()){
            return HttpServletResponse.SC_FORBIDDEN;
        }

        try {
            mySQLService.deleteCity(cityName);
            mongoDBService.deleteCities(cityName);
            return HttpServletResponse.SC_ACCEPTED;
        }catch (Exception e){
            log.error(Main.getStackTrace(e));
            return HttpServletResponse.SC_NOT_MODIFIED;
        }
    }

    /**
     * Odstranění záznamu z daného dne pro dané město
     * @param response
     * @param city
     * @param date
     */
    @RequestMapping(value = "api/temp/delete", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public int deleteTemp(@RequestBody CityData cityData) {

        if(configProperties.isReadOnly()){
            return HttpServletResponse.SC_FORBIDDEN;
        }

        try {
            mongoDBService.deleteDate(cityData.getName(), cityData.getDate());
            return HttpServletResponse.SC_ACCEPTED;
        }catch (Exception e){
            log.error(Main.getStackTrace(e));
            return HttpServletResponse.SC_NOT_MODIFIED;
        }
    }

    private List<CityMySQL> getData(int week){
        List<CityMySQL> cities = mySQLService.getAllCities();
        mainPage.addWeatherToCities(cities, week);
        mainPage.calculateAverageTemp(cities);
        mainPage.unixTimeToDate(cities);
        return cities;
    }

}
