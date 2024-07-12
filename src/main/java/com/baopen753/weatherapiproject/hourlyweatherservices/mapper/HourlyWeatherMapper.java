package com.baopen753.weatherapiproject.hourlyweatherservices.mapper;

import com.baopen753.weatherapiproject.hourlyweatherservices.dto.HourlyWeatherDto;
import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeather;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface HourlyWeatherMapper {

    HourlyWeatherMapper INSTANCE = Mappers.getMapper(HourlyWeatherMapper.class);

    @Mappings({
            @Mapping(source = "hourlyWeatherId.hourOfDay", target = "hourOfDay"),
            @Mapping(source = "temperature", target = "temperature"),
            @Mapping(source = "precipitation", target = "precipitation"),
            @Mapping(source = "status", target = "status")
    })
    HourlyWeatherDto entityToDto(HourlyWeather hourlyWeather);



    @Mappings({
            @Mapping(source = "hourOfDay", target = "hourlyWeatherId.hourOfDay"),
            @Mapping(source = "temperature", target = "temperature"),
            @Mapping(source = "precipitation", target = "precipitation"),
            @Mapping(source = "status", target = "status")
    })
    HourlyWeather dtoToEntity(HourlyWeatherDto dto);                        // note: this method has not yet mapped Location of hourlyWeatherId

    List<HourlyWeatherDto> toDtoList(List<HourlyWeather> hourlyWeatherList);
    List<HourlyWeather> toEntityList(List<HourlyWeatherDto> hourlyWeatherDtoList);

}
