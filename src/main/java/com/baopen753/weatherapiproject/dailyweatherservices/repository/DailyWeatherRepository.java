package com.baopen753.weatherapiproject.dailyweatherservices.repository;

import com.baopen753.weatherapiproject.dailyweatherservices.entity.DailyWeather;
import com.baopen753.weatherapiproject.dailyweatherservices.entity.DailyWeatherId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DailyWeatherRepository extends JpaRepository<DailyWeather, DailyWeatherId> {

    @Query("""
            select dw from DailyWeather dw
            where dw.dailyWeatherId.location.code = ?1
            and (dw.dailyWeatherId.month, dw.dailyWeatherId.dayOfMonth) > (MONTH(CURRENT_DATE), DAY(CURRENT_DATE))
            OR (dw.dailyWeatherId.month, dw.dailyWeatherId.dayOfMonth) = (MONTH(CURRENT_DATE), DAY(CURRENT_DATE))
            """)
    public List<DailyWeather> findDailyWeathersByLocationCode(String locationCode);



}
