package com.baopen753.weatherapiproject.locationservices.entity;

/*Taking note:
 *    1. + This Location class is still in developing.   (30/5/2024)
 *       + Location entity class should not contain @Validation annotation which actually belongs to LocationDTO
 *       + Location entity and LocationDTO can be made difference by where it located. Location entity presents
 *  Location table in database and should be in consistency. Whereas LocationDTO contains a whole or a part of properties of Location entity,
 * then DTO can be brought to display / interact with upper layer.
 *       + All annotations stem from jakarta.validation libraries should be
 * */

import com.baopen753.weatherapiproject.dailyweatherservices.entity.DailyWeather;
import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeather;
import com.baopen753.weatherapiproject.realtimeservices.entity.RealtimeWeather;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "locations")
@NoArgsConstructor
@Getter
@Setter

public class Location {

    @Id
    @Column(name = "code", length = 12, nullable = false, unique = true)
    //  using @Column(name = "code") for database first
    private String code;

    @Column(length = 128, nullable = false)
    private String cityName;

    @Column(length = 128, nullable = false)
    private String countryName;

    @Column(length = 128, nullable = true)
    private String regionName;

    @Column(length = 6, nullable = false)
    private String countryCode;

    private boolean enabled;

    private boolean trashed;

    @OneToOne(mappedBy = "location", cascade = CascadeType.ALL)     // unidirectional
    private RealtimeWeather realtimeWeather;

    @OneToMany(mappedBy = "hourlyWeatherId.location", cascade = CascadeType.ALL, orphanRemoval = true)
    // orphanRemoval = true: means when remove HourlyWeather object, the object is no longer existed in collection as well as in database
    // orphanRemoval = false: means when object is removed, the object is no longer existed but still in database
    private List<HourlyWeather> hourlyWeatherList = new ArrayList<HourlyWeather>();


    @OneToMany(mappedBy = "dailyWeatherId.location",cascade = CascadeType.ALL, orphanRemoval = true)   // specify the 'location' field of dailyWeatherId is the inverse side of this bidirectional relationship
    private List<DailyWeather> dailyWeatherList = new ArrayList<DailyWeather>();

    public Location(String code, String cityName, String countryName, String regionName, String countryCode, boolean enabled, boolean trashed) {
        this.code = code;
        this.cityName = cityName;
        this.countryName = countryName;
        this.regionName = regionName;
        this.countryCode = countryCode;
        this.enabled = enabled;
        this.trashed = trashed;
    }

    public Location(String cityName, String regionName, String countryLong, String countryCode) {
        this.cityName = cityName;
        this.countryName = countryLong;
        this.regionName = regionName;
        this.countryCode = countryCode;
    }


    @Override
    public String toString() {
        return this.regionName + ", " + this.cityName + ", " + this.countryName;
    }


    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Location other = (Location) obj;
        return Objects.equals(code, other.code);
    }


    public void cloneFieldsFrom(Location other) {
        this.cityName = other.cityName;
        this.countryName = other.countryName;
        this.regionName = other.regionName;
        this.countryCode = other.countryCode;
        this.enabled = other.enabled;
        this.code = other.code;
    }
}

