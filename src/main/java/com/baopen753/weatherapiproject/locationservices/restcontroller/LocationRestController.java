package com.baopen753.weatherapiproject.locationservices.restcontroller;

import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.service.ILocationService;
import com.baopen753.weatherapiproject.locationservices.service.impl.LocationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationRestController {


    private ILocationService locationService;

    @Autowired
    public LocationRestController(LocationServiceImpl locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<List<Location>> getLocations() {
        List<Location> locationList = locationService.findAllLocations();
        if (locationList.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(locationList);
    }

    @GetMapping("/{countryCode}/{regionName}")
    public ResponseEntity<List<Location>> getLocationsByCountryAndRegion(@PathVariable("regionName") String regionName, @PathVariable("countryCode") String countryCode) {
        List<Location> locationList = locationService.findLocationByCountryCodeAndRegionCode(regionName, countryCode);
        if (locationList.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(locationList);

    }

    @GetMapping("/{code}")
    public ResponseEntity<Location> findLocationByCode(@PathVariable("code") String code) {
        Location location = locationService.findLocationByCode(code);
        if (location == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(location);
    }


}
