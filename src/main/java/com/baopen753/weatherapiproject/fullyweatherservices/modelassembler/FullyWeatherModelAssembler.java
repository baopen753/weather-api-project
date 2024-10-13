package com.baopen753.weatherapiproject.fullyweatherservices.modelassembler;

import com.baopen753.weatherapiproject.fullyweatherservices.dto.FullyWeatherDto;
import com.baopen753.weatherapiproject.fullyweatherservices.restcontroller.FullyWeatherRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FullyWeatherModelAssembler implements RepresentationModelAssembler<FullyWeatherDto, EntityModel<FullyWeatherDto>> {

    @Override
    public EntityModel<FullyWeatherDto> toModel(FullyWeatherDto dto) {

        EntityModel<FullyWeatherDto> entityModel = EntityModel.of(dto);
        entityModel.add(linkTo(methodOn(FullyWeatherRestController.class).getFullyWeatherByIpAddress(null)).withSelfRel());
        return entityModel;
    }


}
