package com.baopen753.weatherapiproject.hourly;

import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeather;
import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeatherId;
import com.baopen753.weatherapiproject.hourlyweatherservices.repository.HourlyWeatherRepository;
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
public class HourlyWeatherRepositoryTests {

    @Autowired
    private HourlyWeatherRepository hourlyWeatherRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void testAddHourlyWeatherSuccess() {
        String locationCode = "VN_HCM";   // an existing location code
        Location location = locationRepository.findLocationsByCode(locationCode);

        int hourOfDay = 12;

        HourlyWeatherId id = new HourlyWeatherId(hourOfDay, location);
        HourlyWeather hourlyWeatherTest = new HourlyWeather();
        hourlyWeatherTest.setHourlyWeatherId(id);
        hourlyWeatherTest.setPrecipitation(80);
        hourlyWeatherTest.setTemperature(80);
        hourlyWeatherTest.setStatus("Sunny");
        HourlyWeather newHourlyWeather = hourlyWeatherRepository.save(hourlyWeatherTest);

        Assertions.assertThat(newHourlyWeather).isNotNull();
    }


    @Test
    public void testGetHourlyWeatherFindByLocationCodeFound() {
        String locationCode = "VN_HCM";
        int hourOfDay = 12;

        List<HourlyWeather> hourlyWeatherList = hourlyWeatherRepository.findHourlyWeatherByLocationCode(locationCode, hourOfDay);

        Assertions.assertThat(hourlyWeatherList).isNotNull();
        System.out.println(hourlyWeatherList.size());
        hourlyWeatherList.stream().forEach(System.out::println);
    }

    @Test
    public void testGetHourlyWeatherFindByLocationCodeNotFound() {
        String locationCode = "VN_HIHI";
        int hourOfDay = 12;

        List<HourlyWeather> hourlyWeatherList = hourlyWeatherRepository.findHourlyWeatherByLocationCode(locationCode, hourOfDay);

        Assertions.assertThat(hourlyWeatherList).isEmpty();
        System.out.println(hourlyWeatherList.size());
        hourlyWeatherList.stream().forEach(System.out::println);
    }

}
