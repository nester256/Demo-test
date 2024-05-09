package com.example.demo.mapper;

import com.example.demo.model.Services;
import com.example.demo.model.dto.ServicesDto;
import com.example.demo.model.filter.ServiceFilter;
import io.grpc.demo.service.FindServiceRequest;
import io.grpc.demo.service.FindServiceResponse;
import io.grpc.demo.service.Pagination;
import io.grpc.demo.service.Service;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Mapper for converting objects between Services and ServicesDto classes.
 */
@Mapper(componentModel = "spring")
public interface ServicesMapper {

    /**
     * Converts a ServicesDto object to a Services object.
     *
     * @param servicesDto The ServicesDto object to convert.
     * @return The converted Services object.
     */
    Services dtoToModel(ServicesDto servicesDto);

    /**
     * Converts a Services object to a ServicesDto object.
     *
     * @param services The Services object to convert.
     * @return The converted ServicesDto object.
     */
    ServicesDto modelToDto(Services services);

    /**
     * Merges fields of a ServicesDto object into a Services object.
     *
     * @param target The target Services object to merge the data into.
     * @param source The source ServicesDto object whose data will be merged into the target object.
     * @return The Services object with merged data.
     */
    @Mapping(target = "id", ignore = true)
    Services merge(@MappingTarget Services target, ServicesDto source);

    Service toGrpc(ServicesDto service);

    Service toGrpc(Services service);

    ServicesDto toDto(Service service);

    default ServiceFilter toFilter(FindServiceRequest request) {
        ServiceFilter.ServiceFilterBuilder builder = ServiceFilter.builder();
        if (StringUtils.isNotBlank(request.getSearch())) {
            builder.search(request.getSearch());
        }
        if (CollectionUtils.isNotEmpty(request.getIdsList())) {
            builder.ids(request.getIdsList());
        }
        return builder.build();
    }

    default Pageable toPageble(FindServiceRequest request) {
        return request.hasPagination()? PageRequest.of(
                request.getPagination().getCurrentPage() - 1, request.getPagination().getItemsPerPage()
        ) : Pageable.unpaged();
    }

    default FindServiceResponse toResponse(Page<Services> result) {
        Pagination pagination = Pagination.newBuilder().setTotalPages(result.getTotalPages()).setTotalItems(
                result.getTotalElements()
        ).build();
        List<Service> list = result.get().map(this::toGrpc).toList();
        return FindServiceResponse.newBuilder().setPagination(pagination).addAllService(list).build();
    }
}
