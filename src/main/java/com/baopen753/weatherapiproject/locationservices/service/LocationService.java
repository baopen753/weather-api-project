package com.baopen753.weatherapiproject.locationservices.service;

import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.exception.LocationExistedException;
import com.baopen753.weatherapiproject.locationservices.exception.LocationNotFoundException;
import com.baopen753.weatherapiproject.locationservices.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocationService {

    private LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> findAllLocations() {
        return locationRepository.findAll();
    }

    public Location findLocationByCode(String code) throws LocationNotFoundException {
        Location location = locationRepository.findLocationsByCode(code);
        if (location == null) throw new LocationNotFoundException("Not found location by code: " + code);
        return location;
    }

    public Location checkLocationExist(String code) {
        Location location = this.locationRepository.findLocationsByCode(code);
        return location != null ? location : null;
    }


    public List<Location> findLocationByCountryCode(String countryCode) throws LocationNotFoundException {
        List<Location> locationList = locationRepository.findAll().stream().filter(location -> location.getCountryCode().equalsIgnoreCase(countryCode)).toList();
        if (locationList.isEmpty() || locationList == null)
            throw new LocationNotFoundException("Not found location by country code: " + countryCode);
        return locationList;
    }


    @Transactional
    public Location create(Location location) throws LocationExistedException {
        Location locationInDb = locationRepository.findLocationsByCode(location.getCode());
        if (locationInDb != null) throw new LocationExistedException("Duplicated location code. Try again !");
        return locationRepository.save(location);
    }


    @Transactional
    public Location update(Location locationInRequest) throws LocationNotFoundException {

        String codeInRequest = locationInRequest.getCode();
        Location locationInDb = locationRepository.findLocationsByCode(codeInRequest);
        if (locationInDb == null)
            throw new LocationNotFoundException("No found location by code: " + codeInRequest);

        locationInDb.setCountryName(locationInRequest.getCountryName());
        locationInDb.setRegionName(locationInRequest.getRegionName());
        locationInDb.setCountryCode(locationInRequest.getCountryCode());
        locationInDb.setCityName(locationInRequest.getCityName());
        locationInDb.setEnabled(locationInRequest.isEnabled());

        return locationRepository.save(locationInDb);
    }


    @Transactional
    public void delete(String code) throws LocationNotFoundException {
        Location locationInDb = locationRepository.findLocationsByCode(code);
        if (locationInDb == null) throw new LocationNotFoundException(("Not found location by id: " + code));
        locationRepository.deleteByCode(code);
    }


}
