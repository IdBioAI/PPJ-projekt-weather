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

    @PutMapping("/state/change")
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

    @DeleteMapping("/city/delete")
    void DeleteCity(HttpServletResponse response,@RequestParam("City")  String city) {
        Main.getMySQLService().DeleteCity(city);
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
