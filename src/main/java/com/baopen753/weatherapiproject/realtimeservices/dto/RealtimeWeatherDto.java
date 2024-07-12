package com.baopen753.weatherapiproject.realtimeservices.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder


// this class is fetched data from Model by calling setters method
public class RealtimeWeatherDto {

    @JsonProperty("location")
    @JsonInclude(JsonInclude.Include.NON_NULL)     // hide location property in case of containing 'null'
    private String locationAddress ;       // the value of String 'location' is fetched by property with same name 'location'
    //                                           if fetched 'location' is primitive properties   --> fetching data is its value
    //                                           if fetched 'location' is an object              --> fetching data to its toString()
    //  in this case: toString() value of Location object of Model is fetched into String location of DTO

    @JsonProperty("temperature")
    @Range(min = -30, max = 50, message = "Temperature should be in range [-30,50] in Celsius Degree")
    private Integer temperature;

    @JsonProperty("humidity")
    @Range(min = 0, max = 100, message = "Humidity should be in range [0,100] in percentage")
    private Integer humidity;

    @JsonProperty("precipitation")
    @Range(min = 0, max = 100, message = "Precipitation should be in range [0,100] in percentage")
    private Integer precipitation;

    @JsonProperty("wind_speed")
    private Integer windSpeed;

    @JsonProperty("status")
    @NotBlank(message = "Status must not be blank")
    private String status;

    @JsonProperty("last_updated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd 'T' HH:mm:ss 'Z'")
    private Date lastUpdated;

}



