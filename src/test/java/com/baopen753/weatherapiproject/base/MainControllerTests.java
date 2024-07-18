package com.baopen753.weatherapiproject.base;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MainController.class)
public class MainControllerTests {

    private final static String BASE_URL = "/";
    private final static String CONTENT_TYPE = "application/json";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testBaseUri() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.location_by_ip_url", is("http://localhost/api/v1/locations?pageSize={pageSize}&pageNum={pageNum}")))
                .andExpect(jsonPath("$.location_by_code_url", is("http://localhost/api/v1/locations/{code}")))
                .andExpect(jsonPath("$.realtime_weather_by_ip_url", is("http://localhost/api/v1/realtime")))
                .andExpect(jsonPath("$.realtime_weather_by_code_url", is("http://localhost/api/v1/realtime/{code}")))
                .andExpect(jsonPath("$.hourly_weather_by_ip_url", is("http://localhost/api/v1/hourly")))
                .andExpect(jsonPath("$.hourly_weather_by_code_url", is("http://localhost/api/v1/hourly/{code}")))
                .andExpect(jsonPath("$.daily_weather_by_ip_url", is("http://localhost/api/v1/daily")))
                .andExpect(jsonPath("$.daily_weather_by_code_url", is("http://localhost/api/v1/daily/{code}")))
                .andExpect(jsonPath("$.fully_weather_by_ip_url", is("http://localhost/api/v1/fully")))
                .andExpect(jsonPath("$.fully_weather_by_code_url", is("http://localhost/api/v1/fully/{code}")))
                .andDo(print());
    }
}
