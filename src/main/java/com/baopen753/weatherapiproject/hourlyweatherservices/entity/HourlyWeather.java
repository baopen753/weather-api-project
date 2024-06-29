package com.baopen753.weatherapiproject.hourlyweatherservices.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
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

    public HourlyWeather getShallowCopy() {
        HourlyWeather copy = new HourlyWeather();
        copy.setHourlyWeatherId(this.getHourlyWeatherId());
        return copy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hourlyWeatherId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        HourlyWeather other = (HourlyWeather) obj;
        return Objects.equals(hourlyWeatherId, other.hourlyWeatherId);
    }
}
