package com.baopen753.weatherapiproject.realtimeservices.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder



// this class is fetched data from Model by calling setters method
public class RealtimeWeatherDto {

    @JsonProperty("location")
    private String location;       // the value of String 'location' is fetched by property with same name 'location'
                                   //                                           if fetched 'location' is primitive properties   --> fetching data is its value
                                   //                                           if fetched 'location' is an object              --> fetching data to its toString()
                                   //  in this case: toString() value of Location object of Model is fetched into String location of DTO

    @JsonProperty("temperature")
    private Integer temperature;

    @JsonProperty("humidity")
    private Integer humidity;

    @JsonProperty("precipitation")
    private Integer precipitation;

    @JsonProperty("wind_speed")
    private Integer windSpeed;

    @JsonProperty("status")
    private String status;

    @JsonProperty("last_updated")
    private Date lastUpdated;


}



