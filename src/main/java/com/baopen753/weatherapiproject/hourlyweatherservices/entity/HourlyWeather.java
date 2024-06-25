package com.baopen753.weatherapiproject.hourlyweatherservices.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder


@Entity
@Table(name = "hourly_weather")
public class HourlyWeather {

    @EmbeddedId
    private HourlyWeatherId hourlyWeatherId = new HourlyWeatherId();

    @JsonProperty("temperature")
    private Integer temperature;

    @JsonProperty("precipitation")
    private Integer precipitation;

    @Column(name = "status", length = 50)
    private String status;

}
