package com.baopen753.weatherapiproject.locationservices.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "locations")
@NoArgsConstructor
@AllArgsConstructor
@Data  // generate getter & setter , toString, equals, hashCode
@Builder

public class Location {

    @Id
    @Column(length = 12,nullable = false, unique = true)           //  using @Column(name = "code") for database first
    @JsonProperty("code")                                          //  Validation should be implemented within the service layer instead of persistence layer
    private String code;
                                                                   // JsonProperty: make sure there must have corresponding JSON field in RequestBody
    @Column(length = 128, nullable = false)
    @JsonProperty("city_name")
    private String cityName;

    @Column(length = 128, nullable = false)
    @JsonProperty("country_name")
    private String countryName;

    @Column(length = 128, nullable = true)
    @JsonProperty("region_name")
    private String regionName;

    @Column(length = 6,nullable = false)
    @JsonProperty("country_code")
    private String countryCode;

    @JsonIgnore
    private boolean enabled;

    @JsonIgnore
    private boolean trashed;

}
