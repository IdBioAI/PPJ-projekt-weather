package com.weather.weather.rest;


import com.weather.weather.Main;
import com.weather.weather.configurations.ConfigProperties;
import com.weather.weather.view.MainPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class RestControl {

    @Autowired
    MainPage mainPage;
    @Autowired
    ConfigProperties configProperties;

   @GetMapping("/")
    String GetStateInfo() {
        return mainPage.ShowMainPage(0);

    }
    @GetMapping("/week")
    String getWeekInfo() {
        return mainPage.ShowMainPage(1);
    }

    @GetMapping("/week2")
    String getWeek2Info() {
        return mainPage.ShowMainPage(2);
    }

    @PostMapping("/state/change")
    void UpdateState(HttpServletResponse response, @RequestParam("State")  String state) {

        if(configProperties.isReadOnly()){ return; }

        Main.getMySQLService().ChangeState(state);
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @PostMapping("/city/add")
    void AddCity(HttpServletResponse response,@RequestParam("City")  String city) {

        if(configProperties.isReadOnly()){ return; }

        Main.getMySQLService().AddCity(city);
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @PostMapping("/city/delete")
    void DeleteCity(HttpServletResponse response,@RequestParam("City")  String city) {

        if(configProperties.isReadOnly()){ return; }

        Main.getMySQLService().DeleteCity(city);
        Main.getMongoDBService().deleteCities(city);
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/temp/delete")
    void DeleteTemp(HttpServletResponse response,@RequestParam("City")  String city, @RequestParam("Date")  String date) {

        if(configProperties.isReadOnly()){ return; }

        Main.getMongoDBService().deleteDate(city, date);
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
