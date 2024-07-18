package com.baopen753.weatherapiproject.hourlyweatherservices.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder

public class HourlyWeatherListDto extends RepresentationModel<HourlyWeatherListDto> {

    private String location;

    @JsonProperty("hourly_forecast")
    private List<HourlyWeatherDto> hourlyWeatherDtoList = new ArrayList<>();

    public void addHourlyWeatherDto(HourlyWeatherDto hourlyWeatherDto) {
        this.hourlyWeatherDtoList.add(hourlyWeatherDto);
    }
}
