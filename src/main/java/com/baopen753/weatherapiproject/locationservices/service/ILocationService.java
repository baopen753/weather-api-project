package com.baopen753.weatherapiproject.locationservices.service;

import com.baopen753.weatherapiproject.locationservices.dto.LocationDto;
import com.baopen753.weatherapiproject.locationservices.entity.Location;

import java.util.List;

public interface ILocationService {

    List<Location> findAllLocations();

    Location findLocationByCode(String code);

    List<Location> findLocationByCountryCodeAndRegionCode(String regionName, String countryCode);

    Location save(LocationDto locationDto);

    Location update(LocationDto locationDto);

    void deleteById(String id);

}
