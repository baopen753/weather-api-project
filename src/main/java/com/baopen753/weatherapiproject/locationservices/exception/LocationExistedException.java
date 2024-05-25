package com.baopen753.weatherapiproject.locationservices.exception;

public class LocationExistedException extends RuntimeException{
    public LocationExistedException(String message) {
        super(message);
    }

    public LocationExistedException(String message, Throwable cause) {
        super(message, cause);
    }
}
