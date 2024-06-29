package com.baopen753.weatherapiproject.location;


import com.baopen753.weatherapiproject.GeolocationException;
import com.baopen753.weatherapiproject.GeolocationService;
import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * This class performs the lookup of IP2Location data from an IP address by reading a BIN file.
 *
 * @author baopen
 */

     // @RunWith refers from JUnit4
@SpringJUnitConfig(GeolocationService.class)   // define a test context, and tell Spring that can use only GeolocationService bean within this scope
public class IP2LocationTests {

    private final String DbPath = "ip2location/ip2location-lite-db3.bin/IP2LOCATION-LITE-DB3.BIN";

    @Autowired
    private GeolocationService geolocationService;

    @Test
    public void testInvalidIP() throws IOException {

        // initialize ip database
        IP2Location ip2Locator = new IP2Location();
        ip2Locator.Open(DbPath);

        String invalidIp = "abc";

        // query a desired ip address for location result
        IPResult result = ip2Locator.IPQuery(invalidIp);
        Assertions.assertThat(result.getStatus()).isEqualTo("INVALID_IP_ADDRESS");
    }

    @Test
    public void testValidIp() throws IOException {
        String validIp = "1.1.1.1";   // private ip

        IP2Location ip2Locator = new IP2Location();
        ip2Locator.Open(DbPath);

        IPResult result = ip2Locator.IPQuery(validIp);
        Assertions.assertThat(result.getStatus()).isEqualTo("OK");

        System.out.println(result.toString());
    }


    @Test
    public void getLocationFromGeoService()
    {
        String validIp = "1.1.1.1";
        Location locationMappedByIp = geolocationService.getLocation(validIp);

        Assertions.assertThat(locationMappedByIp).isNotNull();
    }


}
