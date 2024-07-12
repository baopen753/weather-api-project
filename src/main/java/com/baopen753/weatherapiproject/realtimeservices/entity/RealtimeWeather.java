package com.baopen753.weatherapiproject.realtimeservices.entity;

import com.baopen753.weatherapiproject.locationservices.entity.Location;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@Builder
@Data

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

    @Column(name = "wind_speed")
    private Integer windSpeed;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "last_updated")
    private Date lastUpdated;

    @OneToOne
    @JoinColumn(name = "location_code")
    @MapsId
    private Location location;

    public RealtimeWeather() {
        this.temperature = 0;
        this.humidity = 0;
        this.precipitation = 0;
        this.windSpeed = 0;
        this.status = null;
        this.lastUpdated = null;
    }
}
