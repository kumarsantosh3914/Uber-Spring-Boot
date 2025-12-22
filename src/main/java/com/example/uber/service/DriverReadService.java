package com.example.uber.service;

import com.example.uber.dto.DriverResponse;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for read operations related to Driver entity
 * Following Interface Segregation Principle (ISP)
 */

public interface DriverReadService {

    Optional<DriverResponse> findById(Long id);
    List<DriverResponse> findAll();
    Optional<DriverResponse> findByEmail(String email);
    List<DriverResponse> findAllAvailableDrivers();
}
