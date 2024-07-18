package com.baopen753.weatherapiproject.dailyweatherservices.restcontroller;

import com.baopen753.weatherapiproject.CommonUtility;
import com.baopen753.weatherapiproject.GeolocationService;
import com.baopen753.weatherapiproject.dailyweatherservices.dto.DailyWeatherDto;
import com.baopen753.weatherapiproject.dailyweatherservices.dto.DailyWeatherListDto;
import com.baopen753.weatherapiproject.dailyweatherservices.entity.DailyWeather;
import com.baopen753.weatherapiproject.dailyweatherservices.mapper.DailyWeatherMapper;
import com.baopen753.weatherapiproject.dailyweatherservices.service.DailyWeatherService;
import com.baopen753.weatherapiproject.global.GlobalExceptionHandler;
import com.baopen753.weatherapiproject.hourlyweatherservices.exception.BadRequestException;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.service.LocationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/daily")
@Validated
public class DailyWeatherRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private DailyWeatherService dailyWeatherService;

    @Autowired
    private GeolocationService geolocationService;

    @Autowired
    private LocationService locationService;

    private DailyWeatherListDto entityListToListDto(List<DailyWeather> dailyWeatherList) {
        DailyWeatherListDto listDto = new DailyWeatherListDto();

        for (DailyWeather entity : dailyWeatherList) {
            DailyWeatherDto dto = DailyWeatherMapper.INSTANCE.entityToDto(entity);
            listDto.addDailyWeatherDto(dto);
        }

        Location location = dailyWeatherList.get(0).getDailyWeatherId().getLocation();

        listDto.setLocation(location.toString());
        return listDto;
    }



    @GetMapping
    public ResponseEntity<?> getDailyWeathersByIpAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);
        Location locationFromIp = geolocationService.getLocation(ipAddress);

        List<DailyWeather> dailyWeatherList = dailyWeatherService.getDailyWeathersByLocation(locationFromIp);
        if (dailyWeatherList.isEmpty()) return ResponseEntity.noContent().build();

        DailyWeatherListDto result = entityListToListDto(dailyWeatherList);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getDailyWeathersByCode(@PathVariable String code) {

        List<DailyWeather> dailyWeatherList = dailyWeatherService.getDailyWeathersByCode(code);
        if (dailyWeatherList.isEmpty()) return ResponseEntity.noContent().build();

        DailyWeatherListDto result = entityListToListDto(dailyWeatherList);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateDailyWeathersByCode(@PathVariable String code, @RequestBody @Valid  List<DailyWeatherDto> dailyWeatherDtoList) {

        // check isEmpty requestBody
        if (dailyWeatherDtoList.isEmpty())
            throw new BadRequestException("Required body request");

        // convert List<Dto> --> List<Entity>
        List<DailyWeather> dailyWeatherListConvertedFromRequest = new ArrayList<>();
        dailyWeatherDtoList.forEach(dto -> {
            DailyWeather entity = DailyWeatherMapper.INSTANCE.dtoToEntity(dto);
            dailyWeatherListConvertedFromRequest.add(entity);
        });

        List<DailyWeather> dailyWeatherListUpdated = dailyWeatherService.updateDailyWeathersByCode(code, dailyWeatherListConvertedFromRequest);
        if (dailyWeatherListUpdated.isEmpty()) return ResponseEntity.noContent().build();

        DailyWeatherListDto result = entityListToListDto(dailyWeatherListUpdated);
        return ResponseEntity.ok(result);
    }
}














