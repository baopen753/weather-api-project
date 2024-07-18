package com.baopen753.weatherapiproject.daily;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.CoreMatchers.is;

import com.baopen753.weatherapiproject.GeolocationException;
import com.baopen753.weatherapiproject.GeolocationService;
import com.baopen753.weatherapiproject.dailyweatherservices.dto.DailyWeatherDto;
import com.baopen753.weatherapiproject.dailyweatherservices.entity.DailyWeather;
import com.baopen753.weatherapiproject.dailyweatherservices.entity.DailyWeatherId;
import com.baopen753.weatherapiproject.dailyweatherservices.restcontroller.DailyWeatherRestController;
import com.baopen753.weatherapiproject.dailyweatherservices.service.DailyWeatherService;
import com.baopen753.weatherapiproject.hourlyweatherservices.exception.BadRequestException;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.exception.LocationNotFoundException;
import com.baopen753.weatherapiproject.locationservices.service.LocationService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

@WebMvcTest(DailyWeatherRestController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = true)
public class DailyWeatherRestControllerTests {


    private static final String ENDPOINT = "/api/v1/daily";
    private static final String REQUEST_CONTENT_TYPE = "application/json";
    private static final String X_CURRENT_HOUR = "X-Current-Hour";

    @MockBean
    private DailyWeatherService dailyWeatherService;

    @MockBean
    private GeolocationService geolocationService;

    @MockBean
    private LocationService locationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testGetDailyWeathersByIpAddressShouldReturn200Ok() throws Exception {

        Location locationMappedFromIp = new Location();
        locationMappedFromIp.setRegionName("Middle");
        locationMappedFromIp.setCountryCode("VN");
        locationMappedFromIp.setCityName("Ho Chi Minh City");
        locationMappedFromIp.setCountryName("The Socialist Republic of Vietnam");

        DailyWeatherId id1 = DailyWeatherId.builder()
                .month(12)
                .dayOfMonth(1)
                .location(locationMappedFromIp)
                .build();

        DailyWeatherId id2 = DailyWeatherId.builder()
                .month(12)
                .dayOfMonth(2)
                .location(locationMappedFromIp)
                .build();

        DailyWeather dailyWeather1 = DailyWeather.builder()
                .dailyWeatherId(id1)
                .precipitation(50)
                .maxTemperature(70)
                .minTemperature(40)
                .status("Cold")
                .build();

        DailyWeather dailyWeather2 = DailyWeather.builder()
                .dailyWeatherId(id2)
                .precipitation(60)
                .maxTemperature(80)
                .minTemperature(50)
                .status("Colder")
                .build();

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(locationMappedFromIp);
        Mockito.when(dailyWeatherService.getDailyWeathersByLocation(locationMappedFromIp)).thenReturn(List.of(dailyWeather1, dailyWeather2));

        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/api/v1/daily")))
                .andExpect(jsonPath("$._links.realtime.href", is("http://localhost/api/v1/realtime")))
                .andExpect(jsonPath("$._links.hourly_forecast.href", is("http://localhost/api/v1/hourly")))
                .andExpect(jsonPath("$._links.fully_forecast.href", is("http://localhost/api/v1/fully")))
                .andDo(print());
    }

    @Test
    public void testGetDailyWeathersByIpAddressShouldReturn204NoContent() throws Exception {
        String locationCode = "USA_LA";   // location has no content about daily weather at the moment

        Location locationMappedFromIp = new Location();
        locationMappedFromIp.setCode(locationCode);

        Mockito.when(locationService.findLocationByCode(Mockito.anyString())).thenReturn(locationMappedFromIp);
        Mockito.when(dailyWeatherService.getDailyWeathersByLocation(locationMappedFromIp)).thenReturn(Collections.EMPTY_LIST);

        mockMvc.perform(get(ENDPOINT)).andExpect(status().isNoContent()).andDo(print());
    }

    @Test
    public void testGetDailyWeatherByIpAddressShouldReturn400BadRequests() throws Exception {
        String invalidIpAddress = "1111111";

        GeolocationException exception = new GeolocationException(invalidIpAddress);
        Location location = geolocationService.getLocation(invalidIpAddress);

        Mockito.when(dailyWeatherService.getDailyWeathersByLocation(location)).thenThrow(exception);

        mockMvc.perform(get(ENDPOINT)).andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    public void testGetDailyWeatherByIpAddressShouldReturn404NotFound() throws Exception {
        String noManagedIpAddress = "1.1.1.1";
        Location locationFromIp = geolocationService.getLocation(noManagedIpAddress);

        LocationNotFoundException exception = new LocationNotFoundException(noManagedIpAddress);

        Mockito.when(geolocationService.getLocation(noManagedIpAddress)).thenReturn(locationFromIp);
        Mockito.when(dailyWeatherService.getDailyWeathersByLocation(locationFromIp)).thenThrow(exception);


        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    public void testGetDailyWeatherByCodeShouldReturn200Ok() throws Exception {
        String locationCode = "VN_HCM";
        String requestURI = ENDPOINT + "/" + locationCode;

        Location locationMappedFromCode = new Location();
        locationMappedFromCode.setCode(locationCode);
        locationMappedFromCode.setRegionName("Middle");
        locationMappedFromCode.setCountryCode("VN");
        locationMappedFromCode.setCityName("Ho Chi Minh City");
        locationMappedFromCode.setCountryName("The Socialist Republic of Vietnam");

        DailyWeatherId id1 = DailyWeatherId.builder()
                .month(12)
                .dayOfMonth(1)
                .location(locationMappedFromCode)
                .build();

        DailyWeatherId id2 = DailyWeatherId.builder()
                .month(12)
                .dayOfMonth(2)
                .location(locationMappedFromCode)
                .build();


        DailyWeather dailyWeather1 = DailyWeather.builder()
                .dailyWeatherId(id1)
                .precipitation(50)
                .maxTemperature(70)
                .minTemperature(40)
                .status("Cold")
                .build();

        DailyWeather dailyWeather2 = DailyWeather.builder()
                .dailyWeatherId(id2)
                .precipitation(60)
                .maxTemperature(80)
                .minTemperature(50)
                .status("Colder")
                .build();


        Mockito.when(locationService.findLocationByCode(Mockito.anyString())).thenReturn(locationMappedFromCode);
        Mockito.when(dailyWeatherService.getDailyWeathersByCode(Mockito.anyString())).thenReturn(List.of(dailyWeather1, dailyWeather2));

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/api/v1/daily/" + locationCode)))
                .andExpect(jsonPath("$._links.realtime.href", is("http://localhost/api/v1/realtime/" + locationCode)))
                .andExpect(jsonPath("$._links.hourly_forecast.href", is("http://localhost/api/v1/hourly/" + locationCode)))
                .andExpect(jsonPath("$._links.fully_forecast.href", is("http://localhost/api/v1/fully/" + locationCode)))
                .andDo(print());
    }

    @Test
    public void testGetDailyWeatherByCodeShouldReturn204NoContent() throws Exception {
        String locationCodeWithNoDailyWeather = "USA_LA";
        String requestURI = ENDPOINT + "/" + locationCodeWithNoDailyWeather;

        Location location = new Location();
        location.setCode(locationCodeWithNoDailyWeather);

        Mockito.when(locationService.findLocationByCode(Mockito.anyString())).thenReturn(location);
        Mockito.when(dailyWeatherService.getDailyWeathersByCode(Mockito.anyString())).thenReturn(Collections.EMPTY_LIST);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void testGetDailyWeatherByCodeShouldReturn404NotFound() throws Exception {
        String locationCode = "HIHI";
        String requestURI = ENDPOINT + "/" + locationCode;

        Location location = new Location();
        location.setCode(locationCode);

        LocationNotFoundException exception = new LocationNotFoundException(locationCode);
        Mockito.when(dailyWeatherService.getDailyWeathersByCode(Mockito.anyString())).thenThrow(exception);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    public void testUpdateDailyWeatherShouldReturn200Ok() throws Exception {
        String locationCode = "VN_HN";
        String requestURI = ENDPOINT + "/" + locationCode;

        Location locationMappedFromCode = new Location();
        locationMappedFromCode.setCode(locationCode);
        locationMappedFromCode.setRegionName("North");
        locationMappedFromCode.setCountryCode("VN");
        locationMappedFromCode.setCityName("Hanoi");
        locationMappedFromCode.setCountryName("The Socialist Republic of Vietnam");

        DailyWeatherDto dto1 = DailyWeatherDto.builder()
                .month(12)
                .dayOfMonth(2)
                .minTemperature(30)
                .maxTemperature(45)
                .precipitation(70)
                .status("Cosy")
                .build();

        DailyWeatherDto dto2 = DailyWeatherDto.builder()
                .month(12)
                .dayOfMonth(3)
                .minTemperature(30)
                .maxTemperature(45)
                .precipitation(70)
                .status("Cosy")
                .build();

        List<DailyWeatherDto> listFromRequest = List.of(dto1, dto2);
        String requestBody = objectMapper.writeValueAsString(listFromRequest);

        DailyWeatherId id1 = DailyWeatherId.builder()
                .location(locationMappedFromCode)
                .month(12)
                .dayOfMonth(2)
                .build();

        DailyWeatherId id2 = DailyWeatherId.builder()
                .location(locationMappedFromCode)
                .month(12)
                .dayOfMonth(3)
                .build();

        DailyWeather dailyWeather1 = DailyWeather.builder()
                .dailyWeatherId(id1)
                .minTemperature(30)
                .maxTemperature(45)
                .precipitation(70)
                .status("Cosy")
                .build();

        DailyWeather dailyWeather2 = DailyWeather.builder()
                .dailyWeatherId(id2)
                .minTemperature(30)
                .maxTemperature(45)
                .precipitation(70)
                .status("Cosy")
                .build();

        List<DailyWeather> dailyWeatherList = List.of(dailyWeather1, dailyWeather2);
        locationMappedFromCode.setDailyWeatherList(dailyWeatherList);

        Mockito.when(dailyWeatherService.updateDailyWeathersByCode(Mockito.anyString(), Mockito.anyList())).thenReturn(dailyWeatherList);
        mockMvc.perform(put(requestURI).contentType(REQUEST_CONTENT_TYPE).content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/api/v1/daily/" + locationCode)))
                .andExpect(jsonPath("$._links.realtime.href", is("http://localhost/api/v1/realtime/" + locationCode)))
                .andExpect(jsonPath("$._links.hourly_forecast.href", is("http://localhost/api/v1/hourly/" + locationCode)))
                .andExpect(jsonPath("$._links.fully_forecast.href", is("http://localhost/api/v1/fully/" + locationCode)))
                .andDo(print());
    }

    @Test
    public void testUpdateDailyWeatherShouldReturn404NotFound() throws Exception {
        String locationCode = "VN_HIHI";
        String requestURI = ENDPOINT + "/" + locationCode;

        Location locationMappedFromCode = new Location();
        locationMappedFromCode.setCode(locationCode);
        locationMappedFromCode.setRegionName("North");
        locationMappedFromCode.setCountryCode("VN");
        locationMappedFromCode.setCityName("Hanoi");
        locationMappedFromCode.setCountryName("The Socialist Republic of Vietnam");

        DailyWeatherDto dto1 = DailyWeatherDto.builder()
                .month(12)
                .dayOfMonth(2)
                .minTemperature(30)
                .maxTemperature(45)
                .precipitation(70)
                .status("Cosy")
                .build();

        DailyWeatherDto dto2 = DailyWeatherDto.builder()
                .month(12)
                .dayOfMonth(3)
                .minTemperature(30)
                .maxTemperature(45)
                .precipitation(70)
                .status("Cosy")
                .build();

        List<DailyWeatherDto> listFromRequest = List.of(dto1, dto2);
        String requestBody = objectMapper.writeValueAsString(listFromRequest);

        LocationNotFoundException exception = new LocationNotFoundException(locationCode);
        Mockito.when(dailyWeatherService.updateDailyWeathersByCode(Mockito.anyString(), Mockito.anyList())).thenThrow(exception);

        mockMvc.perform(put(requestURI).contentType(REQUEST_CONTENT_TYPE).content(requestBody))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testUpdateDailyWeatherShouldReturn400BadRequestDueToEmptyData() throws Exception {
        String locationCode = "VN_HN";
        String requestURI = ENDPOINT + "/" + locationCode;

        BadRequestException exception = new BadRequestException("Required body request");

        String bodyRequest = objectMapper.writeValueAsString(Collections.EMPTY_LIST);

        Mockito.when(dailyWeatherService.updateDailyWeathersByCode(Mockito.anyString(), Mockito.anyList())).thenThrow(exception);

        mockMvc.perform(put(requestURI).contentType(REQUEST_CONTENT_TYPE).content(bodyRequest))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    public void testUpdatedDailyWeatherShouldReturn400BadRequestDueToInvalidData() throws Exception {
        String locationCode = "VN_HN";
        String requestURI = ENDPOINT + "/" + locationCode;

        Location locationMappedFromCode = new Location();
        locationMappedFromCode.setCode(locationCode);
        locationMappedFromCode.setRegionName("North");
        locationMappedFromCode.setCountryCode("VN");
        locationMappedFromCode.setCityName("Hanoi");
        locationMappedFromCode.setCountryName("The Socialist Republic of Vietnam");

        DailyWeatherDto dto1 = DailyWeatherDto.builder()
                .month(12)
                .dayOfMonth(2)
                .minTemperature(30)
                .maxTemperature(45)
                .precipitation(70)
                .status("Cosy")
                .build();

        DailyWeatherDto dto2 = DailyWeatherDto.builder()
                .month(12)
                .dayOfMonth(3)
                .minTemperature(90)   // invalid min temperature input:  [-50,49]
                .maxTemperature(90)   // invalid max temperature input:  [-49,50]
                .precipitation(70)
                .status("Cosy")
                .build();

        List<DailyWeatherDto> listFromRequest = List.of(dto1, dto2);
        String requestBody = objectMapper.writeValueAsString(listFromRequest);

        LocationNotFoundException exception = new LocationNotFoundException(locationCode);
        Mockito.when(dailyWeatherService.updateDailyWeathersByCode(Mockito.anyString(), Mockito.anyList())).thenThrow(exception);

        mockMvc.perform(put(requestURI).contentType(REQUEST_CONTENT_TYPE).content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

}