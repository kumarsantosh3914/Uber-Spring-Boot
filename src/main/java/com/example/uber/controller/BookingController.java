package com.example.uber.controller;

import com.example.uber.dto.BookingRequest;
import com.example.uber.dto.BookingResponse;
import com.example.uber.entity.Booking;
import com.example.uber.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookingResponse> bookings = bookingService.findAll();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        return bookingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByPassengerId(@PathVariable Long passengerId) {
        try {
            List<BookingResponse> bookings = bookingService.findByPassengerId(passengerId);
            return ResponseEntity.ok(bookings);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByDriverId(@PathVariable Long driverId) {
        try {
            List<BookingResponse> bookings = bookingService.findByDriverId(driverId);
            return ResponseEntity.ok(bookings);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        try {
            BookingResponse bookingResponse = bookingService.create(request);
            return ResponseEntity.ok(bookingResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> updateBooking(@PathVariable Long id, @Valid @RequestBody BookingRequest request) {
        try {
            BookingResponse bookingResponse = bookingService.update(id, request);
            return ResponseEntity.ok(bookingResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BookingResponse> updateBookingStatus(@PathVariable Long id, @RequestParam Booking.BookingStatus status) {
        try {
            BookingResponse bookingResponse = bookingService.updateStatus(id, status);
            return ResponseEntity.ok(bookingResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        try {
            bookingService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
