package com.baopen753.weatherapiproject.locationservices.entity;

/*Taking note:
 *    1. + This Location class is still in developing.   (30/5/2024)
 *       + Location entity class should not contain @Validation annotation which actually belongs to LocationDTO
 *       + Location entity and LocationDTO can be made difference by where it located. Location entity presents
 *  Location table in database and should be in consistency. Whereas LocationDTO contains a whole or a part of properties of Location entity,
 * then DTO can be brought to display / interact with upper layer.
 *       + All annotations stem from jakarta.validation libraries should be
 * */

import com.baopen753.weatherapiproject.realtimeservices.entity.RealtimeWeather;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


@Entity
@Table(name = "locations")
@NoArgsConstructor
@AllArgsConstructor
@Data  // generate getter & setter , toString, equals, hashCode
@Builder


public class Location {

    @Id
    @Column(name = "location_code", length = 12, nullable = false, unique = true)           //  using @Column(name = "code") for database first
    @JsonProperty("location_code")
    //  Validation should be implemented within the service layer instead of persistence layer
    @NotBlank(message = "location code cannot be blank")
    // JsonProperty: make sure there must have corresponding JSON field in RequestBody
    @Length(min = 4, max = 12, message = "location code should be 4-12 in length")
    private String code;

    @Column(length = 128, nullable = false)
    @JsonProperty("city_name")
    @NotBlank(message = "city_name cannot be blank")
    private String cityName;

    @Column(length = 128, nullable = false)
    @JsonProperty("country_name")
    @NotBlank(message = "country_name cannot be blank")
    private String countryName;

    @Column(length = 128, nullable = true)
    @JsonProperty("region_name")
    @NotNull(message = "region_name cannot be null")
    private String regionName;

    @Column(length = 6, nullable = false)
    @JsonProperty("country_code")
    @NotBlank(message = "country_code cannot be blank")
    private String countryCode;

    @JsonIgnore
    private boolean enabled;

    @JsonIgnore
    private boolean trashed;

    public Location(String code, String cityName, String countryName, String regionName, String countryCode, boolean enabled, boolean trashed) {
        this.code = code;
        this.cityName = cityName;
        this.countryName = countryName;
        this.regionName = regionName;
        this.countryCode = countryCode;
        this.enabled = enabled;
        this.trashed = trashed;
    }

    @OneToOne(mappedBy = "location")
    @PrimaryKeyJoinColumn  // optional
    private RealtimeWeather realtimeWeather;

}

