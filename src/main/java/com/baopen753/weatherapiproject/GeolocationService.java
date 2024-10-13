package com.baopen753.weatherapiproject;

import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;


/*
 *  This class provides database contains locations mapped compatible public IP address
 *
 *  Aims: Get location based on input Ip address
 */

@Service
public class GeolocationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeolocationService.class);
    private static final IP2Location ip2Locator = new IP2Location();
    private final String DB_PATH = "/ip2location/IP2LOCATION-LITE-DB3.BIN";


    /*
     *  This constructor function plays a role to initialize Ip2Location database
     */
    public GeolocationService() {
        try {
            InputStream inputStream = getClass().getResourceAsStream(DB_PATH);
            byte[] data = inputStream.readAllBytes();
            ip2Locator.Open(data);
            inputStream.close();
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    /*  String validIp = "171.252.153.255";   // private ip
        IP2Location ip2Locator = new IP2Location();
        ip2Locator.Open(DbPath);
     */


    /*
     *   This function is used to query Location based on input IP address
     *
     *   @param:  ipAddress: IP Address you wish to query
     *   @return: Location: desired specific location
     *   @throws: If an invalid ipAddress input(GeolocationException) or error DbPath (IOException)
     */
    public Location getLocation(String ipAddress) throws GeolocationException {
        try {
            IPResult result = ip2Locator.IPQuery(ipAddress);
            if (!result.getStatus().equals("OK")) throw new GeolocationException(ipAddress);
            LOGGER.info(result.toString());
            return new Location(result.getCity(), result.getRegion(), result.getCountryLong(), result.getCountryShort());
        } catch (IOException ex) {
            throw new GeolocationException("Error IP querying: ", ex);
        }
    }
}
