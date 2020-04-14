package com.weather.weather;

import com.weather.model.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@ComponentScan(basePackages={"com.weather.model"})
@EnableAutoConfiguration
public class MySqlService {
// spring data jpa repozitáře (repozitories) (criteria API)
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    MySqlService mySqlService;

    @Autowired
    State state;

    public MySqlService(){
    }

    @PostConstruct
    public void Init() {
        WeatherApplication.SetMySqlService(mySqlService);
    }

    public List<State> SelectState(){

        try {
        return jdbcTemplate.query(
                "SELECT * FROM state",
                (rs, rowNum) ->
                        new State(
                                rs.getInt("id"),
                                rs.getString("stateName")
                        )
        );
        }catch (Exception e){
            WeatherApplication.WriteToLog("Error while select: " + e.getMessage());
        }

        return null;

    }

    public boolean Update(State state) {

        try {
           jdbcTemplate.update( "UPDATE state SET stateName = ? WHERE id = ?",
                                state.getStateName(), state.getId());
            return true;
        }catch (Exception e){
            WeatherApplication.WriteToLog("Error while update: " + e.getMessage());
        }

        return false;

    }

    public State GetState(){
        return state;
    }

}
