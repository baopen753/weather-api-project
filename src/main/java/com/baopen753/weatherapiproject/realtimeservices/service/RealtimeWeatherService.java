package com.baopen753.weatherapiproject.realtimeservices.service;

import com.baopen753.weatherapiproject.global.ErrorDTO;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.exception.LocationNotFoundException;
import com.baopen753.weatherapiproject.locationservices.repository.LocationRepository;
import com.baopen753.weatherapiproject.realtimeservices.entity.RealtimeWeather;
import com.baopen753.weatherapiproject.realtimeservices.exception.RealtimeNotUpdatedException;
import com.baopen753.weatherapiproject.realtimeservices.repository.RealtimeWeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

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

    public RealtimeWeather getRealtimeWeatherByCode(String code) throws LocationNotFoundException, RealtimeNotUpdatedException {
        Location location = locationRepository.findLocationsByCode(code);
        if (location == null) throw new LocationNotFoundException("Not found location with code: " + code);

        if (location.getRealtimeWeather() == null) throw new RealtimeNotUpdatedException(location.getCode());

        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findLocationByCountryCodeAndCity(countryCode, cityName);
        return realtimeWeather;
    }

    public RealtimeWeather updateRealtimeWeatherByCode(String code, RealtimeWeather newRealtimeWeather) throws LocationNotFoundException {

        Location locationInDb = locationRepository.findLocationsByCode(code);
        if (locationInDb == null) throw new LocationNotFoundException("Not found location with code: " + code);

        newRealtimeWeather.setLocation(locationInDb);
        newRealtimeWeather.setLastUpdated(new Date());

        if (locationInDb.getRealtimeWeather() == null) {
            locationInDb.setRealtimeWeather(newRealtimeWeather);
            locationRepository.save(locationInDb);
            return locationInDb.getRealtimeWeather();
        }

        return realtimeWeatherRepository.save(newRealtimeWeather);
    }


}
