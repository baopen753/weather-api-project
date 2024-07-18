package com.baopen753.weatherapiproject.locationservices.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor

@JsonPropertyOrder({"code", "city_name", "region_name", "country_name", "country_code"})
public class LocationDto {

    @NotBlank(message = "location code cannot be blank")
    @Length(min = 4, max = 12, message = "location code should be 4-12 in length")
    private String code;

    @NotBlank(message = "city_name cannot be blank")
    private String cityName;

    @NotNull(message = "region_name cannot be null")
    private String regionName;

    @NotBlank(message = "country_code cannot be blank")
    private String countryCode;

    @NotBlank(message = "country_name cannot be blank")
    private String countryName;

    @JsonIgnore
    private boolean enabled;

    @JsonIgnore
    private boolean trashed;


}
