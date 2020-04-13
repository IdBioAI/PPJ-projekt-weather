package com.weather.weather;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
class WeatherApplicationTests {


    @Test
    public void givenUrl_whenPostRequest_thenFindPostResponse()
            throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/post");

        ResultMatcher contentMatcher = MockMvcResultMatchers.content()
                .string("POST Response");


    }

    @Test
    void contextLoads() {
    }

}
