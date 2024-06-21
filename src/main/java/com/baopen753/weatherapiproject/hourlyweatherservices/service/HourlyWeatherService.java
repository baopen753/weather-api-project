package com.baopen753.weatherapiproject.hourlyweatherservices.service;

import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeather;
import com.baopen753.weatherapiproject.hourlyweatherservices.repository.HourlyWeatherRepository;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.exception.LocationNotFoundException;
import com.baopen753.weatherapiproject.locationservices.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HourlyWeatherService {


    private HourlyWeatherRepository hourlyWeatherRepository;
    private LocationRepository locationRepository;

    public HourlyWeatherService(HourlyWeatherRepository hourlyWeatherRepository, LocationRepository locationRepository) {
        this.hourlyWeatherRepository = hourlyWeatherRepository;
        this.locationRepository = locationRepository;
    }


    public List<HourlyWeather> getHourlyWeatherByLocation(Location location, Integer currentHour) {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        List<HourlyWeather> hourlyWeatherList = hourlyWeatherRepository.findHourlyWeathersByCountryCodeAndCity(countryCode, cityName, currentHour);
        return hourlyWeatherList;
    }
}
