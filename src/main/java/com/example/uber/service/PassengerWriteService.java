package com.example.uber.service;

import com.example.uber.dto.PassengerRequest;
import com.example.uber.dto.PassengerResponse;

/**
 * Interface for Passenger related operations
 * Following Dependency Inversion Principle (DIP)
 * */

public interface PassengerWriteService {

    PassengerResponse create(PassengerRequest request);
    PassengerResponse update(Long id, PassengerRequest request);
    void deleteById(Long id);
}
