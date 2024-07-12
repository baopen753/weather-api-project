package com.baopen753.weatherapiproject.hourlyweatherservices.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class HourlyWeatherDto {

    @JsonProperty("hour_of_day")
    @Range(min = 0, max = 23, message = "Range of hour should be in 0-23")
    private Integer hourOfDay;

    @JsonProperty("temperature")
    @Range(min = -50, max = 50, message = "Range of temperature should be in [-50,50]")
    private Integer temperature;

    @JsonProperty("precipitation")
    @Range(min = 0, max = 100, message = "Range of precipitation should be in 0-100")
    private Integer precipitation;

    @JsonProperty("status")
    @NotBlank(message = "status cannot be a blank")
    @Length(min = 3, max = 50, message = "Message length should be in 3-50 characters")
    private String status;

}
