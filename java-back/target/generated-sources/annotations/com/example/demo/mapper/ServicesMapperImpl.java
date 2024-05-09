package com.example.demo.mapper;

import com.example.demo.model.Services;
import com.example.demo.model.dto.ServicesDto;
import com.example.demo.model.dto.ServicesDto.ServicesDtoBuilder;
import io.grpc.demo.service.Service;
import io.grpc.demo.service.Service.Builder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-16T15:45:17+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 21.0.2 (Homebrew)"
)
@Component
public class ServicesMapperImpl implements ServicesMapper {

    @Override
    public Services dtoToModel(ServicesDto servicesDto) {
        if ( servicesDto == null ) {
            return null;
        }

        Services services = new Services();

        services.setId( servicesDto.getId() );
        services.setName( servicesDto.getName() );
        services.setLanguage( servicesDto.getLanguage() );

        return services;
    }

    @Override
    public ServicesDto modelToDto(Services services) {
        if ( services == null ) {
            return null;
        }

        ServicesDtoBuilder servicesDto = ServicesDto.builder();

        servicesDto.id( services.getId() );
        servicesDto.name( services.getName() );
        servicesDto.language( services.getLanguage() );

        return servicesDto.build();
    }

    @Override
    public Services merge(Services target, ServicesDto source) {
        if ( source == null ) {
            return null;
        }

        target.setName( source.getName() );
        target.setLanguage( source.getLanguage() );

        return target;
    }

    @Override
    public Service toGrpc(ServicesDto service) {
        if ( service == null ) {
            return null;
        }

        Builder service1 = Service.newBuilder();

        if ( service.getId() != null ) {
            service1.setId( service.getId().intValue() );
        }
        service1.setName( service.getName() );
        service1.setLanguage( service.getLanguage() );

        return service1.build();
    }

    @Override
    public Service toGrpc(Services service) {
        if ( service == null ) {
            return null;
        }

        Builder service1 = Service.newBuilder();

        if ( service.getId() != null ) {
            service1.setId( service.getId().intValue() );
        }
        service1.setName( service.getName() );
        service1.setLanguage( service.getLanguage() );

        return service1.build();
    }

    @Override
    public ServicesDto toDto(Service service) {
        if ( service == null ) {
            return null;
        }

        ServicesDtoBuilder servicesDto = ServicesDto.builder();

        servicesDto.id( (long) service.getId() );
        servicesDto.name( service.getName() );
        servicesDto.language( service.getLanguage() );

        return servicesDto.build();
    }
}
