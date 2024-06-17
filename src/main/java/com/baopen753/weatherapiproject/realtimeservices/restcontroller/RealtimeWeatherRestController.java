package com.baopen753.weatherapiproject.realtimeservices.restcontroller;

import com.baopen753.weatherapiproject.CommonUtility;
import com.baopen753.weatherapiproject.GeolocationService;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.realtimeservices.dto.RealtimeWeatherDto;
import com.baopen753.weatherapiproject.realtimeservices.entity.RealtimeWeather;
import com.baopen753.weatherapiproject.realtimeservices.service.RealtimeWeatherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/realtime")
public class RealtimeWeatherRestController {


    private RealtimeWeatherService realtimeWeatherService;
    private GeolocationService geolocationService;
    private ModelMapper modelMapper;

    public RealtimeWeatherRestController(RealtimeWeatherService realtimeWeatherService, GeolocationService geolocationService, ModelMapper modelMapper) {
        this.realtimeWeatherService = realtimeWeatherService;
        this.geolocationService = geolocationService;
        this.modelMapper = modelMapper;
    }

    /*
     *   This method is used to get realtime weather based on Ip address taken from HTTP request
     *
     *   @param:  request - inside HttpServletRequest contains Header X-Forwarded-For then invoke GeoLocation to mapping to compatible Location
     *   @return: ResponseEntity
     */


    private RealtimeWeatherDto entityToDto(RealtimeWeather realtimeWeather) {
        return modelMapper.map(realtimeWeather, RealtimeWeatherDto.class);
    }

    private RealtimeWeather dtoToEntity(RealtimeWeatherDto dto) {
        return modelMapper.map(dto, RealtimeWeather.class);
    }

    @GetMapping
    public ResponseEntity<?> getRealtimeWeatherByIpAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);
        Location locationMappedFromIp = geolocationService.getLocation(ipAddress);
        RealtimeWeather realtimeWeather = realtimeWeatherService.getRealtimeWeatherByLocation(locationMappedFromIp);
        return ResponseEntity.ok(entityToDto(realtimeWeather));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getRealtimeWeatherByLocation(@PathVariable String code) {
        RealtimeWeather locationInRequest = realtimeWeatherService.getRealtimeWeatherByCode(code);
        return ResponseEntity.ok(entityToDto(locationInRequest));
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateRealtimeWeatherByCode(@RequestBody @Valid RealtimeWeatherDto realtimeWeatherDto, @PathVariable String code) {

        RealtimeWeather realtimeWeatherBodyRequest = dtoToEntity(realtimeWeatherDto);
        realtimeWeatherBodyRequest.setLocationCode(code);

        RealtimeWeather updatedRealtime = realtimeWeatherService.updateRealtimeWeatherByCode(code, realtimeWeatherBodyRequest);
        return ResponseEntity.ok(entityToDto(updatedRealtime));
    }


}
