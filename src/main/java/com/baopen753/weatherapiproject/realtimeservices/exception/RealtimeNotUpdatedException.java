package com.baopen753.weatherapiproject.realtimeservices.exception;

public class RealtimeNotUpdatedException extends RuntimeException {
    public RealtimeNotUpdatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RealtimeNotUpdatedException(String locationCode) {
        super("Weather in location: " + locationCode + " has not updated yet");
    }
}
