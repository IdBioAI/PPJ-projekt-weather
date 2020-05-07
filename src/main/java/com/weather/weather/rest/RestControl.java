package com.weather.weather.rest;


import com.weather.weather.Main;
import com.weather.weather.view.mainPage;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class RestControl {

   @GetMapping("/")
    String GetStateInfo() {
       mainPage m = new mainPage();
        return m.ShowMainPage();

    }

    @PostMapping("/state/change")
    void UpdateState(HttpServletResponse response, @RequestParam("State")  String state) {
        Main.getMySQLService().ChangeState(state);
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @PostMapping("/city/add")
    void AddCity(HttpServletResponse response,@RequestParam("City")  String city) {
        Main.getMySQLService().AddCity(city);
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @PostMapping("/city/delete")
    void DeleteCity(HttpServletResponse response,@RequestParam("City")  String city) {
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
        Main.getMongoDBService().deleteDate(city, date);
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
