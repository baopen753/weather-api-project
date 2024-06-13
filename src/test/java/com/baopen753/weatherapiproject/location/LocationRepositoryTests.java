package com.baopen753.weatherapiproject.location;

import com.baopen753.weatherapiproject.locationservices.entity.Location;
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
@Rollback(value = false)
public class LocationRepositoryTests {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private RealtimeWeatherRepository realtimeWeatherRepository;

//    @Test
//    public void testListSuccess() {
//        List<Location> locations = locationRepository.
//        Assertions.assertThat(locations).isNotEmpty();
//        locations.stream().forEach(System.out::println);
//    }

    @Test
    public void testAddSuccess() {
        Location location = new Location();
        location.setCode("VN_NT");
        location.setCityName("Nhatrang");
        location.setCountryCode("VN");
        location.setCountryName("Socialist Republic of Vietnam");
        location.setEnabled(true);
        location.setRegionName("Middle");

        Location addedlocation = locationRepository.save(location);
        Assertions.assertThat(addedlocation).isNotNull();
    }

    @Test
    public void testUpdateSuccess() {
        Location location = new Location();
        location.setCode("VN_HCM");
        location.setCityName("Camau");
        location.setCountryCode("VN");
        location.setCountryName("Socialist Republic of Vietnam");
        location.setEnabled(true);
        location.setRegionName("Middle");
        location.setTrashed(false);

        Location updatedLocation = locationRepository.save(location);
        Assertions.assertThat(updatedLocation.getCode()).isNotNull();
    }

    @Test
    public void testTrashedSuccess() {
        String code = "VN_HN";

        locationRepository.deleteByCode(code);
        Location location = locationRepository.findLocationsByCode(code);

        Assertions.assertThat(location).isNull();
    }

    @Test
    public void testAddRealtimeWeatherData() {
        String locaionCode = "VN_HN";
        Location locationAddedData = locationRepository.findLocationsByCode(locaionCode);

        RealtimeWeather testedRealtimeWeather = new RealtimeWeather();
        testedRealtimeWeather.setLocationCode(locaionCode);
        testedRealtimeWeather.setTemperature(26);
        testedRealtimeWeather.setHumidity(78);
        testedRealtimeWeather.setPrecipitation(30);
        testedRealtimeWeather.setWindSpeed(9);
        testedRealtimeWeather.setLastUpdated(new Date());
        testedRealtimeWeather.setLocation(locationAddedData);

        RealtimeWeather realtimeWeatherResult = realtimeWeatherRepository.save(testedRealtimeWeather);

        Assertions.assertThat(realtimeWeatherResult).isNotNull();

    }

    @Test
    public void testUpdateRealtimeWeatherData() {
        String locaionCode = "VN_HN";
        RealtimeWeather realtimeWeatherInDb = realtimeWeatherRepository.findByLocationCode(locaionCode);

        realtimeWeatherInDb.setTemperature(50);
        realtimeWeatherInDb.setHumidity(50);
        realtimeWeatherInDb.setPrecipitation(50);
        realtimeWeatherInDb.setWindSpeed(50);
        realtimeWeatherInDb.setLastUpdated(new Date());
        realtimeWeatherInDb.setStatus("1st Updated from " + realtimeWeatherInDb.getLocationCode());

        RealtimeWeather updatedRealtimeWeather = realtimeWeatherRepository.save(realtimeWeatherInDb);

        Assertions.assertThat(updatedRealtimeWeather.getHumidity()).isEqualTo(50);
    }
}
