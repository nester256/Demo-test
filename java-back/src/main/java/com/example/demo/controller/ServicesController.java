package com.example.demo.controller;


import com.example.demo.model.dto.ServicesDto;
import com.example.demo.model.filter.ServiceFilter;
import com.example.demo.service.ServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class ServicesController {
    private final ServicesService servicesService;

    @PostMapping("/services/find-all")
    public Page<ServicesDto> findAll(@RequestBody ServiceFilter filter, Pageable pageable) {
        return servicesService.findAll(filter, pageable);
    }

    @GetMapping("/services/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ServicesDto getService(@PathVariable Long id) {
        return servicesService.findById(id);
    }

    @PostMapping("/services")
    @ResponseStatus(HttpStatus.CREATED)
    public ServicesDto createService(@RequestBody ServicesDto service) {
        return servicesService.save(service);
    }

    @PutMapping("/services/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateService(@PathVariable Long id, @RequestBody ServicesDto service) {
        servicesService.update(id, service);
    }

    @DeleteMapping("/services/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteService(@PathVariable Long id) {
        servicesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
