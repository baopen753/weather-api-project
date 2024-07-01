package com.baopen753.weatherapiproject.hourlyweatherservices.service;

import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeather;
import com.baopen753.weatherapiproject.hourlyweatherservices.repository.HourlyWeatherRepository;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.exception.LocationNotFoundException;
import com.baopen753.weatherapiproject.locationservices.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
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

        Location locationInDb = locationRepository.findLocationByCityNameAndCountryCode(cityName, countryCode);
        if (locationInDb == null) throw new LocationNotFoundException(countryCode, cityName);

        List<HourlyWeather> hourlyWeatherList = hourlyWeatherRepository.findHourlyWeatherByLocationCode(locationInDb.getCode(), currentHour);
        return hourlyWeatherList;
    }

    public List<HourlyWeather> getHourlyWeatherByCode(String code, Integer currentHour) {
        Location locationInDb = locationRepository.findLocationsByCode(code);

        if (locationInDb == null) throw new LocationNotFoundException(code);

        List<HourlyWeather> hourlyWeatherList = hourlyWeatherRepository.findHourlyWeatherByLocationCode(locationInDb.getCode(), currentHour);
        return hourlyWeatherList;
    }

    public List<HourlyWeather> updateHourlyWeatherByLocationCode(String code, List<HourlyWeather> hourlyWeatherListInRequest) {

        // 1. check there is an existed location with input code
        Location locationInDb = locationRepository.findLocationsByCode(code);
        if (locationInDb == null) throw new LocationNotFoundException(code);


        // 2. Set location property of HourWeatherId in each item within ListInRequest
        for(HourlyWeather item : hourlyWeatherListInRequest) {
            item.getHourlyWeatherId().setLocation(locationInDb);
        }


        // 3. list all items in ListInDb haven't existed in ListInRequest   --> in order to delete them
        List<HourlyWeather> hourlyWeatherListInDb = locationInDb.getHourlyWeatherList();
        List<HourlyWeather> hourlyWeatherListToBeDeleted = new ArrayList<>();

        for (HourlyWeather item : hourlyWeatherListInDb) {
            if (!hourlyWeatherListInRequest.contains(item))                  // need to manually config equals() & hashCode() due to this contains() method
                hourlyWeatherListToBeDeleted.add(item.getShallowCopy());     // potentially vulnerable StackOverFlow due to equals(), hashCode() in Entity class
        }

        // 4. delete all item in db which existed in ToBeRemoved
        for (HourlyWeather item : hourlyWeatherListToBeDeleted) {
            hourlyWeatherListInDb.remove(item);
        }

        return hourlyWeatherRepository.saveAll(hourlyWeatherListInRequest);
    }


}












