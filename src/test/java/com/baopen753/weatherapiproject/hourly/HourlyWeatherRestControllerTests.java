package com.baopen753.weatherapiproject.hourly;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.baopen753.weatherapiproject.GeolocationService;
import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeather;
import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeatherId;
import com.baopen753.weatherapiproject.hourlyweatherservices.restcontroller.HourlyWeatherRestController;
import com.baopen753.weatherapiproject.hourlyweatherservices.service.HourlyWeatherService;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.service.LocationService;
import com.baopen753.weatherapiproject.realtimeservices.service.RealtimeWeatherService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(HourlyWeatherRestController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
public class HourlyWeatherRestControllerTests {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HourlyWeatherService hourlyWeatherService;

    @MockBean
    private GeolocationService geolocationService;


    private static final String END_POINT_PATH = "/api/v1/hourly";
    private static final String X_CURRENT_HOUR = "X-Current-Hour";


    @Test
    public void testGetHourlyWeatherForecastByIpShouldReturn200Ok() throws Exception {

        int currentOfHour = 17;

        Location locationMappedFromIp = new Location();
        locationMappedFromIp.setRegionName("Middle");
        locationMappedFromIp.setCountryCode("VN");
        locationMappedFromIp.setCityName("Ho Chi Minh City");
        locationMappedFromIp.setCountryName("The Socialist Republic of Vietnam");

        // perform a test environment
        HourlyWeatherId hourlyWeatherId1 = HourlyWeatherId.builder()
                .hourOfDay(17)
                .location(locationMappedFromIp)
                .build();

        HourlyWeather hourlyWeather1 = HourlyWeather.builder()
                .hourlyWeatherId(hourlyWeatherId1)
                .precipitation(50)
                .temperature(50)
                .status("Rainy")
                .build();


        HourlyWeatherId hourlyWeatherId2 = HourlyWeatherId.builder()
                .hourOfDay(18)
                .location(locationMappedFromIp)
                .build();

        HourlyWeather hourlyWeather2 = HourlyWeather.builder()
                .hourlyWeatherId(hourlyWeatherId2)
                .precipitation(50)
                .temperature(50)
                .status("Rainy")
                .build();


        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(locationMappedFromIp);
        Mockito.when(hourlyWeatherService.getHourlyWeatherByLocation(locationMappedFromIp, currentOfHour)).thenReturn(List.of(hourlyWeather1, hourlyWeather2));

        mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR,String.valueOf(currentOfHour)))
                .andExpect(status().isOk())
                .andDo(print());


    }



}
