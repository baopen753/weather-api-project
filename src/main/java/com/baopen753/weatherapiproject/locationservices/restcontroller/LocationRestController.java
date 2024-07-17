package com.baopen753.weatherapiproject.locationservices.restcontroller;

/*Taking note:
 *    1. This RestController class should be directly called Service method . No need to catch Exception due to the existing of GlobalHandleException
 */

import com.baopen753.weatherapiproject.locationservices.dto.LocationDto;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.mapper.LocationMapper;
import com.baopen753.weatherapiproject.locationservices.service.LocationService;

import jakarta.validation.Valid;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
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

    public LocationRestController(LocationService service ) {
        this.service = service;
    }



    @GetMapping
    public ResponseEntity<?> getLocations(@RequestParam("pageSize") @Min(value = 4, message = "Minimum of page size is 4") @Max(value = 50, message = "Maximun of page size is 50") Integer pageSize, @RequestParam("pageNum") @Positive(message = "Page number must be greater than 0") Integer pageNum) {

        List<Location> locationList = service.findAllLocations();
        List<LocationDto> dtoList = locationList.stream().map(location -> LocationMapper.INSTANCE.entityToDto(location)).toList();

        if (dtoList.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> findLocationByCode(@PathVariable("code") String code) {
        Location location = service.findLocationByCode(code);
        LocationDto dto = LocationMapper.INSTANCE.entityToDto(location);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<?> addLocation(@Valid @RequestBody LocationDto locationDto) {
        Location locationFromRequest = LocationMapper.INSTANCE.dtoToEntity(locationDto);
        Location createdLocation = service.create(locationFromRequest);
        LocationDto dto = LocationMapper.INSTANCE.entityToDto(createdLocation);
        URI uri = URI.create("/api/v1/locations/" + locationFromRequest.getCode());
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping
    public ResponseEntity<?> updateLocation(@Valid @RequestBody LocationDto locationDto) {
        Location locationFromRequest = LocationMapper.INSTANCE.dtoToEntity(locationDto);
        Location updatedLocation = service.update(locationFromRequest);
        LocationDto dto = LocationMapper.INSTANCE.entityToDto(updatedLocation);
        return ResponseEntity.ok(dto);
    }


    @DeleteMapping("{code}")
    public ResponseEntity<?> deleteLocation(@PathVariable String code) {
        service.delete(code);
        return ResponseEntity.noContent().build();
    }

}
