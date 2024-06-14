package com.baopen753.weatherapiproject.realtimeservices.entity;

import com.baopen753.weatherapiproject.locationservices.entity.Location;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder


@Entity
@Table(name = "realtime_weather")
public class RealtimeWeather {

    @Id
    @Column(name = "location_code", length = 50)
    @JsonProperty("location_code")
    private String locationCode;

    @Column(name = "temperature")
    @JsonProperty("temperature")
    private Integer temperature;

    @Column(name = "humidity")
    @JsonProperty("humidity")
    private Integer humidity;

    @Column(name = "precipitation")
    @JsonProperty("precipitation")
    private Integer precipitation;

    @Column(name = "wind_speed")
    @JsonProperty("wind_speed")
    private Integer windSpeed;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "last_updated")
    @JsonProperty("last_updated")
    private Date lastUpdated;

    @OneToOne()
    @JoinColumn(name = "location_code")
    @MapsId
    private Location location;
}
