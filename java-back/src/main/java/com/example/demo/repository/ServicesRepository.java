package com.example.demo.repository;

import com.example.demo.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ServicesRepository extends JpaRepository<Services, Long>, JpaSpecificationExecutor<Services> {
}
