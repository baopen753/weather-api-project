package com.baopen753.weatherapiproject.hourlyweatherservices.restcontroller;

import com.baopen753.weatherapiproject.CommonUtility;
import com.baopen753.weatherapiproject.GeolocationService;
import com.baopen753.weatherapiproject.dailyweatherservices.restcontroller.DailyWeatherRestController;
import com.baopen753.weatherapiproject.fullyweatherservices.restcontroller.FullyWeatherRestController;
import com.baopen753.weatherapiproject.hourlyweatherservices.dto.HourlyWeatherDto;
import com.baopen753.weatherapiproject.hourlyweatherservices.dto.HourlyWeatherListDto;
import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeather;
import com.baopen753.weatherapiproject.hourlyweatherservices.exception.BadRequestException;
import com.baopen753.weatherapiproject.hourlyweatherservices.mapper.HourlyWeatherMapper;
import com.baopen753.weatherapiproject.hourlyweatherservices.service.HourlyWeatherService;
import com.baopen753.weatherapiproject.locationservices.entity.Location;

import com.baopen753.weatherapiproject.realtimeservices.restcontroller.RealtimeWeatherRestController;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/hourly")
@Validated    // is used when validating a list in request body

public class HourlyWeatherRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HourlyWeatherRestController.class);

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
        List<HourlyWeather> hourlyWeatherList = hourlyWeatherService.getHourlyWeathersByLocation(locationInMappedFromIp, currentHour);

        if (hourlyWeatherList.isEmpty()) return ResponseEntity.noContent().build();

        HourlyWeatherListDto result = entityListToDto(hourlyWeatherList);
        return ResponseEntity.ok(addLinksByIp(result));
    }


    @GetMapping("/{code}")
    public ResponseEntity<?> listHourlyWeatherForecastByCode(@PathVariable String code, HttpServletRequest request) {

        Integer currentHour = CommonUtility.getCurrentHour(request);

        List<HourlyWeather> hourlyWeatherList = hourlyWeatherService.getHourlyWeathersByCode(code, currentHour);
        if (hourlyWeatherList.isEmpty()) return ResponseEntity.noContent().build();

        HourlyWeatherListDto result = entityListToDto(hourlyWeatherList);

        return ResponseEntity.ok(addLinksByCode(result, code));
    }


    @PutMapping("/{code}")
    public ResponseEntity<?> updateHourlyWeatherForecastByLocation(@RequestBody @Valid List<HourlyWeatherDto> hourlyWeatherDtoList, @PathVariable String code) {

        if (hourlyWeatherDtoList.isEmpty())
            throw new BadRequestException("Hourly weather list is empty");

        // convert DTO list ---> Entity list
        List<HourlyWeather> convertedHourlyWeatherListFromRequest = dtoListToEntityList(hourlyWeatherDtoList);

        List<HourlyWeather> updatedHourlyWeatherList = hourlyWeatherService.updateHourlyWeathersByLocationCode(code, convertedHourlyWeatherListFromRequest);
        // return hourlyWeather list after update

        // convert Entity list ---> listDto object
        HourlyWeatherListDto result = entityListToDto(updatedHourlyWeatherList);
        return ResponseEntity.ok(addLinksByCode(result,code));
    }


    private HourlyWeatherListDto addLinksByIp(HourlyWeatherListDto dto) {
        dto.add(linkTo(methodOn(HourlyWeatherRestController.class).listHourlyWeatherForecastByIpAddress(null)).withSelfRel());
        dto.add(linkTo(methodOn(RealtimeWeatherRestController.class).getRealtimeWeatherByIpAddress(null)).withRel("realtime"));
        dto.add(linkTo(methodOn(DailyWeatherRestController.class).getDailyWeathersByIpAddress(null)).withRel("daily_forecast"));
        dto.add(linkTo(methodOn(FullyWeatherRestController.class).getFullyWeatherByIpAddress(null)).withRel("fully_forecast"));
        return dto;
    }


    private HourlyWeatherListDto addLinksByCode(HourlyWeatherListDto dto, String code) {
        dto.add(linkTo(methodOn(HourlyWeatherRestController.class).listHourlyWeatherForecastByCode(code, null)).withSelfRel());
        dto.add(linkTo(methodOn(RealtimeWeatherRestController.class).getRealtimeWeatherByLocation(code)).withRel("realtime"));
        dto.add(linkTo(methodOn(DailyWeatherRestController.class).getDailyWeathersByCode(code)).withRel("daily_forecast"));
        dto.add(linkTo(methodOn(FullyWeatherRestController.class).getFullyWeatherByCode(code, null)).withRel("fully_forecast"));
        return dto;
    }


}















