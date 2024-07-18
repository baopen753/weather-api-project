package com.baopen753.weatherapiproject.fully;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.baopen753.weatherapiproject.GeolocationException;
import com.baopen753.weatherapiproject.GeolocationService;
import com.baopen753.weatherapiproject.dailyweatherservices.dto.DailyWeatherDto;
import com.baopen753.weatherapiproject.fullyweatherservices.dto.FullyWeatherDto;
import com.baopen753.weatherapiproject.fullyweatherservices.mapper.FullyWeatherMapper;
import com.baopen753.weatherapiproject.fullyweatherservices.restcontroller.FullyWeatherRestController;
import com.baopen753.weatherapiproject.fullyweatherservices.service.FullyWeatherService;
import com.baopen753.weatherapiproject.hourlyweatherservices.dto.HourlyWeatherDto;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.exception.LocationNotFoundException;
import com.baopen753.weatherapiproject.realtimeservices.dto.RealtimeWeatherDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@WebMvcTest(FullyWeatherRestController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = true)
public class FullyWeatherRestControllerTests {


    private static final String END_POINT_PATH = "/api/v1/fully";
    private static final String X_CURRENT_HOUR = "X-Current-Hour";
    private static final String REQUEST_CONTENT_TYPE = "application/json";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeolocationService geolocationService;

    @MockBean
    private FullyWeatherService fullyWeatherService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testGetFullyWeathersByIpAddressShouldReturn200OK() throws Exception {

        Integer currentHour = 10;

        Location testedLocation = new Location();
        testedLocation.setCode("VN_HCM");
        testedLocation.setRegionName("South");
        testedLocation.setCountryCode("VN");
        testedLocation.setCountryName("Socialist Republic of Vietnam");
        testedLocation.setCityName("Ho Chi Minh City");
        testedLocation.setEnabled(true);

        // realtime
        RealtimeWeatherDto realtimeWeatherDto = new RealtimeWeatherDto();
        realtimeWeatherDto.setTemperature(60);
        realtimeWeatherDto.setHumidity(60);
        realtimeWeatherDto.setLastUpdated(new Date());
        realtimeWeatherDto.setPrecipitation(60);
        realtimeWeatherDto.setStatus("2nd updated from VN_HCM");
        realtimeWeatherDto.setWindSpeed(60);
        realtimeWeatherDto.setLocationAddress(testedLocation.toString());


        // list hourly
        HourlyWeatherDto hourlyWeatherDto1 = HourlyWeatherDto.builder().status("Cool").hourOfDay(currentHour).precipitation(50).temperature(50).build();
        HourlyWeatherDto hourlyWeatherDto2 = HourlyWeatherDto.builder().status("Cool").hourOfDay(currentHour).precipitation(50).temperature(50).build();

        // list daily
        DailyWeatherDto dailyWeatherDto1 = DailyWeatherDto.builder().month(12).dayOfMonth(1).precipitation(60).maxTemperature(40).minTemperature(30).status("Cold").build();
        DailyWeatherDto dailyWeatherDto2 = DailyWeatherDto.builder().month(12).dayOfMonth(1).precipitation(60).maxTemperature(40).minTemperature(30).status("Cold").build();

        FullyWeatherDto result = new FullyWeatherDto();
        result.setRealtimeWeather(realtimeWeatherDto);
        result.setHourlyWeatherList(List.of(hourlyWeatherDto1, hourlyWeatherDto2));
        result.setDailyWeatherList(List.of(dailyWeatherDto1, dailyWeatherDto2));
        result.setLocation(testedLocation.toString());

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(testedLocation);
        Mockito.when(fullyWeatherService.getFullyWeatherByLocation(testedLocation, currentHour)).thenReturn(result);

        mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, String.valueOf(currentHour))).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testGetFullyWeathersByIpAddressShouldReturn400BadRequestDueToUnResolvedIpAddress() throws Exception {
        String unresolvedIpAddress = "1111";
        Integer currentHour = 10;

        GeolocationException exception = new GeolocationException(unresolvedIpAddress);
        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenThrow(exception);

        mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, String.valueOf(currentHour))).andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    public void testGetFullyWeathersByIpAddressShouldReturn404BadRequestDueTo404LocationNotFound() throws Exception {
        String notFoundIpAddress = "1.1.1.1";
        Integer currentHour = 10;

        Location testedLocation = new Location();
        testedLocation.setCode("VN_HCM");
        testedLocation.setRegionName("South");
        testedLocation.setCountryCode("VN");
        testedLocation.setCountryName("Socialist Republic of Vietnam");
        testedLocation.setCityName("Ho Chi Minh City");
        testedLocation.setEnabled(true);

        LocationNotFoundException exception = new LocationNotFoundException(notFoundIpAddress);

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenThrow(exception);
        Mockito.when(fullyWeatherService.getFullyWeatherByLocation(testedLocation, currentHour)).thenThrow(exception);

        mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, String.valueOf(currentHour))).andExpect(status().isNotFound()).andDo(print());
    }


    @Test
    public void testGetFullyWeatherByCodeShouldReturn200Ok() throws Exception {
        String validLocationCode = " VN_HN";
        Integer currentHour = 10;
        String requestURI = END_POINT_PATH + "/" + validLocationCode;

        Location testedLocation = new Location();
        testedLocation.setCode("VN_HCM");
        testedLocation.setRegionName("South");
        testedLocation.setCountryCode("VN");
        testedLocation.setCountryName("Socialist Republic of Vietnam");
        testedLocation.setCityName("Ho Chi Minh City");
        testedLocation.setEnabled(true);

        // realtime
        RealtimeWeatherDto realtimeWeatherDto = new RealtimeWeatherDto();
        realtimeWeatherDto.setTemperature(60);
        realtimeWeatherDto.setHumidity(60);
        realtimeWeatherDto.setLastUpdated(new Date());
        realtimeWeatherDto.setPrecipitation(60);
        realtimeWeatherDto.setStatus("2nd updated from VN_HCM");
        realtimeWeatherDto.setWindSpeed(60);
        realtimeWeatherDto.setLocationAddress(testedLocation.toString());


        // list hourly
        HourlyWeatherDto hourlyWeatherDto1 = HourlyWeatherDto.builder().status("Cool").hourOfDay(currentHour).precipitation(50).temperature(50).build();
        HourlyWeatherDto hourlyWeatherDto2 = HourlyWeatherDto.builder().status("Cool").hourOfDay(currentHour).precipitation(50).temperature(50).build();


        // list daily
        DailyWeatherDto dailyWeatherDto1 = DailyWeatherDto.builder().month(12).dayOfMonth(1).precipitation(60).maxTemperature(40).minTemperature(30).status("Cold").build();
        DailyWeatherDto dailyWeatherDto2 = DailyWeatherDto.builder().month(12).dayOfMonth(1).precipitation(60).maxTemperature(40).minTemperature(30).status("Cold").build();


        FullyWeatherDto result = new FullyWeatherDto();
        result.setRealtimeWeather(realtimeWeatherDto);
        result.setHourlyWeatherList(List.of(hourlyWeatherDto1, hourlyWeatherDto2));
        result.setDailyWeatherList(List.of(dailyWeatherDto1, dailyWeatherDto2));
        result.setLocation(testedLocation.toString());

        Mockito.when(fullyWeatherService.getFullyWeatherByCode(Mockito.anyString(), Mockito.anyInt())).thenReturn(result);

        mockMvc.perform(get(requestURI).header(X_CURRENT_HOUR, String.valueOf(currentHour)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testGetFullWeathersByCodeShouldReturn404NotFound() throws Exception {
        String invalidLocationCode = "HIHI";
        String requestURI = END_POINT_PATH + "/" + invalidLocationCode;

        LocationNotFoundException exception = new LocationNotFoundException(invalidLocationCode);
        Mockito.when(fullyWeatherService.getFullyWeatherByCode(Mockito.anyString(), Mockito.anyInt())).thenThrow(exception);

        mockMvc.perform(get(requestURI).header(X_CURRENT_HOUR, 12))
                .andExpect(status().isNotFound()).andDo(print());
    }


    @Test
    public void testUpdateFullWeathersByCodeShouldReturn200Ok() throws Exception {
        String locationCode = "VN_HCM";
        String requestURI = END_POINT_PATH + "/" + locationCode;

        Location testedLocation = new Location();
        testedLocation.setCode(locationCode);
        testedLocation.setRegionName("South");
        testedLocation.setCountryCode("VN");
        testedLocation.setCountryName("Socialist Republic of Vietnam");
        testedLocation.setCityName("Ho Chi Minh City");
        testedLocation.setEnabled(true);

        // realtime
        RealtimeWeatherDto realtimeWeatherDto = new RealtimeWeatherDto();
        realtimeWeatherDto.setTemperature(50);
        realtimeWeatherDto.setHumidity(60);
        realtimeWeatherDto.setLastUpdated(new Date());
        realtimeWeatherDto.setPrecipitation(60);
        realtimeWeatherDto.setStatus("2nd updated from VN_HCM");
        realtimeWeatherDto.setWindSpeed(60);
        realtimeWeatherDto.setLocationAddress(testedLocation.toString());


        // list hourly
        HourlyWeatherDto hourlyWeatherDto1 = HourlyWeatherDto.builder().status("Cool").hourOfDay(10).precipitation(40).temperature(40).build();
        HourlyWeatherDto hourlyWeatherDto2 = HourlyWeatherDto.builder().status("Cool").hourOfDay(11).precipitation(50).temperature(40).build();


        // list daily
        DailyWeatherDto dailyWeatherDto1 = DailyWeatherDto.builder().month(12).dayOfMonth(1).precipitation(60).maxTemperature(40).minTemperature(30).status("Cold").build();
        DailyWeatherDto dailyWeatherDto2 = DailyWeatherDto.builder().month(12).dayOfMonth(1).precipitation(60).maxTemperature(40).minTemperature(30).status("Cold").build();


        FullyWeatherDto fullyWeatherDto = new FullyWeatherDto();
        fullyWeatherDto.setRealtimeWeather(realtimeWeatherDto);
        fullyWeatherDto.setHourlyWeatherList(List.of(hourlyWeatherDto1, hourlyWeatherDto2));
        fullyWeatherDto.setDailyWeatherList(List.of(dailyWeatherDto1, dailyWeatherDto2));
        fullyWeatherDto.setLocation(testedLocation.toString());


        String requestBody = objectMapper.writeValueAsString(fullyWeatherDto);


        Location location = FullyWeatherMapper.INSTANCE.dtoToEntity(fullyWeatherDto);

        Mockito.when(fullyWeatherService.updateFullyWeatherByCode(locationCode, location)).thenReturn(location);

        mockMvc.perform(put(requestURI).content(requestBody).contentType(REQUEST_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andDo(print());


    }

    @Test
    public void testUpdateFullWeathersByCodeShouldReturn400BadRequestDueToInvalidFieldBody() throws Exception {
        String locationCode = "VN_HCM";
        String requestURI = END_POINT_PATH + "/" + locationCode;

        Location testedLocation = new Location();
        testedLocation.setCode(locationCode);
        testedLocation.setRegionName("South");
        testedLocation.setCountryCode("VN");
        testedLocation.setCountryName("Socialist Republic of Vietnam");
        testedLocation.setCityName("Ho Chi Minh City");
        testedLocation.setEnabled(true);

        // realtime
        RealtimeWeatherDto realtimeWeatherDto = new RealtimeWeatherDto();
        realtimeWeatherDto.setTemperature(999);              // Bad request due to here: Temperature should be in ranage of [-30,50]
        realtimeWeatherDto.setHumidity(60);
        realtimeWeatherDto.setLastUpdated(new Date());
        realtimeWeatherDto.setPrecipitation(60);
        realtimeWeatherDto.setStatus("2nd updated from VN_HCM");
        realtimeWeatherDto.setWindSpeed(60);
        realtimeWeatherDto.setLocationAddress(testedLocation.toString());


        // list hourly
        HourlyWeatherDto hourlyWeatherDto1 = HourlyWeatherDto.builder().status("Cool").hourOfDay(10).precipitation(40).temperature(40).build();
        HourlyWeatherDto hourlyWeatherDto2 = HourlyWeatherDto.builder().status("Cool").hourOfDay(11).precipitation(50).temperature(40).build();


        // list daily
        DailyWeatherDto dailyWeatherDto1 = DailyWeatherDto.builder().month(12).dayOfMonth(1).precipitation(60).maxTemperature(40).minTemperature(30).status("Cold").build();
        DailyWeatherDto dailyWeatherDto2 = DailyWeatherDto.builder().month(12).dayOfMonth(1).precipitation(60).maxTemperature(40).minTemperature(30).status("Cold").build();


        FullyWeatherDto fullyWeatherDto = new FullyWeatherDto();
        fullyWeatherDto.setRealtimeWeather(realtimeWeatherDto);
        fullyWeatherDto.setHourlyWeatherList(List.of(hourlyWeatherDto1, hourlyWeatherDto2));
        fullyWeatherDto.setDailyWeatherList(List.of(dailyWeatherDto1, dailyWeatherDto2));
        fullyWeatherDto.setLocation(testedLocation.toString());

        String requestBody = objectMapper.writeValueAsString(fullyWeatherDto);

        Location location = FullyWeatherMapper.INSTANCE.dtoToEntity(fullyWeatherDto);
        Mockito.when(fullyWeatherService.updateFullyWeatherByCode(locationCode, location)).thenReturn(location);

        mockMvc.perform(put(requestURI).content(requestBody).contentType(REQUEST_CONTENT_TYPE))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testUpdatedFullWeathersByCodeShouldReturn404NotFound() throws Exception {
        String notFoundlocationCode = "HIHI";
        String requestURI = END_POINT_PATH + "/" + notFoundlocationCode;

        LocationNotFoundException exception = new LocationNotFoundException(notFoundlocationCode);

        FullyWeatherDto fullyWeatherDto = new FullyWeatherDto();

        RealtimeWeatherDto realtimeWeatherDto = new RealtimeWeatherDto();
        realtimeWeatherDto.setTemperature(40);
        realtimeWeatherDto.setHumidity(60);
        realtimeWeatherDto.setLastUpdated(new Date());
        realtimeWeatherDto.setPrecipitation(60);
        realtimeWeatherDto.setStatus("2nd updated from VN_HCM");
        realtimeWeatherDto.setWindSpeed(60);

        DailyWeatherDto dailyWeatherDto = DailyWeatherDto.builder()
                .month(12)
                .dayOfMonth(1)
                .precipitation(60)
                .maxTemperature(40)
                .minTemperature(30)
                .status("Cold")
                .build();

        HourlyWeatherDto hourlyWeatherDto = HourlyWeatherDto.builder()
                .status("Cool")
                .hourOfDay(10)
                .precipitation(40)
                .temperature(40)
                .build();

        Location locationFromRequest = FullyWeatherMapper.INSTANCE.dtoToEntity(fullyWeatherDto);

        fullyWeatherDto.setRealtimeWeather(realtimeWeatherDto);
        fullyWeatherDto.getHourlyWeatherList().add(hourlyWeatherDto);
        fullyWeatherDto.getDailyWeatherList().add(dailyWeatherDto);

        String requestBody = objectMapper.writeValueAsString(fullyWeatherDto);

        Mockito.when(fullyWeatherService.updateFullyWeatherByCode(notFoundlocationCode, locationFromRequest)).thenThrow(exception);

        mockMvc.perform(put(requestURI).content(requestBody).contentType(REQUEST_CONTENT_TYPE))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}
