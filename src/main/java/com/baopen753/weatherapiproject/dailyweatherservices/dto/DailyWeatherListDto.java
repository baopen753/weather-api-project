package com.baopen753.weatherapiproject.dailyweatherservices.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

public class DailyWeatherListDto {

    private String location;

    @JsonProperty("daily_forecast")
    private List<DailyWeatherDto> dailyWeatherDtoList = new ArrayList<>();

    public void addDailyWeatherDto(DailyWeatherDto dailyWeatherDto) {
        this.dailyWeatherDtoList.add(dailyWeatherDto);
    }

}
