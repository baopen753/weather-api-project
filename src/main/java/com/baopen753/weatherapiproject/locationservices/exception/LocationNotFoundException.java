package com.baopen753.weatherapiproject.locationservices.exception;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(String locationCode) {
        super("Location not found with code " + locationCode);
    }


    public LocationNotFoundException(String countryCode, String cityName) {
        super("Location not found for " + cityName+ ", " + countryCode);
    }
}
