package com.baopen753.weatherapiproject.fullyweatherservices.restcontroller;

import com.baopen753.weatherapiproject.CommonUtility;
import com.baopen753.weatherapiproject.GeolocationService;
import com.baopen753.weatherapiproject.fullyweatherservices.dto.FullyWeatherDto;
import com.baopen753.weatherapiproject.fullyweatherservices.mapper.FullyWeatherMapper;
import com.baopen753.weatherapiproject.fullyweatherservices.service.FullyWeatherService;
import com.baopen753.weatherapiproject.hourlyweatherservices.exception.BadRequestException;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fully")
@Validated
public class FullyWeatherRestController {

    @Autowired
    private GeolocationService geolocationService;

    @Autowired
    private FullyWeatherService fullyWeatherService;



    @GetMapping
    public ResponseEntity<?> getFullyWeatherByIpAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);
        Integer currentHour = CommonUtility.getCurrentHour(request);

        Location locationFromIp = geolocationService.getLocation(ipAddress);
        FullyWeatherDto result = fullyWeatherService.getFullyWeatherByLocation(locationFromIp, currentHour);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getFullyWeatherByCode(@PathVariable String code, HttpServletRequest request) {
        Integer currentHour = CommonUtility.getCurrentHour(request);

        FullyWeatherDto result = fullyWeatherService.getFullyWeatherByCode(code, currentHour);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateFullyWeatherByCode(@PathVariable String code, @RequestBody @Valid FullyWeatherDto fullyWeatherDto) {

        if(fullyWeatherDto == null)
            throw new BadRequestException("Fully weather cannot be null");

        if (fullyWeatherDto.getDailyWeatherList().isEmpty())
            throw new BadRequestException("Daily weather should not be empty");

        if (fullyWeatherDto.getHourlyWeatherList().isEmpty())
            throw new BadRequestException("Hourly weather should not be empty");

        Location locationInRequest = FullyWeatherMapper.INSTANCE.dtoToEntity(fullyWeatherDto);

        Location locationUpdated = fullyWeatherService.updateFullyWeatherByCode(code, locationInRequest);

        FullyWeatherDto updatedDto = FullyWeatherMapper.INSTANCE.entityToDto(locationUpdated);
        return ResponseEntity.ok(updatedDto);
    }

}
