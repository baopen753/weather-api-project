package com.baopen753.weatherapiproject.fullyweatherservices.service;

import com.baopen753.weatherapiproject.dailyweatherservices.dto.DailyWeatherDto;
import com.baopen753.weatherapiproject.dailyweatherservices.entity.DailyWeather;
import com.baopen753.weatherapiproject.dailyweatherservices.mapper.DailyWeatherMapper;
import com.baopen753.weatherapiproject.dailyweatherservices.repository.DailyWeatherRepository;
import com.baopen753.weatherapiproject.fullyweatherservices.dto.FullyWeatherDto;
import com.baopen753.weatherapiproject.hourlyweatherservices.dto.HourlyWeatherDto;
import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeather;
import com.baopen753.weatherapiproject.hourlyweatherservices.mapper.HourlyWeatherMapper;
import com.baopen753.weatherapiproject.hourlyweatherservices.repository.HourlyWeatherRepository;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.exception.LocationNotFoundException;
import com.baopen753.weatherapiproject.locationservices.repository.LocationRepository;
import com.baopen753.weatherapiproject.realtimeservices.dto.RealtimeWeatherDto;
import com.baopen753.weatherapiproject.realtimeservices.entity.RealtimeWeather;
import com.baopen753.weatherapiproject.realtimeservices.mapper.RealtimeWeatherMapper;
import com.baopen753.weatherapiproject.realtimeservices.repository.RealtimeWeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FullyWeatherService {

    @Autowired
    private RealtimeWeatherRepository realtimeWeatherRepository;

    @Autowired
    private DailyWeatherRepository dailyWeatherRepository;

    @Autowired
    private HourlyWeatherRepository hourlyWeatherRepository;

    @Autowired
    private LocationRepository locationRepository;


    private List<DailyWeatherDto> entityListToDailyWeatherDtoList(List<DailyWeather> dailyWeatherList) {
        List<DailyWeatherDto> dailyWeatherDtoList = new ArrayList<>();
        dailyWeatherList.forEach(entity -> {
            DailyWeatherDto dto = DailyWeatherMapper.INSTANCE.entityToDto(entity);
            dailyWeatherDtoList.add(dto);
        });
        return dailyWeatherDtoList;
    }


    private List<HourlyWeatherDto> entityListToHourlyWeatherDtoList(List<HourlyWeather> hourlyWeatherList) {
        List<HourlyWeatherDto> hourlyWeatherDtoList = new ArrayList<>();
        hourlyWeatherList.forEach(entity -> {
            HourlyWeatherDto dto = HourlyWeatherMapper.INSTANCE.entityToDto(entity);
            hourlyWeatherDtoList.add(dto);
        });
        return hourlyWeatherDtoList;
    }

    private List<DailyWeather> dtoListToDailyWeatheEntityList(List<DailyWeatherDto> dailyWeatherDtoList) {
        List<DailyWeather> dailyWeatherEntityList = new ArrayList<>();
        dailyWeatherDtoList.forEach(dto -> {
            DailyWeather entity = DailyWeatherMapper.INSTANCE.dtoToEntity(dto);
            dailyWeatherEntityList.add(entity);
        });
        return dailyWeatherEntityList;
    }

    private List<HourlyWeather> dtoListToHourlyWeatherEntityList(List<HourlyWeatherDto> hourlyWeatherDtoList) {
        List<HourlyWeather> hourlyWeatherEntityList = new ArrayList<>();
        hourlyWeatherDtoList.forEach(dto -> {
            HourlyWeather entity = HourlyWeatherMapper.INSTANCE.dtoToEntity(dto);
            hourlyWeatherEntityList.add(entity);
        });
        return hourlyWeatherEntityList;
    }


    public FullyWeatherDto getFullyWeatherByLocation(Location locationFromIp, Integer currentHour) {
        String cityName = locationFromIp.getCityName();
        String countryCode = locationFromIp.getCountryCode();

        Location locationInDb = locationRepository.findLocationByCityNameAndCountryCode(cityName, countryCode);
        if (locationInDb == null)
            throw new LocationNotFoundException(countryCode, cityName);

        FullyWeatherDto fullyWeatherDto = new FullyWeatherDto();
        String locationAddress = locationInDb.toString();

        // get entities
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findLocationByCountryCodeAndCity(countryCode, cityName);
        if(realtimeWeather == null) {
            realtimeWeather = new RealtimeWeather();
        }

        List<HourlyWeather> hourlyWeatherList = hourlyWeatherRepository.findHourlyWeatherByLocationCode(locationInDb.getCode(), currentHour);
        List<DailyWeather> dailyWeatherList = dailyWeatherRepository.findDailyWeathersByLocationCode(locationInDb.getCode());

        // convert into dto
        RealtimeWeatherDto realtimeWeatherDto = RealtimeWeatherMapper.INSTANCE.entityToDto(realtimeWeather);
        List<HourlyWeatherDto> hourlyWeatherDtoList = entityListToHourlyWeatherDtoList(hourlyWeatherList);
        List<DailyWeatherDto> dailyWeatherDtoList = entityListToDailyWeatherDtoList(dailyWeatherList);

        // setting properties for FullyWeatherDto
        fullyWeatherDto.setLocation(locationAddress);
        fullyWeatherDto.setRealtimeWeather(realtimeWeatherDto);
        fullyWeatherDto.setHourlyWeatherList(hourlyWeatherDtoList);
        fullyWeatherDto.setDailyWeatherList(dailyWeatherDtoList);

        return fullyWeatherDto;
    }

    public FullyWeatherDto getFullyWeatherByCode(String code, Integer currentHour) {

        Location locationInDb = locationRepository.findLocationsByCode(code);
        if (locationInDb == null)
            throw new LocationNotFoundException(code);

        String countryCode = locationInDb.getCountryCode();
        String cityName = locationInDb.getCityName();

        FullyWeatherDto fullyWeatherDto = new FullyWeatherDto();
        String locationAddress = locationInDb.toString();

        // get entities
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findLocationByCountryCodeAndCity(countryCode, cityName);
        if(realtimeWeather == null) {
            realtimeWeather = new RealtimeWeather();
        }

        List<HourlyWeather> hourlyWeatherList = hourlyWeatherRepository.findHourlyWeatherByLocationCode(code, currentHour);
        List<DailyWeather> dailyWeatherList = dailyWeatherRepository.findDailyWeathersByLocationCode(code);

        // convert into dto
        RealtimeWeatherDto realtimeWeatherDto = RealtimeWeatherMapper.INSTANCE.entityToDto(realtimeWeather);

        List<HourlyWeatherDto> hourlyWeatherDtoList = new ArrayList<>();
        hourlyWeatherList.forEach(entity -> {
            HourlyWeatherDto dto = HourlyWeatherMapper.INSTANCE.entityToDto(entity);
            hourlyWeatherDtoList.add(dto);
        });

        List<DailyWeatherDto> dailyWeatherDtoList = new ArrayList<>();
        dailyWeatherList.forEach(entity -> {
            DailyWeatherDto dto = DailyWeatherMapper.INSTANCE.entityToDto(entity);
            dailyWeatherDtoList.add(dto);
        });

        // setting properties for FullyWeatherDto
        fullyWeatherDto.setLocation(locationAddress);
        fullyWeatherDto.setRealtimeWeather(realtimeWeatherDto);
        fullyWeatherDto.setHourlyWeatherList(hourlyWeatherDtoList);
        fullyWeatherDto.setDailyWeatherList(dailyWeatherDtoList);

        return fullyWeatherDto;
    }

    public Location updateFullyWeatherByCode(String code, Location locationInRequest) {
        Location locationFromDb = locationRepository.findLocationsByCode(code);
        if (locationFromDb == null)
            throw new LocationNotFoundException(code);


        // set location property
        setLocationPropertyForFullWeather(locationInRequest, locationFromDb);

        // check Realtime existed or not due to haven't yet checked in Controller
        saveRealtimeWeatherifNotExisted(locationInRequest, locationFromDb);

        locationInRequest.cloneFieldsFrom(locationFromDb);


        return locationRepository.save(locationInRequest);
    }

    private void saveRealtimeWeatherifNotExisted(Location locationInRequest, Location locationFromDb) {
        if (locationFromDb.getRealtimeWeather() == null)
            locationFromDb.setRealtimeWeather(locationInRequest.getRealtimeWeather());
        locationRepository.save(locationFromDb);
    }

    private void setLocationPropertyForFullWeather(Location locationInRequest, Location locationFromDb) {
        RealtimeWeather realtimeWeather = locationInRequest.getRealtimeWeather();
        realtimeWeather.setLocation(locationFromDb);
        realtimeWeather.setLocationCode(locationFromDb.getCode());    // set code as primary key for Realtime from request in order to locationRepo.save()
        realtimeWeather.setLastUpdated(new Date());

        List<DailyWeather> dailyWeatherList = locationInRequest.getDailyWeatherList();
        dailyWeatherList.forEach(dailyWeather -> {
            dailyWeather.getDailyWeatherId().setLocation(locationFromDb);
        });

        List<HourlyWeather> hourlyWeatherList = locationInRequest.getHourlyWeatherList();
        hourlyWeatherList.forEach(hourlyWeather -> {
            hourlyWeather.getHourlyWeatherId().setLocation(locationFromDb);
        });
    }


}
