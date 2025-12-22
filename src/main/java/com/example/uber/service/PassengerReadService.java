package com.example.uber.service;

import com.example.uber.dto.PassengerResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface for Passenger related read operations
 * Following Dependency Inversion Principle (DIP)
 * */

public interface PassengerReadService {

    Optional<PassengerResponse> findById(Long id);
    List<PassengerResponse> findAll();
    Optional<PassengerResponse> findByEmail(String email);
}
