package com.baopen753.weatherapiproject.base;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.baopen753.weatherapiproject.dailyweatherservices.restcontroller.DailyWeatherRestController;
import com.baopen753.weatherapiproject.fullyweatherservices.restcontroller.FullyWeatherRestController;
import com.baopen753.weatherapiproject.hourlyweatherservices.restcontroller.HourlyWeatherRestController;
import com.baopen753.weatherapiproject.locationservices.restcontroller.LocationRestController;
import com.baopen753.weatherapiproject.realtimeservices.restcontroller.RealtimeWeatherRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/")
    public ResponseEntity<BaseEntity> handleBaseUrl() {
        return ResponseEntity.ok(createRootBaseUrl());
    }

    /*
     * This method will create a multiple URL, each one belongs to compatible RestController which have specific url corresponding with a method
     */
    private BaseEntity createRootBaseUrl() {
        BaseEntity baseEntity = new BaseEntity();

        String locationByIpUrl = linkTo(methodOn(LocationRestController.class).getLocations(null, null)).toString();
        baseEntity.setLocationByIpUrl(locationByIpUrl);

        String locationByCodeUrl = linkTo(methodOn(LocationRestController.class).findLocationByCode(null)).toString();
        baseEntity.setLocationByCodeUrl(locationByCodeUrl);

        String realtimeWeatherByIpUrl = linkTo(methodOn(RealtimeWeatherRestController.class).getRealtimeWeatherByIpAddress(null)).toString();
        baseEntity.setRealtimeWeatherByIpUrl(realtimeWeatherByIpUrl);

        String realtimeWeatherByCodeUrl = linkTo(methodOn(RealtimeWeatherRestController.class).getRealtimeWeatherByLocation(null)).toString();
        baseEntity.setRealtimeWeatherByCodeUrl(realtimeWeatherByCodeUrl);

        String dailyWeatherByIpUrl = linkTo(methodOn(DailyWeatherRestController.class).getDailyWeathersByIpAddress(null)).toString();
        baseEntity.setDailyWeatherByIpUrl(dailyWeatherByIpUrl);

        String dailyWeatherByCodeUrl = linkTo(methodOn(DailyWeatherRestController.class).getDailyWeathersByCode(null)).toString();
        baseEntity.setDailyWeatherByCodeUrl(dailyWeatherByCodeUrl);

        String hourlyWeatherByIpUrl = linkTo(methodOn(HourlyWeatherRestController.class).listHourlyWeatherForecastByIpAddress(null)).toString();
        baseEntity.setHourlyWeatherByIpUrl(hourlyWeatherByIpUrl);

        String hourlyWeatherByCodeUrl = linkTo(methodOn(HourlyWeatherRestController.class).listHourlyWeatherForecastByCode(null, null)).toString();
        baseEntity.setHourlyWeatherByCodeUrl(hourlyWeatherByCodeUrl);

        String fullyWeatherByIpUrl = linkTo(methodOn(FullyWeatherRestController.class).getFullyWeatherByIpAddress(null)).toString();
        baseEntity.setFullyWeatherByIpUrl(fullyWeatherByIpUrl);

        String fullyWeatherByCodeUrl = linkTo(methodOn(FullyWeatherRestController.class).getFullyWeatherByCode(null, null)).toString();
        baseEntity.setFullyWeatherByCodeUrl(fullyWeatherByCodeUrl);

        return baseEntity;
    }
}
