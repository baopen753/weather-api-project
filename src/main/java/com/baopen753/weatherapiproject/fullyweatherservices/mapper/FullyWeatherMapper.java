package com.baopen753.weatherapiproject.fullyweatherservices.mapper;

import com.baopen753.weatherapiproject.dailyweatherservices.entity.DailyWeather;
import com.baopen753.weatherapiproject.dailyweatherservices.mapper.DailyWeatherMapper;
import com.baopen753.weatherapiproject.hourlyweatherservices.mapper.HourlyWeatherMapper;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeather;
import com.baopen753.weatherapiproject.realtimeservices.entity.RealtimeWeather;

import com.baopen753.weatherapiproject.dailyweatherservices.dto.DailyWeatherDto;
import com.baopen753.weatherapiproject.fullyweatherservices.dto.FullyWeatherDto;
import com.baopen753.weatherapiproject.hourlyweatherservices.dto.HourlyWeatherDto;
import com.baopen753.weatherapiproject.realtimeservices.dto.RealtimeWeatherDto;

import com.baopen753.weatherapiproject.realtimeservices.mapper.RealtimeWeatherMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {DailyWeatherMapper.class, HourlyWeatherMapper.class, RealtimeWeatherMapper.class})
public interface FullyWeatherMapper {

    FullyWeatherMapper INSTANCE = Mappers.getMapper(FullyWeatherMapper.class);

    @Mappings({
            @Mapping(target = "location", source = "location", qualifiedByName = "mapLocationToString"),
            @Mapping(source = "realtimeWeather", target = "realtimeWeather"),
            @Mapping(source = "dailyWeatherList", target = "dailyWeatherList"),
            @Mapping(source = "hourlyWeatherList", target = "hourlyWeatherList")
    })
    FullyWeatherDto entityToDto(Location location);

    @Mappings({
            @Mapping(source = "realtimeWeather", target = "realtimeWeather"),
            @Mapping(source = "hourlyWeatherList", target = "hourlyWeatherList"),
            @Mapping(source = "dailyWeatherList", target = "dailyWeatherList")
    })
    Location dtoToEntity(FullyWeatherDto fullyWeatherDto);


    // customize mapping method: Object: Location --> String: locationAddress
    @Named("mapLocationToString")
    default String mapLocationToString(Location location) {
        return location != null ? location.toString() : null;
    }
}