package com.baopen753.weatherapiproject.locationservices.entity;


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
    private String code;                                           //  Validation should be implemented within the service layer instead of persistence layer

    @Column(length = 128, nullable = false)
    private String cityName;

    @Column(length = 128, nullable = false)
    private String countryName;

    @Column(length = 128, nullable = true)
    private String regionName;

    @Column(length = 6,nullable = false)
    private String countryCode;

    private boolean enabled;

    private boolean trashed;

}
