package com.weather.weather.rest;


import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.weather.weather.Main;
import com.weather.weather.configurations.ConfigProperties;
import com.weather.weather.configurations.FileStorageProperties;
import com.weather.weather.model.CityMg;
import com.weather.weather.model.CityMySQL;
import com.weather.weather.rest.modelJSON.CityData;
import com.weather.weather.view.MainPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

@RestController
public class RestAPIControl {

    @Autowired
    MainPage mainPage;
    @Autowired
    ConfigProperties configProperties;
    @Autowired
    FileStorageProperties fileStorageProperties;


    @GetMapping("api/day")
    public List<CityMySQL> GetStateInfo(HttpServletResponse response) {
        try {
            return GetData(0);
        }catch (Exception e){
            Main.getLog().error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        return null;
    }

    @GetMapping("api/week")
    public List<CityMySQL> getWeekInfo(HttpServletResponse response) {
        try {
            return GetData(1);
        }catch (Exception e){
            Main.getLog().error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        return null;
    }

    @GetMapping("api/week2")
    public List<CityMySQL> getWeek2Info(HttpServletResponse response) {
        try {
            return GetData(2);
        }catch (Exception e){
            Main.getLog().error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        return null;
    }

    @RequestMapping(value = "api/state/change/{state}", method = RequestMethod.PUT)
    public int UpdateState(@PathVariable("state")  String state) {
        if(configProperties.isReadOnly()){
            //response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return 403;
        }

        try {
            Main.getMySQLService().ChangeState(state);
            //response.setStatus(HttpServletResponse.SC_ACCEPTED);
            return 202;
        } catch (Exception e) {
            Main.getLog().error(e.getMessage());
            //response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return 304;
        }
    }

    @RequestMapping(value = "api/city/add/{city}", method = RequestMethod.POST)
    public int AddCity(@PathVariable("city") String cityName) {

        if(configProperties.isReadOnly()){
            return 403;
        }

        try{
            Main.getMySQLService().AddCity(cityName);
            return 202;
        }catch (Exception e){
            Main.getLog().error(e.getMessage());
            return 304;
        }
    }

    @RequestMapping(value = "api/city/delete/{city}", method = RequestMethod.DELETE)
    public int DeleteCity(@PathVariable("city")  String cityName) {

        if(configProperties.isReadOnly()){
            return 403;
        }

        try {
            Main.getMySQLService().DeleteCity(cityName);
            Main.getMongoDBService().deleteCities(cityName);
            return 202;
        }catch (Exception e){
            Main.getLog().error(e.getMessage());
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
            Main.getMongoDBService().deleteDate(cityData.getName(), cityData.getDate());
            return 202;
        }catch (Exception e){
            Main.getLog().error(e.getMessage());
            return 304;
        }
    }

    private List<CityMySQL> GetData(int week){
        List<CityMySQL> cities = Main.getMySQLService().GetAllCities();
        mainPage.findWeather(cities, week);
        mainPage.calculateAverageTemp(cities);
        mainPage.UnixTimeToDate(cities);
        return cities;
    }

}
