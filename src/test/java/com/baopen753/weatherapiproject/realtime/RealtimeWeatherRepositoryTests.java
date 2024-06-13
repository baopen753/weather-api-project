package com.baopen753.weatherapiproject.realtime;

import com.baopen753.weatherapiproject.locationservices.repository.LocationRepository;
import com.baopen753.weatherapiproject.realtimeservices.entity.RealtimeWeather;
import com.baopen753.weatherapiproject.realtimeservices.repository.RealtimeWeatherRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = true)

public class RealtimeWeatherRepositoryTests {

    @Autowired
    private RealtimeWeatherRepository realtimeWeatherRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void testFindLocationByCodeAndCityNameSuccess() {

        String countryCode = "VN";
        String cityName = "Hanoi";

        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findLocationByCountryCodeAndCity(countryCode, cityName);
        // Location location = locationRepository.findLocationsByCode(countryCode);

        Assertions.assertThat(realtimeWeather.getLocation().getRegionName()).isEqualToIgnoringCase("Myduc");
        Assertions.assertThat(realtimeWeather).isNotNull();

    }

    @Test
    public void testFindLocationByCodeNotFound() {

        String countryCode = "CA";
        String cityName = "Hanoi";

        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findLocationByCountryCodeAndCity(countryCode, cityName);
        // Location location = locationRepository.findLocationsByCode(countryCode);

        Assertions.assertThat(realtimeWeather).isNull();

    }

}
