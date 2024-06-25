package com.baopen753.weatherapiproject.hourlyweatherservices.restcontroller;

import com.baopen753.weatherapiproject.CommonUtility;
import com.baopen753.weatherapiproject.GeolocationService;
import com.baopen753.weatherapiproject.hourlyweatherservices.dto.HourlyWeatherDto;
import com.baopen753.weatherapiproject.hourlyweatherservices.dto.HourlyWeatherListDto;
import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeather;
import com.baopen753.weatherapiproject.hourlyweatherservices.service.HourlyWeatherService;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        HourlyWeatherDto dto = modelMapper.map(hourlyWeather, HourlyWeatherDto.class);
        dto.setHourOfDay(hourlyWeather.getHourlyWeatherId().getHourOfDay());
        return dto;
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

    @GetMapping
    public ResponseEntity<?> listHourlyWeatherForecastByIpAddress(HttpServletRequest request) {

        // get ip address & current hour from request
        String ipAddress = CommonUtility.getIPAddress(request);

        Integer currentHour = CommonUtility.getCurrentHour(request);

        Location locationInMappedFromIp = geolocationService.getLocation(ipAddress);
        List<HourlyWeather> hourlyWeatherList = hourlyWeatherService.getHourlyWeatherByLocation(locationInMappedFromIp, currentHour);

        if (hourlyWeatherList.isEmpty())
            return ResponseEntity.noContent().build();

        HourlyWeatherListDto result = entityListToDto(hourlyWeatherList);
        return ResponseEntity.ok(result);
    }
}
