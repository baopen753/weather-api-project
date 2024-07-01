package com.baopen753.weatherapiproject.hourlyweatherservices.restcontroller;


import com.baopen753.weatherapiproject.CommonUtility;
import com.baopen753.weatherapiproject.GeolocationService;
import com.baopen753.weatherapiproject.hourlyweatherservices.dto.HourlyWeatherDto;
import com.baopen753.weatherapiproject.hourlyweatherservices.dto.HourlyWeatherListDto;
import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeather;
import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeatherId;
import com.baopen753.weatherapiproject.hourlyweatherservices.exception.BadRequestException;
import com.baopen753.weatherapiproject.hourlyweatherservices.mapper.HourlyWeatherMapper;
import com.baopen753.weatherapiproject.hourlyweatherservices.service.HourlyWeatherService;
import com.baopen753.weatherapiproject.locationservices.entity.Location;

import com.baopen753.weatherapiproject.locationservices.service.LocationService;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/hourly")
@Validated    // is used when validating a list in request body

public class HourlyWeatherRestController {

    private HourlyWeatherService hourlyWeatherService;
    private GeolocationService geolocationService;
    private LocationService locationService;
    private ModelMapper modelMapper;

    @Autowired
    public HourlyWeatherRestController(HourlyWeatherService hourlyWeatherService, GeolocationService geolocationService, LocationService locationService, ModelMapper modelMapper) {
        this.hourlyWeatherService = hourlyWeatherService;
        this.geolocationService = geolocationService;
        this.locationService = locationService;
        this.modelMapper = modelMapper;
    }

    private HourlyWeatherDto entityToDto(HourlyWeather hourlyWeather) {
        HourlyWeatherDto dto = modelMapper.map(hourlyWeather, HourlyWeatherDto.class);
        dto.setHourOfDay(hourlyWeather.getHourlyWeatherId().getHourOfDay());
        return dto;
    }

    private HourlyWeather dtoToEntity(HourlyWeatherDto dto) {                    // this class can only map (temperature, precipitation, status, hour_of_day) from dto -> entity
        HourlyWeather entity = modelMapper.map(dto, HourlyWeather.class);        // but, entity.location not yet

        HourlyWeatherId hourlyWeatherId = HourlyWeatherId.builder().hourOfDay(dto.getHourOfDay()).build();

        entity.setHourlyWeatherId(hourlyWeatherId);
        return entity;
    }

    private HourlyWeatherListDto entityListToDto(List<HourlyWeather> hourlyWeatherList) {

        // get location code to take a location info as a representative
        Location location = hourlyWeatherList.get(0).getHourlyWeatherId().getLocation();

        HourlyWeatherListDto result = new HourlyWeatherListDto();
        result.setLocation(location.toString());

        hourlyWeatherList.forEach(hourlyWeather -> {
            HourlyWeatherDto dto = entityToDto(hourlyWeather);
            result.addHourlyWeatherDto(dto);
        });
        return result;
    }

    private List<HourlyWeather> dtoListToEntityList(List<HourlyWeatherDto> hourlyWeatherDtoList) {

        List<HourlyWeather> hourlyWeatherList = new ArrayList<>();
        hourlyWeatherDtoList.forEach(dto -> {
            HourlyWeather entity = HourlyWeatherMapper.INSTANCE.dtoToEntity(dto);
            hourlyWeatherList.add(entity);
        });
        return hourlyWeatherList;
    }



    @GetMapping
    public ResponseEntity<?> listHourlyWeatherForecastByIpAddress(HttpServletRequest request) {

        // get ip address & current hour from request
        String ipAddress = CommonUtility.getIPAddress(request);
        Integer currentHour = CommonUtility.getCurrentHour(request);

        Location locationInMappedFromIp = geolocationService.getLocation(ipAddress);
        List<HourlyWeather> hourlyWeatherList = hourlyWeatherService.getHourlyWeatherByLocation(locationInMappedFromIp, currentHour);

        if (hourlyWeatherList.isEmpty()) return ResponseEntity.noContent().build();

        HourlyWeatherListDto result = entityListToDto(hourlyWeatherList);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> listHourlyWeatherForecastByCode(@PathVariable String code, HttpServletRequest request) {

        Integer currentHour = CommonUtility.getCurrentHour(request);

        List<HourlyWeather> hourlyWeatherList = hourlyWeatherService.getHourlyWeatherByCode(code, currentHour);
        if (hourlyWeatherList.isEmpty()) return ResponseEntity.noContent().build();

        HourlyWeatherListDto result = entityListToDto(hourlyWeatherList);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateHourlyWeatherForecastByLocation(@RequestBody @Valid List<HourlyWeatherDto> hourlyWeatherDtoList, @PathVariable String code) {

        if (hourlyWeatherDtoList.isEmpty())
            throw new BadRequestException("Hourly weather list is empty");

        // convert DTO list ---> Entity list
        List<HourlyWeather> convertedHourlyWeatherListFromRequest = dtoListToEntityList(hourlyWeatherDtoList);

        List<HourlyWeather> updatedHourlyWeatherList = hourlyWeatherService.updateHourlyWeatherByLocationCode(code, convertedHourlyWeatherListFromRequest);
        // return hourlyWeather list after update

        // convert Entity list ---> listDto object
        HourlyWeatherListDto result = entityListToDto(updatedHourlyWeatherList);
        return ResponseEntity.ok(result);
    }


}















