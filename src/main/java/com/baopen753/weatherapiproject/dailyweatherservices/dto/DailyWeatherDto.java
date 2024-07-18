package com.baopen753.weatherapiproject.dailyweatherservices.dto;

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

public class DailyWeatherDto {

    @Range(min = 1, max = 31, message = "Range of hour should be [1,31]")
    private Integer dayOfMonth;

    @Range(min = 1, max = 12, message = "Range of month should be [1,12]")
    private Integer month;

    @Range(min = -50, max = 49, message = "Min temperature should be [-50,49]")
    private Integer minTemperature;

    @Range(min = -49, max = 50, message = "Max temperature should be [-49,50]")
    private Integer maxTemperature;

    @Range(min = 0, max = 100, message = "Temperature should be [0,100]")
    private Integer precipitation;

    @NotBlank(message = "Status cannot be blank")
    @Length(min = 3, max = 50, message = "Message should be in 3-50 characters")
    private String status;

}
