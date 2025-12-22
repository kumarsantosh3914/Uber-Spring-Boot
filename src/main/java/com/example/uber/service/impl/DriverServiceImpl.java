package com.example.uber.service.impl;

import com.example.uber.dto.DriverRequest;
import com.example.uber.dto.DriverResponse;
import com.example.uber.entity.Driver;
import com.example.uber.mapper.DriverMapper;
import com.example.uber.repository.DriverRepository;
import com.example.uber.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    @Override
    @Transactional(readOnly = true) // Read-only transaction for find operation to optimize performance
    public Optional<DriverResponse> findById(Long id) {
        return driverRepository.findById(id)
                .map(driverMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DriverResponse> findAll() {
        return driverRepository.findAll().stream()
                .map(driverMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DriverResponse> findByEmail(String email) {
        return driverRepository.findByEmail(email)
                .map(driverMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DriverResponse> findAllAvailableDrivers() {
        return driverRepository.findAll().stream()
                .filter(Driver::getIsAvailable)
                .map(driverMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DriverResponse create(DriverRequest request) {
        if(driverRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Driver with email " + request.getEmail() + " already exists.");
        }

        if(driverRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new IllegalArgumentException("Driver with license number " + request.getLicenseNumber() + " already exists.");
        }

        Driver driver = driverMapper.toEntity(request);
        Driver savedDriver = driverRepository.save(driver);
        return driverMapper.toResponse(savedDriver);
    }

    @Override
    public DriverResponse update(Long id, DriverRequest request) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found with id: " + id));

        // Check if email is being changed and if new email already exists
        if(!driver.getEmail().equals(request.getEmail()) && driverRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Driver with email " + request.getEmail() + " already exists.");
        }

        // Check if license number is being changed and if new license number already exists
        if(!driver.getLicenseNumber().equals(request.getLicenseNumber()) && driverRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new IllegalArgumentException("Driver with license number " + request.getLicenseNumber() + " already exists.");
        }

        driverMapper.updateEntity(driver, request);
        Driver updatedDriver = driverRepository.save(driver);
        return driverMapper.toResponse(updatedDriver);
    }

    @Override
    public void deleteById(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new IllegalArgumentException("Driver not found with id: " + id);
        }
        driverRepository.deleteById(id);
    }
}
