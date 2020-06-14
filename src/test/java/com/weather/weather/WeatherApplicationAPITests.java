package com.weather.weather;

import com.weather.weather.rest.RestAPIControl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class WeatherApplicationAPITests {

    //@Autowired
    //private MockMvc mockMvc;
    @MockBean
    private RestAPIControl restAPIControl;

    @Test
    public void updateStateTest() throws Exception{


    }

}
