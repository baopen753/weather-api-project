package com.baopen753.weatherapiproject.realtimeservices.entity;

import com.baopen753.weatherapiproject.locationservices.entity.Location;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder


@Entity
@Table(name = "realtime_weather")
public class RealtimeWeather {

    @Id
    @Column(name = "location_code", length = 50)
    private String locationCode;

    @Column(name = "temperature")
    private Integer temperature;

    @Column(name = "humidity")
    private Integer humidity;

    @Column(name = "precipitation")
    private Integer precipitation;

    @Column(name = "windSpeed")
    private Integer windSpeed;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "lastUpdated")
    private Date lastUpdated;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_code")
    @MapsId
    private Location location;
}
