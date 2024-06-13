package com.baopen753.weatherapiproject.realtimeservices.repository;

import com.baopen753.weatherapiproject.realtimeservices.entity.RealtimeWeather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RealtimeWeatherRepository extends JpaRepository<RealtimeWeather, String> {

    @Query("SELECT l FROM RealtimeWeather l WHERE l.locationCode = ?1")
    public RealtimeWeather findByLocationCode(String locationCode);

    @Query("SELECT l FROM RealtimeWeather l WHERE l.location.countryCode = ?1 AND l.location.cityName = ?2")
    public RealtimeWeather findLocationByCountryCodeAndCity(String countryCode, String city);


}

