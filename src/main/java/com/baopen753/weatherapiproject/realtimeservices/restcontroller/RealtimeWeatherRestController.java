package com.baopen753.weatherapiproject.realtimeservices.restcontroller;

import com.baopen753.weatherapiproject.CommonUtility;
import com.baopen753.weatherapiproject.GeolocationService;
import com.baopen753.weatherapiproject.dailyweatherservices.restcontroller.DailyWeatherRestController;
import com.baopen753.weatherapiproject.fullyweatherservices.restcontroller.FullyWeatherRestController;
import com.baopen753.weatherapiproject.global.ErrorDTO;
import com.baopen753.weatherapiproject.hourlyweatherservices.restcontroller.HourlyWeatherRestController;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.realtimeservices.dto.RealtimeWeatherDto;
import com.baopen753.weatherapiproject.realtimeservices.entity.RealtimeWeather;
import com.baopen753.weatherapiproject.realtimeservices.exception.RealtimeNotUpdatedException;
import com.baopen753.weatherapiproject.realtimeservices.mapper.RealtimeWeatherMapper;
import com.baopen753.weatherapiproject.realtimeservices.service.RealtimeWeatherService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/realtime")
public class RealtimeWeatherRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RealtimeWeatherService.class);

    private RealtimeWeatherService realtimeWeatherService;
    private GeolocationService geolocationService;

    public RealtimeWeatherRestController(RealtimeWeatherService realtimeWeatherService, GeolocationService geolocationService) {
        this.realtimeWeatherService = realtimeWeatherService;
        this.geolocationService = geolocationService;
    }
    /*
     *   This method is used to get realtime weather based on Ip address taken from HTTP request
     *
     *   @param:  request - inside HttpServletRequest contains Header X-Forwarded-For then invoke GeoLocation to mapping to compatible Location
     *   @return: ResponseEntity
     */

    @GetMapping
    public ResponseEntity<?> getRealtimeWeatherByIpAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);
        Location locationMappedFromIp = geolocationService.getLocation(ipAddress);
        RealtimeWeather realtimeWeather = realtimeWeatherService.getRealtimeWeatherByLocation(locationMappedFromIp);
        RealtimeWeatherDto result = RealtimeWeatherMapper.INSTANCE.entityToDto(realtimeWeather);
        return ResponseEntity.ok(addLinksByIp(result));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getRealtimeWeatherByLocation(@PathVariable String code) {
        RealtimeWeather realtimeWeather = realtimeWeatherService.getRealtimeWeatherByCode(code);
        RealtimeWeatherDto result = RealtimeWeatherMapper.INSTANCE.entityToDto(realtimeWeather);
        return ResponseEntity.ok(addLinksByCode(result,code));
    }


    @PutMapping("/{code}")
    public ResponseEntity<?> updateRealtimeWeatherByCode(@RequestBody @Valid RealtimeWeatherDto realtimeWeatherDto, @PathVariable String code) {
        RealtimeWeather realtimeWeatherBodyRequest = RealtimeWeatherMapper.INSTANCE.dtoToEntity(realtimeWeatherDto);
        realtimeWeatherBodyRequest.setLocationCode(code);

        RealtimeWeather updatedRealtime = realtimeWeatherService.updateRealtimeWeatherByCode(code, realtimeWeatherBodyRequest);
        RealtimeWeatherDto result = RealtimeWeatherMapper.INSTANCE.entityToDto(updatedRealtime);
        return ResponseEntity.ok(addLinksByCode(result,code));
    }


    private RealtimeWeatherDto addLinksByIp(RealtimeWeatherDto dto) {
        dto.add(linkTo(methodOn(RealtimeWeatherRestController.class).getRealtimeWeatherByIpAddress(null)).withSelfRel());
        dto.add(linkTo(methodOn(HourlyWeatherRestController.class).listHourlyWeatherForecastByIpAddress(null)).withRel("hourly_weather"));
        dto.add(linkTo(methodOn(DailyWeatherRestController.class).getDailyWeathersByIpAddress(null)).withRel("daily_weather"));
        dto.add(linkTo(methodOn(FullyWeatherRestController.class).getFullyWeatherByIpAddress(null)).withRel("fully_weather"));
        return dto;
    }


    private RealtimeWeatherDto addLinksByCode(RealtimeWeatherDto dto, String code) {
        dto.add(linkTo(methodOn(RealtimeWeatherRestController.class).getRealtimeWeatherByLocation(code)).withSelfRel());
        dto.add(linkTo(methodOn(HourlyWeatherRestController.class).listHourlyWeatherForecastByCode(code,null)).withRel("hourly_weather"));
        dto.add(linkTo(methodOn(DailyWeatherRestController.class).getDailyWeathersByCode(code)).withRel("daily_weather"));
        dto.add(linkTo(methodOn(FullyWeatherRestController.class).getFullyWeatherByCode(code,null)).withRel("fully_weather"));
        return dto;
    }



    @ExceptionHandler(RealtimeNotUpdatedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handleWeatherNotUpdatedException(HttpServletRequest request, Exception exception) {

        LOGGER.error("This is log message: " + exception.getMessage());

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setTimeStamp(new Date());
        errorDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDTO.setPath(request.getServletPath());
        errorDTO.addError(exception.getMessage());

        return errorDTO;
    }

}
