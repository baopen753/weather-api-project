package com.baopen753.weatherapiproject.location;

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
public class LocationRepositoryTests {

    @Autowired
    private LocationRepository locationRepository;

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
    public void testTrashedSuccess()
    {
        String code = "VN_HN";

        locationRepository.deleteByCode(code);
        Location location = locationRepository.findLocationsByCode(code);

        Assertions.assertThat(location).isNull();
    }
}
