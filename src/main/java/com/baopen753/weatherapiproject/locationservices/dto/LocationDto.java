package com.baopen753.weatherapiproject.locationservices.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@JsonPropertyOrder({"code", "city_name", "region_name", "country_name", "country_code"})
public class LocationDto {

    @JsonProperty("code")
    private String code;

    @JsonProperty("city_name")
    private String cityName;

    @JsonProperty("region_name")
    private String regionName;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("country_name")
    private String countryName;



}
