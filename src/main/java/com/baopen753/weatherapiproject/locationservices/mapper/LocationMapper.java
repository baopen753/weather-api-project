package com.baopen753.weatherapiproject.locationservices.mapper;

import com.baopen753.weatherapiproject.locationservices.dto.LocationDto;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocationMapper {
    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);


    @Mappings({
            @Mapping(source = "code", target = "code"),
            @Mapping(source = "cityName", target = "cityName"),
            @Mapping(source = "countryName", target = "countryName"),
            @Mapping(source = "regionName", target = "regionName"),
            @Mapping(source = "countryCode", target = "countryCode")
    })
    Location dtoToEntity(LocationDto location);

    @Mappings({
            @Mapping(source = "code", target = "code"),
            @Mapping(source = "cityName", target = "cityName"),
            @Mapping(source = "countryName", target = "countryName"),
            @Mapping(source = "regionName", target = "regionName"),
            @Mapping(source = "countryCode", target = "countryCode")
    })
    LocationDto entityToDto(Location location);
}
