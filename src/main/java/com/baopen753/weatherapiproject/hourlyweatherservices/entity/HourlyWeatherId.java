package com.baopen753.weatherapiproject.hourlyweatherservices.entity;

import com.baopen753.weatherapiproject.locationservices.entity.Location;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Objects;

/*
 *   This class plays a role representing a key of HourlyWeather class.
 *   The key is composite primary key: hourOfDay, locationCode (reference by Location(code)   )
 * */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

@Embeddable
public class HourlyWeatherId implements Serializable {

    @Column(name = "hour_of_day")
    @Min(value = 0, message = "Hour of day should be [0,23]")
    @Max(value = 23, message = "Hour of day should be [0,23]")
    private Integer hourOfDay;

    @ManyToOne
    @JoinColumn(name = "location_code")       // is the name of column will be set within table - not name of object properties
    private Location location;

    @Override
    public int hashCode() {
        return Objects.hash(hourOfDay, location);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        HourlyWeatherId that = (HourlyWeatherId) obj;
        return Objects.equals(this.hourOfDay, that.hourOfDay) && Objects.equals(this.location, that.location);
    }
}
