package com.weather.rest;

import com.weather.model.State;
import com.weather.view.mainPage;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestControl {

   @GetMapping("/state")
    String GetStateInfo() {
       mainPage m = new mainPage();
        return m.ShowMainPage();

    }

    @PostMapping("/state")
    String UpdateState(@RequestBody State state) {

       return "Update state";

    }

}
