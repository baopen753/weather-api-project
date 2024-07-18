package com.baopen753.weatherapiproject.dailyweatherservices.service;

import com.baopen753.weatherapiproject.dailyweatherservices.entity.DailyWeather;
import com.baopen753.weatherapiproject.dailyweatherservices.repository.DailyWeatherRepository;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.exception.LocationNotFoundException;
import com.baopen753.weatherapiproject.locationservices.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DailyWeatherService {

    @Autowired
    private DailyWeatherRepository dailyWeatherRepository;

    @Autowired
    private LocationRepository locationRepository;

    public List<DailyWeather> getDailyWeathersByLocation(Location locationFromIp) {

        String cityName = locationFromIp.getCityName();
        String countryCode = locationFromIp.getCountryCode();

        Location locationInDb = locationRepository.findLocationByCityNameAndCountryCode(cityName, countryCode);
        if (locationInDb == null) throw new LocationNotFoundException(cityName, countryCode);

        List<DailyWeather> dailyWeatherList = dailyWeatherRepository.findDailyWeathersByLocationCode(locationInDb.getCode());
        return dailyWeatherList;
    }

    public List<DailyWeather> getDailyWeathersByCode(String code) {
        Location locationFromCode = locationRepository.findLocationsByCode(code);
        if (locationFromCode == null) throw new LocationNotFoundException(code);

        List<DailyWeather> dailyWeatherList = dailyWeatherRepository.findDailyWeathersByLocationCode(locationFromCode.getCode());
        return dailyWeatherList;
    }


    public List<DailyWeather> updateDailyWeathersByCode(String code, List<DailyWeather> dailyWeatherListFromRequest) {
        Location locationFromCode = locationRepository.findLocationsByCode(code);
        if(locationFromCode == null) throw new LocationNotFoundException(code);

        // 1. set location properties for all item of ListFromRequest
        dailyWeatherListFromRequest.forEach(entity->{
            entity.getDailyWeatherId().setLocation(locationFromCode);
        });

        // 2. get list daily weather from db based on location from code
        List<DailyWeather> dailyWeatherListFromDb = locationFromCode.getDailyWeatherList();

        // 3. get all DailyWeather from Db which haven't existed in ListFromRequest  ---> in order to be removed
        List<DailyWeather> dailyWeatherListToBeRemoved = new ArrayList<>();

        dailyWeatherListFromDb.forEach(entity->{
            if(!dailyWeatherListFromRequest.contains(entity))
                dailyWeatherListToBeRemoved.add(entity);
        });

        // 4. From ListDb, delete all dailyWeather from  ListTobeRemoved
        dailyWeatherListFromDb.removeAll(dailyWeatherListToBeRemoved);    // this can affect database due to 'cascade = 'CascadeType.REMOVE' on @OneToMany relationship at Location

        return dailyWeatherRepository.saveAll(dailyWeatherListFromRequest);







    }


}
