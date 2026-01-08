package com.example.uber.service.impl;

import com.example.uber.dto.BookingRequest;
import com.example.uber.dto.BookingResponse;
import com.example.uber.entity.Booking;
import com.example.uber.entity.Driver;
import com.example.uber.entity.Passenger;
import com.example.uber.mapper.BookingMapper;
import com.example.uber.repository.BookingRepository;
import com.example.uber.repository.DriverRepository;
import com.example.uber.repository.PassengerRepository;
import com.example.uber.service.BookingService;
import com.example.uber.service.FareCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final DriverRepository driverRepository;
    private final BookingMapper bookingMapper;
    private final FareCalculationService fareCalculationService;

    @Override
    @Transactional(readOnly = true)
    public Optional<BookingResponse> findById(Long id) {
        return bookingRepository.findById(id)
                .map(bookingMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> findAll() {
        return bookingRepository.findAll()
                .stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> findByPassengerId(Long passengerId) {
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new IllegalArgumentException("Passenger not found with id: " + passengerId));

        return bookingRepository.findByPassenger(passenger).stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> findByDriverId(Long driverId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found with id: " + driverId));

        return bookingRepository.findByDriver(driver)
                .stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponse create(BookingRequest request) {
        Passenger passenger = passengerRepository.findById(request.getPassengerId())
                .orElseThrow(
                        () -> new IllegalArgumentException("Passenger not found with id: " + request.getPassengerId()));

        Driver driver = null;
        if (request.getDriverId() != null) {
            driver = driverRepository.findById(request.getDriverId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Driver not found with id: " + request.getDriverId()));

            if (!driver.getIsAvailable()) {
                throw new IllegalArgumentException("Driver with id: " + driver.getId() + " is not available");
            }
        }

        // Calculate fare if not provided
        if (request.getFare() == null) {
            BigDecimal calculatedFare = fareCalculationService.calculateFare(
                    request.getPickupLocationLatitude(),
                    request.getPickupLocationLongitude(),
                    request.getDropoffLocationLatitude(),
                    request.getDropoffLocationLongitude());
            request.setFare(calculatedFare);
        }

        Booking booking = bookingMapper.toEntity(request, passenger, driver);

        // If driver is assigned, mark as unavailable
        if (driver != null) {
            driver.setIsAvailable(false);
            driverRepository.save(driver);
        }

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toResponse(savedBooking);
    }

    @Override
    public BookingResponse update(Long id, BookingRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));

        Passenger passenger = passengerRepository.findById(request.getPassengerId())
                .orElseThrow(
                        () -> new IllegalArgumentException("Passenger not found with id: " + request.getPassengerId()));

        Driver driver = null;
        if (request.getDriverId() != null) {
            driver = driverRepository.findById(request.getDriverId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Driver not found with id: " + request.getDriverId()));
        }

        // Update driver availability if changed
        Driver previousDriver = booking.getDriver();
        if (previousDriver != null && !previousDriver.equals(driver)) {
            previousDriver.setIsAvailable(true);
            driverRepository.save(previousDriver);
        }

        if (driver != null && !driver.equals(previousDriver)) {
            if (!driver.getIsAvailable()) {
                throw new IllegalArgumentException("Driver with id: " + driver.getId() + " is not available");
            }
            driver.setIsAvailable(false);
            driverRepository.save(driver);
        }

        bookingMapper.updateEntity(booking, request, passenger, driver);
        Booking updatedBooking = bookingRepository.save(booking);
        return bookingMapper.toResponse(updatedBooking);
    }

    @Override
    public BookingResponse updateStatus(Long id, Booking.BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));

        booking.setStatus(status);

        // Handle status-specific logic
        if (status == Booking.BookingStatus.IN_PROGRESS && booking.getActualPickupTime() == null) {
            booking.setActualPickupTime(LocalDateTime.now());
        } else if (status == Booking.BookingStatus.COMPLETED) {
            booking.setCompletedAt(LocalDateTime.now());

            // Release driver
            if (booking.getDriver() != null) {
                Driver driver = booking.getDriver();
                driver.setIsAvailable(true);
                driverRepository.save(driver);
            }
        } else if (status == Booking.BookingStatus.CANCELED) {
            // Release driver
            if (booking.getDriver() != null) {
                Driver driver = booking.getDriver();
                driver.setIsAvailable(true);
                driverRepository.save(driver);
            }
        }

        Booking updateBooking = bookingRepository.save(booking);
        return bookingMapper.toResponse(updateBooking);
    }

    @Override
    public Boolean acceptRide(Long id, Integer driverId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found with id: " + driverId));

        booking.setDriver(driver);
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        driver.setIsAvailable(false);
        driverRepository.save(driver);
        bookingRepository.save(booking);

        return true;
    }

    @Override
    public void deleteById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));

        // Release driver if assigned
        if (booking.getDriver() != null) {
            Driver driver = booking.getDriver();
            driver.setIsAvailable(true);
            driverRepository.save(driver);
        }

        bookingRepository.deleteById(id);
    }
}
