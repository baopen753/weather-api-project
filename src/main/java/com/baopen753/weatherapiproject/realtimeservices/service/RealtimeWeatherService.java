package com.baopen753.weatherapiproject.realtimeservices.service;

import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.exception.LocationNotFoundException;
import com.baopen753.weatherapiproject.locationservices.repository.LocationRepository;
import com.baopen753.weatherapiproject.realtimeservices.entity.RealtimeWeather;
import com.baopen753.weatherapiproject.realtimeservices.repository.RealtimeWeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RealtimeWeatherService {

    private RealtimeWeatherRepository realtimeWeatherRepository;
    private LocationRepository locationRepository;

    @Autowired
    public RealtimeWeatherService(RealtimeWeatherRepository realtimeWeatherRepository, LocationRepository locationRepository) {
        this.realtimeWeatherRepository = realtimeWeatherRepository;
        this.locationRepository = locationRepository;
    }

    public RealtimeWeather getRealtimeWeatherByLocation(Location location) throws LocationNotFoundException {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findLocationByCountryCodeAndCity(countryCode, cityName);
        if (realtimeWeather == null) throw new LocationNotFoundException(countryCode, cityName);
        return realtimeWeather;
    }


}
