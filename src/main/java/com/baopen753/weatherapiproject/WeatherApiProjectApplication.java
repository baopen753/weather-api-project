package com.baopen753.weatherapiproject;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class WeatherApiProjectApplication {


    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);    //  mapping only properties of Source which share the same with Des
        return modelMapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(WeatherApiProjectApplication.class, args);

    }

}
