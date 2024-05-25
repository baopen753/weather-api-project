package com.baopen753.weatherapiproject.locationservices.service.impl;

import com.baopen753.weatherapiproject.locationservices.dto.LocationDto;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.exception.LocationExistedException;
import com.baopen753.weatherapiproject.locationservices.exception.LocationNotFoundException;
import com.baopen753.weatherapiproject.locationservices.repository.LocationRepository;
import com.baopen753.weatherapiproject.locationservices.service.ILocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements ILocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public List<Location> findAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public Location findLocationByCode(String code) {

        try {
            Location location = locationRepository.findByCode(code);
            return location;
        } catch (LocationNotFoundException exception) {
            throw new LocationNotFoundException("Not found location by code: " + code);
        }

    }

    @Override
    public List<Location> findLocationByCountryCodeAndRegionCode(String regionName, String countryCode) {

        try {
            List<Location> locations = locationRepository.findByCountryCodeAndRegionName(regionName, countryCode);
            return locations;
        } catch (LocationNotFoundException exception) {
            throw new LocationNotFoundException("Not found location by country code: " + countryCode + " and region code: " + regionName);
        }

    }

    @Override
    public Location save(Location location) {
        try {
            Location addedLocation = locationRepository.save(location);
            return addedLocation;
        } catch (LocationExistedException exception) {
            throw new LocationExistedException("The location with ID " + location.getCode() + " is exised. Try again !");
        }
    }

    @Override
    public Location update(LocationDto locationDto) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }
}
