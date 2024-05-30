package com.baopen753.weatherapiproject.locationservices.restcontroller;

import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.error.global.ErrorDTO;
import com.baopen753.weatherapiproject.locationservices.exception.LocationExistedException;
import com.baopen753.weatherapiproject.locationservices.exception.LocationNotFoundException;
import com.baopen753.weatherapiproject.locationservices.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
            return new ResponseEntity<>(HttpStatusCode.valueOf(404));
        }
    }


    @PostMapping
    public ResponseEntity<?> addLocation(@Valid @RequestBody Location location) {   // BindingResult is used to collect validation errors
                                                                                    // no need to add BindingResult in here, leeds to be incompatible Error Response Customizing

        try {
            Location createdLocation = locationService.create(location);
            URI uri = URI.create("/api/v1/locations/" + location.getCode());
            return ResponseEntity.created(uri).body(createdLocation);
        } catch (LocationExistedException ex) {
            ErrorDTO error = new ErrorDTO("Location already existed");
            return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(error);
        }
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
