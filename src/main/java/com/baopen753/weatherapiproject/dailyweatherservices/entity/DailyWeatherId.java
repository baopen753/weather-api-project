package com.baopen753.weatherapiproject.dailyweatherservices.entity;

import com.baopen753.weatherapiproject.locationservices.entity.Location;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder


@Embeddable
public class DailyWeatherId implements Serializable {

    @Column(name = "day_of_month", nullable = false)
    @Min(value = 1, message = "Day of month should be in range 1-31")
    @Max(value = 31, message = "Day of month should be in range 1-31")
    private Integer dayOfMonth;

    @Column(name = "month", nullable = false)
    @Min(value = 1, message = "Month should be in range 1-12")
    @Max(value = 12, message = "Month should be in range 1-12")
    private Integer month;

    @ManyToOne
    private Location location;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DailyWeatherId that = (DailyWeatherId) obj;
        return Objects.equals(this.dayOfMonth, that.dayOfMonth) && Objects.equals(this.month, that.month) && Objects.equals(this.location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfMonth, month, location);
    }
}
