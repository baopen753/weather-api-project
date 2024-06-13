package com.baopen753.weatherapiproject.realtimeservices.restcontroller;


import com.baopen753.weatherapiproject.CommonUtility;
import com.baopen753.weatherapiproject.GeolocationService;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.realtimeservices.entity.RealtimeWeather;
import com.baopen753.weatherapiproject.realtimeservices.service.RealtimeWeatherService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/realtime")
public class RealtimeWeatherRestController {


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

        Location location = new Location();
        location.setCountryCode("USA");
        location.setCityName("Los Angeles");

        String ipAddress = CommonUtility.getIPAddress(request);
        Location locationMappedFromIp = geolocationService.getLocation(ipAddress);
        RealtimeWeather realtimeWeather = realtimeWeatherService.getRealtimeWeatherByLocation(locationMappedFromIp);
        return ResponseEntity.ok(realtimeWeather);
    }


}
