package com.baopen753.weatherapiproject.locationservices.service;

import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.exception.LocationExistedException;
import com.baopen753.weatherapiproject.locationservices.exception.LocationNotFoundException;
import com.baopen753.weatherapiproject.locationservices.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Deprecated
    public List<Location> findAllLocations() {
        return locationRepository.findAll();
    }

    public Page<Location> findAllLocationsByPage(int pageNum, int pageSize, String sortField) {
        Sort sort = Sort.by(sortField).ascending();
        Pageable locationPageable = PageRequest.of(pageNum, pageSize, sort);
        return locationRepository.findAllLocations(locationPageable);
    }

    public Location findLocationByCode(String code) {
        Location location = locationRepository.findLocationsByCode(code);
        if (location == null) throw new LocationNotFoundException(code);
        return location;
    }

    @Transactional
    public Location create(Location location) {
        Location locationInDb = locationRepository.findLocationsByCode(location.getCode());
        if (locationInDb != null) throw new LocationExistedException("Duplicated location code. Try again !");
        return locationRepository.save(location);
    }

    @Transactional
    public Location update(Location locationInRequest) {

        String codeInRequest = locationInRequest.getCode();
        Location locationInDb = locationRepository.findLocationsByCode(codeInRequest);
        if (locationInDb == null)
            throw new LocationNotFoundException(codeInRequest);

        locationInDb.setCountryName(locationInRequest.getCountryName());
        locationInDb.setRegionName(locationInRequest.getRegionName());
        locationInDb.setCountryCode(locationInRequest.getCountryCode());
        locationInDb.setCityName(locationInRequest.getCityName());
        locationInDb.setEnabled(locationInRequest.isEnabled());

        return locationRepository.save(locationInDb);
    }

    @Transactional
    public void delete(String code) {
        Location locationInDb = locationRepository.findLocationsByCode(code);
        if (locationInDb == null) throw new LocationNotFoundException((code));
        locationRepository.deleteByCode(code);
    }


}
