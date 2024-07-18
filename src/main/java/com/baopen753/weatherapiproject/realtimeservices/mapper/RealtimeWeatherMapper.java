package com.baopen753.weatherapiproject.realtimeservices.mapper;

import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.realtimeservices.dto.RealtimeWeatherDto;
import com.baopen753.weatherapiproject.realtimeservices.entity.RealtimeWeather;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RealtimeWeatherMapper {

    RealtimeWeatherMapper INSTANCE = Mappers.getMapper(RealtimeWeatherMapper.class);

    @Mappings({
            @Mapping(source = "temperature", target = "temperature"),
            @Mapping(source = "humidity", target = "humidity"),
            @Mapping(source = "precipitation", target = "precipitation"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "windSpeed", target = "windSpeed"),
            @Mapping(source = "lastUpdated", target = "lastUpdated")
    })
    RealtimeWeather dtoToEntity(RealtimeWeatherDto dto);


    @Mappings({
            @Mapping(source = "temperature", target = "temperature"),
            @Mapping(source = "humidity", target = "humidity"),
            @Mapping(source = "precipitation", target = "precipitation"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "windSpeed", target = "windSpeed"),
            @Mapping(source = "lastUpdated", target = "lastUpdated")
    })
    RealtimeWeatherDto entityToDto(RealtimeWeather entity);
}
