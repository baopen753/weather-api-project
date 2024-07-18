package com.baopen753.weatherapiproject.dailyweatherservices.restcontroller;

import com.baopen753.weatherapiproject.CommonUtility;
import com.baopen753.weatherapiproject.GeolocationService;
import com.baopen753.weatherapiproject.dailyweatherservices.dto.DailyWeatherDto;
import com.baopen753.weatherapiproject.dailyweatherservices.dto.DailyWeatherListDto;
import com.baopen753.weatherapiproject.dailyweatherservices.entity.DailyWeather;
import com.baopen753.weatherapiproject.dailyweatherservices.mapper.DailyWeatherMapper;
import com.baopen753.weatherapiproject.dailyweatherservices.service.DailyWeatherService;
import com.baopen753.weatherapiproject.fullyweatherservices.restcontroller.FullyWeatherRestController;
import com.baopen753.weatherapiproject.global.GlobalExceptionHandler;
import com.baopen753.weatherapiproject.hourlyweatherservices.exception.BadRequestException;
import com.baopen753.weatherapiproject.hourlyweatherservices.restcontroller.HourlyWeatherRestController;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.service.LocationService;

import com.baopen753.weatherapiproject.realtimeservices.restcontroller.RealtimeWeatherRestController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        return ResponseEntity.ok(addLinksByIp(result));
    }


    @GetMapping("/{code}")
    public ResponseEntity<?> getDailyWeathersByCode(@PathVariable String code) {

        List<DailyWeather> dailyWeatherList = dailyWeatherService.getDailyWeathersByCode(code);
        if (dailyWeatherList.isEmpty()) return ResponseEntity.noContent().build();

        DailyWeatherListDto result = entityListToListDto(dailyWeatherList);
        return ResponseEntity.ok(addLinksByCode(result, code));
    }


    @PutMapping("/{code}")
    public ResponseEntity<?> updateDailyWeathersByCode(@PathVariable String code, @RequestBody @Valid List<DailyWeatherDto> dailyWeatherDtoList) {

        // check isEmpty requestBody
        if (dailyWeatherDtoList.isEmpty()) throw new BadRequestException("Required body request");

        // convert List<Dto> --> List<Entity>
        List<DailyWeather> dailyWeatherListConvertedFromRequest = new ArrayList<>();
        dailyWeatherDtoList.forEach(dto -> {
            DailyWeather entity = DailyWeatherMapper.INSTANCE.dtoToEntity(dto);
            dailyWeatherListConvertedFromRequest.add(entity);
        });

        List<DailyWeather> dailyWeatherListUpdated = dailyWeatherService.updateDailyWeathersByCode(code, dailyWeatherListConvertedFromRequest);
        if (dailyWeatherListUpdated.isEmpty()) return ResponseEntity.noContent().build();

        DailyWeatherListDto result = entityListToListDto(dailyWeatherListUpdated);
        return ResponseEntity.ok(addLinksByCode(result, code));
    }


    private EntityModel<DailyWeatherListDto> addLinksByIp(DailyWeatherListDto dto) {
        EntityModel<DailyWeatherListDto> entityModel = EntityModel.of(dto);      // of() will wrap dto as data type of EntityModel
        entityModel.add(linkTo(methodOn(DailyWeatherRestController.class).getDailyWeathersByIpAddress(null)).withSelfRel());
        entityModel.add(linkTo(methodOn(RealtimeWeatherRestController.class).getRealtimeWeatherByIpAddress(null)).withRel("realtime"));
        entityModel.add(linkTo(methodOn(HourlyWeatherRestController.class).listHourlyWeatherForecastByIpAddress(null)).withRel("hourly_forecast"));
        entityModel.add(linkTo(methodOn(FullyWeatherRestController.class).getFullyWeatherByIpAddress(null)).withRel("fully_forecast"));
        return entityModel;
    }

    private EntityModel<DailyWeatherListDto> addLinksByCode(DailyWeatherListDto dto, String code) {
        EntityModel<DailyWeatherListDto> entityModel = EntityModel.of(dto);      // of() will wrap dto as data type of EntityModel
        entityModel.add(linkTo(methodOn(DailyWeatherRestController.class).getDailyWeathersByCode(code)).withSelfRel());
        entityModel.add(linkTo(methodOn(RealtimeWeatherRestController.class).getRealtimeWeatherByLocation(code)).withRel("realtime"));
        entityModel.add(linkTo(methodOn(HourlyWeatherRestController.class).listHourlyWeatherForecastByCode(code, null)).withRel("hourly_forecast"));
        entityModel.add(linkTo(methodOn(FullyWeatherRestController.class).getFullyWeatherByCode(code, null)).withRel("fully_forecast"));
        return entityModel;
    }

}














