package com.baopen753.weatherapiproject.realtime;

import com.baopen753.weatherapiproject.GeolocationService;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.exception.LocationNotFoundException;
import com.baopen753.weatherapiproject.realtimeservices.entity.RealtimeWeather;
import com.baopen753.weatherapiproject.realtimeservices.restcontroller.RealtimeWeatherRestController;
import com.baopen753.weatherapiproject.realtimeservices.service.RealtimeWeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



/*   This class is used to test the function of RealtimeWeather
 *
 *   Note: We might skip locations mapping from IpAddresses because of having not hosting yet
 */


@WebMvcTest(RealtimeWeatherRestController.class)
public class RealTimeWeatherRestControllerTests {

    private static final String ENDPOINT = "/api/v1/realtime";
    private static final String REQUEST_CONTENT_TYPE = "application/json";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RealtimeWeatherService realtimeWeatherService;

    @MockBean
    GeolocationService geolocationService;

    @Test
    @Disabled
    public void testGetShouldReturn400BadRequest() throws Exception {

    }


    /*
     *   Guide: in order to test with Exception, we need to mock environment test (Service layer) with invalid input which is compatible with the Exception
     *
     *   In this case: we need to crete Location object with invalid properties (country_code, city_name) to throw LocationNotFoundException(countryCode,cityName);
     */
    @Test
    public void testGetShouldReturn404NotFound() throws Exception {

        // Location with invalid info
        Location location = new Location();
        location.setCountryCode("VN");
        location.setCityName("Longan");

        LocationNotFoundException exception = new LocationNotFoundException(location.getCountryCode(), location.getCityName());

        // These following lines of code stem from Controller layer .
        // In order to test 404NotFoundException, all we need to do is simulating both behaviors from GeolocationService  & RealtimeWeatherService

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);  // we do not need to input Ipaddress as parameter of getLocation() ---> navigate to return with prepared invalid Location

        // LocationNotFoundException occurs within RealtimeWeatherService.get...ByLocation()     // we need to specify Exception object as Throw()
        Mockito.when(realtimeWeatherService.getRealtimeWeatherByLocation(location)).thenThrow(exception);

        mockMvc.perform(get(ENDPOINT)).andExpect(status().isNotFound()).andDo(print());

    }


    @Test
    @Disabled
    public void testGetShouldReturn200Ok() throws Exception {
        // Location with invalid info
        Location location = new Location();
        location.setCountryCode("USA");
        location.setCityName("Los Angeles");

        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setHumidity(60);
        realtimeWeather.setPrecipitation(60);
        realtimeWeather.setTemperature(60);
        realtimeWeather.setWindSpeed(60);
        realtimeWeather.setLastUpdated(new Date());
        realtimeWeather.setStatus("2nd updated from USA_LA");
        realtimeWeather.setLocationCode("USA_LA");
        realtimeWeather.setLocation(location);

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);
        Mockito.when(realtimeWeatherService.getRealtimeWeatherByLocation(location)).thenReturn(realtimeWeather);

        mockMvc.perform(get(ENDPOINT)).andExpect(status().isOk()).andDo(print());

    }


    @Test
    public void testGetRealtimeShouldReturn200OK() throws Exception {
        String validIp = "171.252.153.255";   // private ip

        Location location = new Location();
        location.setCountryCode("VN");
        location.setCityName("Ho Chi Minh City");

        Mockito.when(geolocationService.getLocation(validIp)).thenReturn(location);

        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andDo(print());


    }


}
