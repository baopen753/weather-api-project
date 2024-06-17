package com.baopen753.weatherapiproject.locationservices.restcontroller;

/*Taking note:
 *    1. This RestController class should be directly called Service method . No need to catch Exception due to the existing of GlobalHandleException
 */

import com.baopen753.weatherapiproject.locationservices.dto.LocationDto;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.service.LocationService;

import jakarta.validation.Valid;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/v1/locations")
@Validated
public class LocationRestController {

    private LocationService service;
    private ModelMapper mapper;


    public LocationRestController(LocationService service, ModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<?> getLocations(@RequestParam("pageSize") @Min(value = 10, message = "Minimum of page size is 10") @Max(value = 50, message = "Maximun of page size is 50") Integer pageSize, @RequestParam("pageNum") @Positive(message = "Page number must be greater than 0") Integer pageNum) {

        List<Location> locationList = service.findAllLocations();
        List<LocationDto> dtoList = locationList.stream().map(location -> mapper.map(location, LocationDto.class)).toList();
        if (dtoList.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> findLocationByCode(@PathVariable("code") String code) {
        Location location = service.findLocationByCode(code);
        LocationDto dto = mapper.map(location, LocationDto.class);
        return ResponseEntity.ok(dto);
    }


    @PostMapping
    public ResponseEntity<?> addLocation(@Valid @RequestBody Location location) {
        Location createdLocation = service.create(location);
        LocationDto dto = mapper.map(createdLocation, LocationDto.class);
        URI uri = URI.create("/api/v1/locations/" + location.getCode());
        return ResponseEntity.created(uri).body(dto);
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
