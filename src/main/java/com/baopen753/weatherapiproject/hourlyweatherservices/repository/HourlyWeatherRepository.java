package com.baopen753.weatherapiproject.hourlyweatherservices.repository;

import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeather;
import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeatherId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HourlyWeatherRepository extends JpaRepository<HourlyWeather, HourlyWeatherId> {

    // should return weather at least at future time
    @Query("SELECT l FROM HourlyWeather l WHERE l.hourlyWeatherId.location.countryCode = ?1 AND l.hourlyWeatherId.hourOfDay > ?2")
    public List<HourlyWeather> findHourlyWeathersByCountryCode(String locationCode, Integer currentHour);

    @Query("SELECT l FROM HourlyWeather l WHERE l.hourlyWeatherId.location.countryCode = ?1 AND l.hourlyWeatherId.location.cityName = ?2 AND l.hourlyWeatherId.hourOfDay > ?3")
    public List<HourlyWeather> findHourlyWeathersByCountryCodeAndCity(String locationCode, String city,Integer currentHour);

}
