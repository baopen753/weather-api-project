package com.baopen753.weatherapiproject.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BaseEntity {
    private String locationByIpUrl;

    private String locationByCodeUrl;

    private String realtimeWeatherByIpUrl;

    private String realtimeWeatherByCodeUrl;

    private String hourlyWeatherByIpUrl;

    private String hourlyWeatherByCodeUrl;

    private String dailyWeatherByIpUrl;

    private String dailyWeatherByCodeUrl;

    private String fullyWeatherByIpUrl;

    private String fullyWeatherByCodeUrl;
}
