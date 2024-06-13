package com.baopen753.weatherapiproject.locationservices.exception;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(String message) {
        super(message);
    }


    public LocationNotFoundException(String countryCode, String cityName) {
        super("Location not found for " + countryCode + ", " + cityName);
    }
}
