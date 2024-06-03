package com.baopen753.weatherapiproject.locationservices.restcontroller;

/*Taking note:
 *    1. This RestController class should be directly called Service method . No need to catch Exception due to the existing of GlobalHandleException
 * */


import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.exception.LocationExistedException;
import com.baopen753.weatherapiproject.locationservices.exception.LocationNotFoundException;
import com.baopen753.weatherapiproject.locationservices.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/v1/locations")
public class LocationRestController {


    private LocationService service;

    @Autowired
    public LocationRestController(LocationService locationService) {
        this.service = locationService;
    }


    @GetMapping
    public ResponseEntity<List<Location>> getLocations() {
        List<Location> locationList = service.findAllLocations();
        if (locationList.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(locationList);
    }


    @GetMapping("/{code}")
    public ResponseEntity<?> findLocationByCode(@PathVariable("code") String code) {
        Location location = service.findLocationByCode(code);
        return ResponseEntity.ok(location);
    }


    @PostMapping
    public ResponseEntity<?> addLocation(@Valid @RequestBody Location location) {
        Location createdLocation = service.create(location);
        URI uri = URI.create("/api/v1/locations/" + location.getCode());
        return ResponseEntity.created(uri).body(createdLocation);

    }


    @PutMapping
    public ResponseEntity<?> updateLocation(@Valid @RequestBody Location location) {
        Location updatedLocation = service.update(location);
        return ResponseEntity.ok(updatedLocation);
    }


    @DeleteMapping("{code}")
    public ResponseEntity<?> deleteLocation(@PathVariable String code) {
        service.delete(code);
        return ResponseEntity.noContent().build();
    }

}
