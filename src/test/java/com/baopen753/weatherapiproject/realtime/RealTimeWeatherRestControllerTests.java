package com.baopen753.weatherapiproject.realtime;

import com.baopen753.weatherapiproject.GeolocationException;
import com.baopen753.weatherapiproject.GeolocationService;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.exception.LocationNotFoundException;
import com.baopen753.weatherapiproject.locationservices.repository.LocationRepository;
import com.baopen753.weatherapiproject.locationservices.service.LocationService;
import com.baopen753.weatherapiproject.realtimeservices.dto.RealtimeWeatherDto;
import com.baopen753.weatherapiproject.realtimeservices.entity.RealtimeWeather;
import com.baopen753.weatherapiproject.realtimeservices.restcontroller.RealtimeWeatherRestController;
import com.baopen753.weatherapiproject.realtimeservices.service.RealtimeWeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @Mock
    LocationService locationService;

    @MockBean
    LocationRepository locationRepository;



    /*
     *   Guide: in order to test with Exception, we need to mock environment test (Service layer) with invalid input which is compatible with the Exception
     *
     *   In this case: we need to crete Location object with invalid properties (country_code, city_name) to throw LocationNotFoundException(countryCode,cityName);
     */
    @Test
    public void testGetRealtimeShouldReturn404NotFound() throws Exception {

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
    public void testGetRealtimeShouldReturn200OK() throws Exception {

        Location location = new Location();
        location.setCountryCode("VN");
        location.setCityName("Ho Chi Minh City");
        location.setRegionName("South");
        location.setCountryName("The Republic Socialist of Vietnam");

        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setTemperature(60);
        realtimeWeather.setHumidity(60);
        realtimeWeather.setLastUpdated(new Date());
        realtimeWeather.setPrecipitation(60);
        realtimeWeather.setStatus("2nd updated from VN_HCM");
        realtimeWeather.setWindSpeed(60);
        realtimeWeather.setLocation(location);


        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);
        Mockito.when(realtimeWeatherService.getRealtimeWeatherByLocation(location)).thenReturn(realtimeWeather);

        String expectedLocationRespose = location.getRegionName() + ", " + location.getCityName() + ", " + location.getCountryName();


        mockMvc.perform(get(ENDPOINT)).andExpect(status().isOk()).andExpect(jsonPath("$.location", is(expectedLocationRespose))).andDo(print());


    }

    @Test
    public void testGetRealtimeByCodeShouldReturn404NotFound() throws Exception {
        String nonExistCode = "nonExistCode";

        LocationNotFoundException exception = new LocationNotFoundException("Not found location with: " + nonExistCode);
        Mockito.when(realtimeWeatherService.getRealtimeWeatherByCode(Mockito.anyString())).thenThrow(exception);

        mockMvc.perform(get(ENDPOINT + "/" + nonExistCode)).andExpect(status().isNotFound()).andExpect(jsonPath("$.error[0]", is(exception.getMessage()))).andDo(print());
    }

    @Test
    public void testGetRealtimeByCodeShouldReturn200OK() throws Exception {
        String existedCode = "VN_HN";
        Location location = locationService.findLocationByCode(existedCode);

        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setTemperature(65);
        realtimeWeather.setHumidity(65);
        realtimeWeather.setLastUpdated(new Date());
        realtimeWeather.setPrecipitation(65);
        realtimeWeather.setStatus("1nd updated from VN_HN");
        realtimeWeather.setWindSpeed(65);
        realtimeWeather.setLocationCode(existedCode);
        realtimeWeather.setLocation(location);

        Mockito.when(realtimeWeatherService.getRealtimeWeatherByCode(existedCode)).thenReturn(realtimeWeather);

        mockMvc.perform(get(ENDPOINT + "/" + existedCode)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testUpdateRealtimeByCodeShouldReturn200Ok() throws Exception {

        // RealtimeWeather code with existing location_code
        String locationCode = "USA_LA";
        String requestURI = ENDPOINT + "/" + locationCode;

        RealtimeWeatherDto dto = new RealtimeWeatherDto();
        dto.setHumidity(61);
        dto.setPrecipitation(62);
        dto.setTemperature(50);
        dto.setWindSpeed(64);
        dto.setLastUpdated(new Date());
        dto.setStatus("Cloudy");

        Location location = new Location();
        location.setCityName("Los Angeles");
        location.setRegionName("California");
        location.setCountryName("United States of America");
        location.setCode("USA_LA");
        location.setCountryCode("USA");

        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setTemperature(dto.getTemperature());
        realtimeWeather.setHumidity(dto.getHumidity());
        realtimeWeather.setPrecipitation(dto.getPrecipitation());
        realtimeWeather.setStatus(dto.getStatus());
        realtimeWeather.setWindSpeed(dto.getWindSpeed());
        realtimeWeather.setLocationCode(locationCode);
        realtimeWeather.setLocation(location);

      //  location.se

        String requestBody = objectMapper.writeValueAsString(dto);

        Mockito.when(realtimeWeatherService.updateRealtimeWeatherByCode(Mockito.eq(locationCode), Mockito.any())).thenReturn(realtimeWeather);
        mockMvc.perform(put(requestURI).contentType(REQUEST_CONTENT_TYPE).content(requestBody))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    public void testUpdatedRealtimeByCodeShouldReturn404NotFound() throws Exception {

        String nonExistedLocationCode = "HIHI";
        String requestURI = ENDPOINT + "/" + nonExistedLocationCode;

        LocationNotFoundException exception = new LocationNotFoundException("Not found location with: " + nonExistedLocationCode);

        RealtimeWeatherDto dto = new RealtimeWeatherDto();
        dto.setHumidity(61);
        dto.setPrecipitation(62);
        dto.setTemperature(40);
        dto.setWindSpeed(64);
        dto.setLastUpdated(new Date());
        dto.setStatus("Cloudy");

        String bodyContent = objectMapper.writeValueAsString(dto);

        Mockito.doThrow(exception).when(realtimeWeatherService).updateRealtimeWeatherByCode(Mockito.eq(nonExistedLocationCode), Mockito.any());

        mockMvc.perform(put(requestURI).contentType(REQUEST_CONTENT_TYPE).content(bodyContent))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    public void testUpdatedRealtimeByCodeShouldReturn400BadRequest() throws Exception {

        String nonExistedLocationCode = "HIHI";
        String requestURI = ENDPOINT + "/" + nonExistedLocationCode;

        LocationNotFoundException exception = new LocationNotFoundException("Not found location with: " + nonExistedLocationCode);

        RealtimeWeatherDto dto = new RealtimeWeatherDto();
        dto.setHumidity(61);
        dto.setPrecipitation(62);
        dto.setTemperature(60);                 // violating Temperature range
        dto.setWindSpeed(64);
        dto.setLastUpdated(new Date());
        dto.setStatus("Cloudy");

        String bodyContent = objectMapper.writeValueAsString(dto);

        Mockito.doThrow(exception).when(realtimeWeatherService).updateRealtimeWeatherByCode(Mockito.eq(nonExistedLocationCode), Mockito.any());

        mockMvc.perform(put(requestURI).contentType(REQUEST_CONTENT_TYPE).content(bodyContent))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


}
