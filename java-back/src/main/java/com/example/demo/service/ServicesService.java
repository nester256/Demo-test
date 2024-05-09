package com.example.demo.service;

import com.example.demo.model.dto.ServicesDto;
import com.example.demo.model.filter.ServiceFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing services.
 */
public interface ServicesService {

    /**
     * Retrieves a page of services filtered by the provided filter.
     *
     * @param filter   The filter to apply.
     * @param pageable The pageable configuration for pagination.
     * @return A page of ServicesDto objects.
     */
    Page<ServicesDto> findAll(ServiceFilter filter, Pageable pageable);

    /**
     * Retrieves a service by its ID.
     *
     * @param id The ID of the service to retrieve.
     * @return The ServicesDto object corresponding to the given ID, or null if not found.
     */
    ServicesDto findById(Long id);

    /**
     * Saves a new service.
     *
     * @param services The ServicesDto object to save.
     * @return The saved ServicesDto object.
     */
    ServicesDto save(ServicesDto services);

    /**
     * Updates an existing service.
     *
     * @param id     The ID of the service to update.
     * @param source The ServicesDto object containing updated data.
     */
    void update(Long id, ServicesDto source);

    /**
     * Deletes a service by its ID.
     *
     * @param id The ID of the service to delete.
     */
    void deleteById(Long id);
}
