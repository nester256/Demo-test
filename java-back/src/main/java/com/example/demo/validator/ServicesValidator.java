package com.example.demo.validator;

import com.example.demo.controller.exсeptions.ValidationException;
import com.example.demo.model.dto.ServicesDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ServicesValidator {
    public void validateService(ServicesDto service) throws ValidationException {
        if (Objects.isNull(service) || StringUtils.isBlank(service.getName())) {
            throw new ValidationException("Service name cannot be null or empty");
        }
        if (StringUtils.isBlank(service.getLanguage())) {
            throw new ValidationException("Service language cannot be null or empty");
        }
    }
    public void validateUpdate(ServicesDto service, Long id) throws ValidationException {
        validateService(service);
        if (Objects.isNull(id) || id <= 0) {
            throw new ValidationException("Service id cannot be less than 1 or null");
        }
    }
}
