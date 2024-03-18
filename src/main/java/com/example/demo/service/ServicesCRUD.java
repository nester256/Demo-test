package com.example.demo.service;


import com.example.demo.models.Services;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServicesCRUD implements ServicesService {

    private final ServicesRepository servicesRepository;
    private final ServicesMapper servicesMapper;
    @Override
    @Transactional
    public List<ServicesDto> findAll() {
        return servicesMapper.toListDto(servicesRepository.findAll());
    }

    @Override
    @Transactional
    public ServicesDto findById(Long id) {
        return Optional.of(servicesRepository.getById(id)).map(servicesMapper::modelToDto).get();
    }

    @Override
    @Transactional
    public ServicesDto save(ServicesDto service) {
        return servicesMapper.modelToDto(servicesRepository.save(servicesMapper.dtoToModel(service)));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        var service = getById(id);
        servicesRepository.delete(service);
    }

    private Services getById(Long id) {
        return servicesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Service with id: " + id + " not found"));
    }
}
