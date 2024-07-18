package com.baopen753.weatherapiproject.fullyweatherservices.dto;

import com.baopen753.weatherapiproject.dailyweatherservices.dto.DailyWeatherDto;
import com.baopen753.weatherapiproject.hourlyweatherservices.dto.HourlyWeatherDto;
import com.baopen753.weatherapiproject.realtimeservices.dto.RealtimeWeatherDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class FullyWeatherDto {

    private String location;

    @Valid
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = RealtimeFilterFields.class)
    private RealtimeWeatherDto realtimeWeather = new RealtimeWeatherDto();

    @JsonProperty("daily_weather")
    @Valid
    private List<DailyWeatherDto> dailyWeatherList = new ArrayList<>();

    @JsonProperty("hourly_weather")
    @Valid
    private List<HourlyWeatherDto> hourlyWeatherList = new ArrayList<>();

}

