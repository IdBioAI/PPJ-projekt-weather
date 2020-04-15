package com.weather.rest;

import com.weather.model.State;
import com.weather.view.mainPage;
import com.weather.weather.WeatherApplication;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestControl {

   @GetMapping("/")
    String GetStateInfo() {
       mainPage m = new mainPage();
        return m.ShowMainPage();

    }

    @PostMapping("/state/change")
    String UpdateState(@RequestParam("State")  String city) {
        return GetStateInfo();

    }

    @PostMapping("/city/add")
    String AddCity(@RequestParam("City")  String city) {
        WeatherApplication.GetMongoDBService().AddCity(city);
        return GetStateInfo();

    }

    @DeleteMapping("/city/delete")
    String DeleteCity(@RequestParam("City")  String city) {
        WeatherApplication.GetMongoDBService().DeleteCity(city);
        return GetStateInfo();
    }

}
