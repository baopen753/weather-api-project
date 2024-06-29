package com.baopen753.weatherapiproject;

public class GeolocationException extends RuntimeException {
    public GeolocationException(String ipAddress) {
        super("Geolocation mapping failed with ip address: " + ipAddress);
    }

    public GeolocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
