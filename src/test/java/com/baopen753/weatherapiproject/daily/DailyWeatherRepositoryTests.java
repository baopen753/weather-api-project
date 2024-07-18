package com.baopen753.weatherapiproject.daily;


import com.baopen753.weatherapiproject.dailyweatherservices.entity.DailyWeather;
import com.baopen753.weatherapiproject.dailyweatherservices.entity.DailyWeatherId;
import com.baopen753.weatherapiproject.dailyweatherservices.repository.DailyWeatherRepository;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.baopen753.weatherapiproject.locationservices.repository.LocationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)

public class DailyWeatherRepositoryTests {

    @Autowired
    private DailyWeatherRepository dailyWeatherRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void testGetDailyWeathers() {
        String locationCode = "VN_HCM";

        List<DailyWeather> dailyWeatherList = dailyWeatherRepository.findDailyWeathersByLocationCode(locationCode);

        Assertions.assertThat(dailyWeatherList).isNotNull();
        System.out.println("Print all");
        dailyWeatherList.forEach(System.out::println);
    }

    @Test
    public void testUpdateDailyWeathers() {
        String locationCode = "VN_HCM";

        Location locationMappedFromIp = new Location();
        locationMappedFromIp.setCode(locationCode);
        locationMappedFromIp.setRegionName("Middle");
        locationMappedFromIp.setCountryCode("VN");
        locationMappedFromIp.setCityName("Ho Chi Minh City");
        locationMappedFromIp.setCountryName("The Socialist Republic of Vietnam");

        DailyWeatherId id1 = DailyWeatherId.builder()
                .month(12)
                .dayOfMonth(1)
                .location(locationMappedFromIp)
                .build();

        DailyWeatherId id2 = DailyWeatherId.builder()
                .month(12)
                .dayOfMonth(2)
                .location(locationMappedFromIp)
                .build();

        DailyWeather dailyWeather1 = DailyWeather.builder()
                .dailyWeatherId(id1)
                .precipitation(99)
                .maxTemperature(99)
                .minTemperature(99)
                .status("Cold")
                .build();

        DailyWeather dailyWeather2 = DailyWeather.builder()
                .dailyWeatherId(id2)
                .precipitation(88)
                .maxTemperature(88)
                .minTemperature(88)
                .status("Colder")
                .build();

        List<DailyWeather> dailyWeatherList = List.of(dailyWeather1, dailyWeather2);

        List<DailyWeather> weatherListUpdated = dailyWeatherRepository.saveAll(dailyWeatherList);
        Assertions.assertThat(weatherListUpdated).isNotNull();
        Assertions.assertThat(weatherListUpdated.size()).isEqualTo(2);

        weatherListUpdated.forEach(System.out::println);

    }



}
