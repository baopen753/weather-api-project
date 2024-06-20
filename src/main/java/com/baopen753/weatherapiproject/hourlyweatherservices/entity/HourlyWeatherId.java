package com.baopen753.weatherapiproject.hourlyweatherservices.entity;

import com.baopen753.weatherapiproject.locationservices.entity.Location;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*
 *   This class plays a role representing a key of HourlyWeather class.
 *   The key is composite primary key: hourOfDay, locationCode (reference by Location(code)   )
 * */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

@Embeddable
public class HourlyWeatherId implements Serializable {
    private Integer hourOfDay;

    @ManyToOne
    @JoinColumn(name = "location_code")   // is the name of column within table - not name of object properties
    private Location location;
}
