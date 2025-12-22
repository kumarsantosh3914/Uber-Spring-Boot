package com.example.uber.service;

import com.example.uber.dto.DriverRequest;
import com.example.uber.dto.DriverResponse;

/**
 * Service interface for Driver write operations
 * Following Interface Segregation Principle (ISP)
 */

public interface DriverWriteService {

    DriverResponse create(DriverRequest request);
    DriverResponse update(Long id, DriverRequest request);
    void deleteById(Long id);
}
