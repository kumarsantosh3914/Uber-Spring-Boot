package com.example.uber.service;

import com.example.uber.dto.BookingRequest;
import com.example.uber.dto.BookingResponse;
import com.example.uber.entity.Booking;

/**
 * Interface for Booking write operations.
 * Following Interface Segregation Principle (ISP).
 */

public interface BookingWriteService {

    BookingResponse create(BookingRequest request);
    BookingResponse update(Long id, BookingRequest request);
    BookingResponse updateStatus(Long id, Booking.BookingStatus status);
    Boolean acceptRide(Long id, Integer driverId);
    void deleteById(Long id);
}
