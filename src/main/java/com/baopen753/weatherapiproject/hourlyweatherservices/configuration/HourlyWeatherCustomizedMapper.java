package com.baopen753.weatherapiproject.hourlyweatherservices.configuration;

import com.baopen753.weatherapiproject.hourlyweatherservices.dto.HourlyWeatherDto;
import com.baopen753.weatherapiproject.hourlyweatherservices.entity.HourlyWeather;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

@Configurable
public class HourlyWeatherCustomizedMapper {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // define a customized mapper
        modelMapper.addMappings(new PropertyMap<HourlyWeather, HourlyWeatherDto>() {

            @Override
            protected void configure() {
                map().setHourOfDay(source.getHourlyWeatherId().getHourOfDay());
                map().setStatus(source.getStatus());
                map().setPrecipitation(source.getPrecipitation());
                map().setTemperature(source.getTemperature());
            }
        });
        return modelMapper;
    }

}
