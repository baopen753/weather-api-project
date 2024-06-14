package com.baopen753.weatherapiproject.location;


import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * This class performs the lookup of IP2Location data from an IP address by reading a BIN file.
 *
 * @author baopen
 */

public class IP2LocationTests {

    private final String DbPath = "ip2location/ip2location-lite-db3.bin/IP2LOCATION-LITE-DB3.BIN";

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
        String validIp = "171.252.153.255";   // private ip

        IP2Location ip2Locator = new IP2Location();
        ip2Locator.Open(DbPath);

        IPResult result = ip2Locator.IPQuery(validIp);
        Assertions.assertThat(result.getStatus()).isEqualTo("OK");
     //   Assertions.assertThat(result.getCity()).isEqualTo("Ho Chi Minh City");

        System.out.println(result.toString());
    }


}
