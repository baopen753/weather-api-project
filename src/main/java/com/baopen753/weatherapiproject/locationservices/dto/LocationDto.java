package com.baopen753.weatherapiproject.locationservices.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@JsonPropertyOrder({"code","city_name","region_name","country_code","country_name","enabled"})
public class LocationDto {

   // @NotNull(message = "Location code cannot be null")
    private String code;


    private String cityName;

    private String regionName;

    private String countryCode;

    private String countryName;

    private boolean enabled;



}
