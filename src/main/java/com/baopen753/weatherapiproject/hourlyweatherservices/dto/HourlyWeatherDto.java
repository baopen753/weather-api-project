package com.baopen753.weatherapiproject.hourlyweatherservices.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data

@JsonPropertyOrder({"hour_of_day", "temperature", "precipitation", "status"})
public class HourlyWeatherDto {

    @JsonProperty("hour_of_day")
    private Integer hourOfDay;

    @JsonProperty("temperature")
    private Integer temperature;

    @JsonProperty("precipitation")
    private Integer precipitation;

    @JsonProperty("status")
    private String status;

}
