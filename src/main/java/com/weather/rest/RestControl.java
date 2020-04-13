package com.weather.rest;

import com.weather.models.State;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestControl {

   @GetMapping("/state")
    String GetStateInfo() {
        return "State info";

    }

    @PostMapping("/state")
    String UpdateState(@RequestBody State state) {
        return "Update state";
    }

}
