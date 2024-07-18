package com.baopen753.weatherapiproject.hourlyweatherservices.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class HourlyWeatherListDto {

    private String location;

    @JsonProperty("hourly_forecast")
    private List<HourlyWeatherDto> hourlyWeatherDtoList = new ArrayList<>();

    public void addHourlyWeatherDto(HourlyWeatherDto hourlyWeatherDto) {
        this.hourlyWeatherDtoList.add(hourlyWeatherDto);
    }
}
