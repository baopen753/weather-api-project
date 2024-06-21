package com.baopen753.weatherapiproject.hourlyweatherservices.restcontroller;


import com.baopen753.weatherapiproject.CommonUtility;
import com.baopen753.weatherapiproject.GeolocationService;
import com.baopen753.weatherapiproject.hourlyweatherservices.dto.HourlyWeatherDto;
import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeather;
import com.baopen753.weatherapiproject.hourlyweatherservices.service.HourlyWeatherService;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.service.LocationService;
import com.baopen753.weatherapiproject.realtimeservices.dto.RealtimeWeatherDto;
import com.baopen753.weatherapiproject.realtimeservices.entity.RealtimeWeather;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hourly")
public class HourlyWeatherRestController {


    private HourlyWeatherService hourlyWeatherService;
    private GeolocationService geolocationService;
    private ModelMapper modelMapper;

    @Autowired
    public HourlyWeatherRestController(HourlyWeatherService hourlyWeatherService, GeolocationService geolocationService, ModelMapper modelMapper) {
        this.hourlyWeatherService = hourlyWeatherService;
        this.geolocationService = geolocationService;
        this.modelMapper = modelMapper;
    }

    private HourlyWeatherDto entityToDto(HourlyWeather hourlyWeather) {
        return modelMapper.map(hourlyWeather, HourlyWeatherDto.class);
    }

    @GetMapping
    public ResponseEntity<?> getHourlyWeatherByIpAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);
        Integer currentHour = Integer.parseInt(request.getHeader("X-CURRENT-HOUR"));
        Location locationInMappedromIp = geolocationService.getLocation(ipAddress);
        List<HourlyWeather> hourlyWeatherList = hourlyWeatherService.getHourlyWeatherByLocation(locationInMappedromIp, currentHour);

        List<HourlyWeatherDto> result = hourlyWeatherList.stream().map(hourlyWeather -> entityToDto(hourlyWeather)).toList();
        return ResponseEntity.ok(result);
    }
}
