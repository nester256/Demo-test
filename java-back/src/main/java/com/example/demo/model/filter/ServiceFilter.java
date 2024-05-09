package com.example.demo.model.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a filter for querying services.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceFilter {

    /**
     * List of IDs to filter services by.
     */
    private List<Long> ids = new ArrayList<>();

    /**
     * Search string to filter services by.
     */
    private String search;
}
