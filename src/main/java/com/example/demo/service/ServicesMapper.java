package com.example.demo.service;

import com.example.demo.models.Services;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServicesMapper {

    Services dtoToModel(ServicesDto servicesDto);

    ServicesDto modelToDto(Services services);

    List<ServicesDto> toListDto(List<Services> services);
}
