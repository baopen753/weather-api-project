package com.baopen753.weatherapiproject.dailyweatherservices.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder

@Entity
@Table(name = "daily_weather")
public class DailyWeather {

    @EmbeddedId
    DailyWeatherId dailyWeatherId = new DailyWeatherId();

    @Column(name = "min_temperature", nullable = false)
    private Integer minTemperature;

    @Column(name = "max_temperature", nullable = false)
    private Integer maxTemperature;

    @Column(name = "precipitation", nullable = false)
    private Integer precipitation;

    @Column(name = "status",length = 50, nullable = false)
    private String status;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyWeather that = (DailyWeather) o;
        return Objects.equals(dailyWeatherId, that.dailyWeatherId) && Objects.equals(minTemperature, that.minTemperature) && Objects.equals(maxTemperature, that.maxTemperature) && Objects.equals(precipitation, that.precipitation) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dailyWeatherId, minTemperature, maxTemperature, precipitation, status);
    }

    @Override
    public String toString() {
        return "DailyWeather{" +
                "dailyWeatherId=" + dailyWeatherId +
                ", minTemperature=" + minTemperature +
                ", maxTemperature=" + maxTemperature +
                ", precipitation=" + precipitation +
                ", status='" + status + '\'' +
                '}';
    }
}
