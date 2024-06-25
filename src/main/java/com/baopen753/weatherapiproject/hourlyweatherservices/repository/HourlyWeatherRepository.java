package com.baopen753.weatherapiproject.hourlyweatherservices.repository;

import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeather;
import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeatherId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HourlyWeatherRepository extends JpaRepository<HourlyWeather, HourlyWeatherId> {

    @Query("SELECT l FROM HourlyWeather l WHERE l.hourlyWeatherId.location.code = ?1 AND l.hourlyWeatherId.hourOfDay > ?2")
    public List<HourlyWeather> findHourlyWeatherByLocationCode(String locationCode, Integer currentHour);

}
