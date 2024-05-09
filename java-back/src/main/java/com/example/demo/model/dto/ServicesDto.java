package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicesDto {

    /**
     * Unique identifier of the service.
     */
    private Long id;

    /**
     * Name of the service.
     */
    private String name;

    /**
     * Language in which the service operates.
     */
    private String language;
}
