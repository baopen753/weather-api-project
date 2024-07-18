package com.baopen753.weatherapiproject.fullyweatherservices.dto;

import com.baopen753.weatherapiproject.realtimeservices.dto.RealtimeWeatherDto;

public class RealtimeFilterFields {
    public boolean equals(Object obj) {
         if (obj instanceof RealtimeWeatherDto) {
             RealtimeWeatherDto dto = (RealtimeWeatherDto) obj;
             return dto.getStatus() == null;
         }
         return false;
    }
}
