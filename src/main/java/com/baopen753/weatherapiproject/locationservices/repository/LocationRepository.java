package com.baopen753.weatherapiproject.locationservices.repository;

import com.baopen753.weatherapiproject.locationservices.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, String> {

    @Query("SELECT l FROM Location l WHERE l.trashed = false AND l.code = ?1")
    public Location findByCode(String code);

    @Query("SELECT l FROM Location l WHERE l.regionName ilike %?1% AND l.countryCode ilike %?2%")
    public List<Location> findByCountryCodeAndRegionName(String regionName , String countryCode);



}
