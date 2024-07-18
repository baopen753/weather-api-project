package com.baopen753.weatherapiproject.dailyweatherservices.mapper;


import com.baopen753.weatherapiproject.dailyweatherservices.dto.DailyWeatherDto;
import com.baopen753.weatherapiproject.dailyweatherservices.dto.DailyWeatherListDto;
import com.baopen753.weatherapiproject.dailyweatherservices.entity.DailyWeather;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DailyWeatherMapper {

    DailyWeatherMapper INSTANCE = Mappers.getMapper(DailyWeatherMapper.class);

    @Mappings({
            @Mapping(source = "minTemperature", target = "minTemperature"),
            @Mapping(source = "maxTemperature", target = "maxTemperature"),
            @Mapping(source = "precipitation", target = "precipitation"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "dailyWeatherId.dayOfMonth", target = "dayOfMonth"),
            @Mapping(source = "dailyWeatherId.month", target = "month")
    })
    DailyWeatherDto entityToDto(DailyWeather dailyWeather);


    @Mappings({
            @Mapping(source = "minTemperature", target = "minTemperature"),
            @Mapping(source = "maxTemperature", target = "maxTemperature"),
            @Mapping(source = "precipitation", target = "precipitation"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "dayOfMonth", target = "dailyWeatherId.dayOfMonth"),
            @Mapping(source = "month", target = "dailyWeatherId.month")
    })
    DailyWeather dtoToEntity(DailyWeatherDto dailyWeatherDto);

    List<DailyWeatherDto> toDtoList(List<DailyWeather> dailyWeatherList);
    List<DailyWeather> toEntityList(List<DailyWeatherDto> dailyWeatherDtoList);

}
