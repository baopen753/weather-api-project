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

import java.util.Date;

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

        Assertions.assertThat(realtimeWeather.getLocation().getRegionName()).isEqualToIgnoringCase("North");
        Assertions.assertThat(realtimeWeather).isNotNull();
    }

    @Test
    public void testFindLocationByCountryCodeAndCityFound() {

        String countryCode = "CA";
        String cityName = "Hanoi";

        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findLocationByCountryCodeAndCity(countryCode, cityName);
        // Location location = locationRepository.findLocationsByCode(countryCode);

        Assertions.assertThat(realtimeWeather).isNull();

    }

    @Test
    public void testFindLocationByCodeFound() {
        String code = "VN_HN";

        RealtimeWeather result = realtimeWeatherRepository.findByLocationCode(code);

        System.out.println(result.toString());
        Assertions.assertThat(result).isNotNull();

    }

    @Test
    public void testFindLocationByCodeNotFound() {
        String code = "Wrong_Code";
        RealtimeWeather result = realtimeWeatherRepository.findByLocationCode(code);

        Assertions.assertThat(result).isNull();
    }

    @Test
    public void testUpdateRealtimeWeatherByCodeSuccess() {

        // creating an existing RealtimeWeather
        RealtimeWeather testExistingRealtimeWeather = new RealtimeWeather();
        testExistingRealtimeWeather.setHumidity(61);
        testExistingRealtimeWeather.setPrecipitation(62);
        testExistingRealtimeWeather.setTemperature(63);
        testExistingRealtimeWeather.setWindSpeed(64);
        testExistingRealtimeWeather.setLastUpdated(new Date());
        testExistingRealtimeWeather.setLocationCode("USA_LA");
        testExistingRealtimeWeather.setStatus("Cloudy");

        RealtimeWeather result = realtimeWeatherRepository.save(testExistingRealtimeWeather);
        Assertions.assertThat(result.getHumidity()).isEqualTo(61);
        Assertions.assertThat(result.getStatus()).isEqualTo("Cloudy");
    }




}
