package com.example.demo.service.impl;


import com.example.demo.controller.exсeptions.ServiceNotFoundException;
import com.example.demo.mapper.ServicesMapper;
import com.example.demo.model.Services;
import com.example.demo.model.dto.ServicesDto;
import com.example.demo.model.filter.ServiceFilter;
import com.example.demo.repository.ServicesRepository;
import com.example.demo.service.ServicesService;
import com.example.demo.service.impl.specification.ServicesSpecification;
import com.example.demo.validator.ServicesValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServicesServiceImpl implements ServicesService {

    private final ServicesRepository servicesRepository;
    private final ServicesMapper servicesMapper;
    private final ServicesSpecification specification;
    private final ServicesValidator servicesValidator;

    @Override
    public Page<ServicesDto> findAll(ServiceFilter filter, Pageable pageable) {
        log.info("Finding all services with filter {} and pageable {}", filter, pageable);
        Page<ServicesDto> result = servicesRepository.findAll(specification.byFilter(filter), pageable)
                .map(servicesMapper::modelToDto);
        log.info("Found {} services", result.getTotalElements());
        return result;
    }

    @Override
    public ServicesDto findById(Long id) {
        log.info("Finding service by id {}", id);
        var service = getById(id);
        ServicesDto result = Optional.of(service)
                .map(servicesMapper::modelToDto)
                .orElse(null);
        log.info("Found service by id {}: {}", id, result);
        return result;
    }

    @Override
    @Transactional
    public ServicesDto save(ServicesDto service) {
        servicesValidator.validateService(service);
        log.info("Saving service {}", service);
        ServicesDto result = servicesMapper.modelToDto(servicesRepository.save(servicesMapper.dtoToModel(service)));
        log.info("Service saved: {}", result);
        return result;
    }


    @Override
    @Transactional
    public void update(Long id, ServicesDto source) {
        servicesValidator.validateUpdate(source, id);
        log.info("Updating started for the service with id {}, by data {}", id, source);
        Services target = getById(id);
        servicesRepository.save(servicesMapper.merge(target, source));
        log.info("Service with id {} was successfully updated by data {}", id, source);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Deleting service by id {}", id);
        var service = getById(id);
        servicesRepository.delete(service);
        log.info("Service with id {} deleted successfully", id);
    }

    private Services getById(Long id) {
        return servicesRepository.findById(id).orElseThrow(() -> new ServiceNotFoundException(String.format("Service with id: %s not found", id)));
    }
}
