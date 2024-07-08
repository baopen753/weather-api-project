package com.baopen753.weatherapiproject.hourly;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.baopen753.weatherapiproject.GeolocationException;
import com.baopen753.weatherapiproject.GeolocationService;
import com.baopen753.weatherapiproject.hourlyweatherservices.dto.HourlyWeatherDto;
import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeather;
import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeatherId;
import com.baopen753.weatherapiproject.hourlyweatherservices.restcontroller.HourlyWeatherRestController;
import com.baopen753.weatherapiproject.hourlyweatherservices.service.HourlyWeatherService;
import com.baopen753.weatherapiproject.locationservices.entity.Location;

import com.baopen753.weatherapiproject.locationservices.exception.LocationNotFoundException;
import com.baopen753.weatherapiproject.locationservices.service.LocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@WebMvcTest(HourlyWeatherRestController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = true)
public class HourlyWeatherRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HourlyWeatherService hourlyWeatherService;

    @MockBean
    private GeolocationService geolocationService;

    @MockBean
    private LocationService locationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ModelMapper modelMapper;

    private static final String END_POINT_PATH = "/api/v1/hourly";
    private static final String X_CURRENT_HOUR = "X-Current-Hour";
    private static final String REQUEST_CONTENT_TYPE = "application/json";

    @Test
    public void testGetHourlyWeatherForecastByIpShouldReturn200Ok() throws Exception {

        int currentOfHour = 17;

        Location locationMappedFromIp = new Location();
        locationMappedFromIp.setRegionName("Middle");
        locationMappedFromIp.setCountryCode("VN");
        locationMappedFromIp.setCityName("Ho Chi Minh City");
        locationMappedFromIp.setCountryName("The Socialist Republic of Vietnam");

        // perform a test environment
        HourlyWeatherId hourlyWeatherId1 = HourlyWeatherId.builder().hourOfDay(17).location(locationMappedFromIp).build();

        HourlyWeather hourlyWeather1 = HourlyWeather.builder().hourlyWeatherId(hourlyWeatherId1).precipitation(50).temperature(50).status("Rainy").build();

        HourlyWeatherId hourlyWeatherId2 = HourlyWeatherId.builder().hourOfDay(18).location(locationMappedFromIp).build();

        HourlyWeather hourlyWeather2 = HourlyWeather.builder().hourlyWeatherId(hourlyWeatherId2).precipitation(50).temperature(50).status("Rainy").build();


        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(locationMappedFromIp);

        Mockito.when(hourlyWeatherService.getHourlyWeathersByLocation(locationMappedFromIp, currentOfHour)).thenReturn(List.of(hourlyWeather1, hourlyWeather2));

        mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, String.valueOf(currentOfHour))).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testGetHourlyWeatherForecastByIpShouldReturn204NoContent() throws Exception {
        int currentOfHour = 20;    // there has no any hourlyweather forecast in hcm > 20

        Location locationMappedFromIp = new Location();
        locationMappedFromIp.setRegionName("Middle");
        locationMappedFromIp.setCountryCode("VN");
        locationMappedFromIp.setCityName("Ho Chi Minh City");
        locationMappedFromIp.setCountryName("The Socialist Republic of Vietnam");

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(locationMappedFromIp);

        Mockito.when(hourlyWeatherService.getHourlyWeathersByLocation(locationMappedFromIp, currentOfHour)).thenReturn(new ArrayList<>());   // return an empty array which indidate that List<HourlyWeather> is null

        mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, String.valueOf(currentOfHour))).andExpect(status().isNoContent()).andDo(print());
    }

    @Test
    public void testGetHourlyWeatherForecastByIpShouldReturn400BadRequestInGeolocation() throws Exception {
        String locationNotMappedIpAddress = "1.1.1.1";
        GeolocationException exception = new GeolocationException(locationNotMappedIpAddress);

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenThrow(exception);

        mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, "17")).andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    public void testGetHourlyWeatherForecastByIpShouldReturn400BadRequestMissingXCurrentHourHeader() throws Exception {

        mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    public void testGetHourlyWeatherForecastByIpHasCurrentHourShouldReturn204NoContent() throws Exception {
        mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, "17")).andExpect(status().isNoContent()).andDo(print());
    }

    @Test
    public void testGetHourlyWeatherForecastByIpNotFoundShouldReturn404NotFound() throws Exception {
        // ip can be mapped by Geo but the mapped location does not exist in weather_db
        int currentHour = 17;

        Location location = new Location();
        location.setCountryCode("US");
        location.setCityName("Los Angeles");

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);

        LocationNotFoundException exception = new LocationNotFoundException(location.getCountryCode(), location.getCityName());
        Mockito.when(hourlyWeatherService.getHourlyWeathersByLocation(location, currentHour)).thenThrow(exception);

        mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, String.valueOf(currentHour))).andExpect(status().isNotFound()).andDo(print());
    }


    @Test
    public void testGetHourlyWeatherForecastByCodeShouldReturn200OK() throws Exception {
        String locationCode = "VN_HN";
        Integer currentHour = 16;

        Location locationGetFromCode = new Location();
        locationGetFromCode.setCode(locationCode);
        locationGetFromCode.setCountryCode("VN");
        locationGetFromCode.setRegionName("North");
        locationGetFromCode.setCityName("Hanoi");
        locationGetFromCode.setCountryName("Socialist Republic of Vietnam");

        HourlyWeatherId hourlyWeatherId1 = HourlyWeatherId.builder().location(locationGetFromCode).hourOfDay(17).build();
        HourlyWeather hourlyWeather1 = HourlyWeather.builder().temperature(70).precipitation(70).status("Cosy").hourlyWeatherId(hourlyWeatherId1).build();


        HourlyWeatherId hourlyWeatherId2 = HourlyWeatherId.builder().location(locationGetFromCode).hourOfDay(18).build();
        HourlyWeather hourlyWeather2 = HourlyWeather.builder().temperature(90).precipitation(90).status("Hot").hourlyWeatherId(hourlyWeatherId2).build();


        Mockito.when(hourlyWeatherService.getHourlyWeathersByCode(locationGetFromCode.getCode(), currentHour)).thenReturn(List.of(hourlyWeather1, hourlyWeather2));
        mockMvc.perform(get(END_POINT_PATH + "/" + locationCode).header(X_CURRENT_HOUR, 16)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testGetHourlyWeatherForecastByCodeShouldReturn204NoContent() throws Exception {
        String locationCode = "VN_HN";
        Integer currentHour = 20;

        Mockito.when(hourlyWeatherService.getHourlyWeathersByCode(locationCode, currentHour)).thenReturn(Collections.emptyList());
        mockMvc.perform(get(END_POINT_PATH + "/" + locationCode).header(X_CURRENT_HOUR, String.valueOf(currentHour))).andExpect(status().isNoContent()).andDo(print());
    }

    @Test
    public void testGetHourlyWeatherForecastByCodeShouldReturn400BadRequestDueToHeaderCurrentHour() throws Exception {
        String locationCode = "VN_HN";
        Integer currentHour = 17;

        Mockito.when(hourlyWeatherService.getHourlyWeathersByCode(locationCode, currentHour)).thenReturn(new ArrayList<>());
        mockMvc.perform(get(END_POINT_PATH + "/" + locationCode)).andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    public void testGetHourlyWeatherForecastByCodeShouldReturn404NotFound() throws Exception {
        String locationCode = "VN_HIHI";  // non-existing location code
        Integer currentHour = 17;

        String countryCode = "VN";
        String cityName = "Ho Chi Minh City";

        LocationNotFoundException exception = new LocationNotFoundException(countryCode, cityName);

        Mockito.when(hourlyWeatherService.getHourlyWeathersByCode(locationCode, currentHour)).thenThrow(exception);

        mockMvc.perform(get(END_POINT_PATH + "/" + locationCode).header(X_CURRENT_HOUR, currentHour)).andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    public void testUpdateHourlyWeatherForecastByCodeShouldReturn200Ok() throws Exception {

        String locationCode = "VN_HN";
        String requestURI = END_POINT_PATH + "/" + locationCode;

        Location locationGetFromCode = new Location();
        locationGetFromCode.setCode(locationCode);
        locationGetFromCode.setCountryCode("VN");
        locationGetFromCode.setRegionName("North");
        locationGetFromCode.setCityName("Hanoi");
        locationGetFromCode.setCountryName("Socialist Republic of Vietnam");

        HourlyWeatherDto dto1 = HourlyWeatherDto.builder().temperature(50).precipitation(50).hourOfDay(16).status("Cold").build();
        HourlyWeatherDto dto2 = HourlyWeatherDto.builder().temperature(50).precipitation(60).hourOfDay(17).status("Freeze").build();
        HourlyWeatherDto dto3 = HourlyWeatherDto.builder().temperature(50).precipitation(70).hourOfDay(18).status("Freezeze").build();

        HourlyWeather entity1 = new HourlyWeather();
        entity1.getHourlyWeatherId().setLocation(locationGetFromCode);
        entity1.getHourlyWeatherId().setHourOfDay(16);
        entity1.setTemperature(50);
        entity1.setPrecipitation(50);
        entity1.setStatus("Cold");

        HourlyWeather entity2 = new HourlyWeather();
        entity2.getHourlyWeatherId().setLocation(locationGetFromCode);
        entity2.getHourlyWeatherId().setHourOfDay(17);
        entity2.setTemperature(60);
        entity2.setPrecipitation(60);
        entity2.setStatus("Freeze");

        HourlyWeather entity3 = new HourlyWeather();
        entity3.getHourlyWeatherId().setLocation(locationGetFromCode);
        entity3.getHourlyWeatherId().setHourOfDay(18);
        entity3.setTemperature(70);
        entity3.setPrecipitation(70);
        entity3.setStatus("Freezeze");


        List<HourlyWeatherDto> hourlyWeatherDtoList = List.of(dto1, dto2, dto3);
        List<HourlyWeather> hourlyWeatherList = List.of(entity1, entity2, entity3);

        // List<HourlyWeather> hourlyWeatherList = dtoListToEntityList(hourlyWeatherDtoList);   // using this  method will be NullPointerException


        String requestBody = objectMapper.writeValueAsString(hourlyWeatherDtoList);


        Mockito.when(hourlyWeatherService.updateHourlyWeathersByLocationCode(Mockito.eq(locationCode), Mockito.anyList())).thenReturn(hourlyWeatherList);
        mockMvc.perform(put(requestURI).contentType(REQUEST_CONTENT_TYPE).content(requestBody))
                .andExpect(status().isOk())
                .andDo(print());

    }

//    private List<HourlyWeather> dtoListToEntityList(List<HourlyWeatherDto> hourlyWeatherDtoList) {
//
//        List<HourlyWeather> hourlyWeatherList = new ArrayList<>();
//        hourlyWeatherDtoList.forEach(dto -> {
//            HourlyWeather entity = HourlyWeatherMapper.INSTANCE.dtoToEntity(dto);
//            hourlyWeatherList.add(entity);
//        });
//        return hourlyWeatherList;
//    }

    @Test
    public void testUpdateHourlyWeatherForecastByCodeShouldReturn404NotFoundDueToLocation() throws Exception {
        String locationCode = "VN_HIHI";
        String requestURI = END_POINT_PATH + "/" + locationCode;
        LocationNotFoundException exception = new LocationNotFoundException(locationCode);

        HourlyWeatherDto dto1 = HourlyWeatherDto.builder().temperature(50).precipitation(50).hourOfDay(16).status("Cold").build();
        HourlyWeatherDto dto2 = HourlyWeatherDto.builder().temperature(50).precipitation(60).hourOfDay(17).status("Freeze").build();

        List<HourlyWeatherDto> inputs = List.of(dto1, dto2);
        String bodyRequest = objectMapper.writeValueAsString(inputs);

        Mockito.when(hourlyWeatherService.updateHourlyWeathersByLocationCode(Mockito.eq(locationCode), Mockito.anyList())).thenThrow(exception);

        mockMvc.perform(put(requestURI).contentType(REQUEST_CONTENT_TYPE).content(bodyRequest))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testUpdateHourlyWeatherForecastByCodeShouldReturn400BadRequestDuetoEmptyListBody() throws Exception
    {
        String locationCode = "VN_HN";
        String requestURI = END_POINT_PATH + "/" + locationCode;

        HourlyWeatherDto dto1 = HourlyWeatherDto.builder().temperature(700).precipitation(50).hourOfDay(24).status("Cold").build();

        List<HourlyWeatherDto> inputs = List.of(dto1);
        String requestBody = objectMapper.writeValueAsString(inputs);

        mockMvc.perform(put(requestURI).contentType(REQUEST_CONTENT_TYPE).content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());


    }


}



