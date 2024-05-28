package com.baopen753.weatherapiproject.locationservices.restcontroller;

import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.exception.LocationNotFoundException;
import com.baopen753.weatherapiproject.locationservices.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/v1/locations")
public class LocationRestController {


    private LocationService locationService;

    @Autowired
    public LocationRestController(LocationService locationService) {
        this.locationService = locationService;
    }


    @GetMapping
    public ResponseEntity<List<Location>> getLocations() {
        List<Location> locationList = locationService.findAllLocations();
        if (locationList.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(locationList);
    }


    @GetMapping("/{code}")
    public ResponseEntity<Location> findLocationByCode(@PathVariable("code") String code) {
        try {
            Location location = locationService.findLocationByCode(code);
            return ResponseEntity.ok(location);
        } catch (LocationNotFoundException exception) {
            return ResponseEntity.status(404).build();
        }
    }


    @PostMapping
    public ResponseEntity<Location> addLocation(@Valid @RequestBody Location location, BindingResult bindingResult) {   // BindingResult is used to collect validation errors

        Location locationCheckExisted = locationService.checkLocationExist(location.getCode());
        if (locationCheckExisted != null) return ResponseEntity.status(409).build();
        Location persistedLocation = locationService.create(location);
        URI uri = URI.create("/api/v1/locations/" + location.getCode());
        return ResponseEntity.created(uri).body(persistedLocation);

    }


    @PutMapping
    public ResponseEntity<Location> updateLocation(@Valid @RequestBody Location location) {
        try {
            Location updatedLocation = locationService.update(location);
            return ResponseEntity.ok(updatedLocation);
        } catch (LocationNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("{code}")
    public ResponseEntity<?> deleteLocation(@PathVariable String code) {
        try {
            locationService.delete(code);
            return ResponseEntity.noContent().build();
        } catch (LocationNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }

    }

}
