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
@Rollback
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

    @Test
    public void testUpdateHourlyWeatherByCodeSuccess() {
        String locationCode = "VN_HN";

        Location location = locationRepository.findLocationsByCode(locationCode);

        HourlyWeatherId id1 = new HourlyWeatherId(12, location);
        HourlyWeatherId id2 = new HourlyWeatherId(13, location);
        HourlyWeatherId id3 = new HourlyWeatherId(14, location);
        HourlyWeatherId id4 = new HourlyWeatherId(15, location);

        HourlyWeather hourlyWeather1 = HourlyWeather.builder()
                .hourlyWeatherId(id1)
                .precipitation(40)
                .temperature(40)
                .status("Cool")
                .build();

        HourlyWeather hourlyWeather2 = HourlyWeather.builder()
                .hourlyWeatherId(id2)
                .precipitation(30)
                .temperature(30)
                .status("Cooler")
                .build();

        HourlyWeather hourlyWeather3 = HourlyWeather.builder()
                .hourlyWeatherId(id3)
                .precipitation(20)
                .temperature(20)
                .status("Freeze")
                .build();

        HourlyWeather hourlyWeather4 = HourlyWeather.builder()
                .hourlyWeatherId(id4)
                .precipitation(10)
                .temperature(10)
                .status("Freezeze")
                .build();

        List<HourlyWeather> input = List.of(hourlyWeather1, hourlyWeather2, hourlyWeather3, hourlyWeather4);

        List<HourlyWeather> result = hourlyWeatherRepository.saveAllAndFlush(input);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(4);

        result.stream().forEach(System.out::println);

    }


}
