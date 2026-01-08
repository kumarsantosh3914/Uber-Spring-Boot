package com.example.uber.mapper;

import com.example.uber.dto.BookingRequest;
import com.example.uber.dto.BookingResponse;
import com.example.uber.entity.Booking;
import com.example.uber.entity.Driver;
import com.example.uber.entity.Passenger;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public Booking toEntity(BookingRequest request, Passenger passenger, Driver driver) {
        Booking.BookingStatus status = driver != null ? Booking.BookingStatus.CONFIRMED : Booking.BookingStatus.PENDING;

        return Booking.builder()
                .passenger(passenger)
                .driver(driver)
                .pickupLocationLatitude(request.getPickupLocationLatitude().toString())
                .pickupLocationLongitude(request.getPickupLocationLongitude().toString())
                .dropoffLocation(request.getDropoffLocation())
                .dropoffLocationLatitude(request.getDropoffLocationLatitude().toString())
                .dropoffLocationLongitude(request.getDropoffLocationLongitude().toString())
                .fare(request.getFare())
                .status(status)
                .scheduledPickupTime(request.getScheduledPickupTime())
                .build();
    }

    public BookingResponse toResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .passengerId(booking.getPassenger() != null ? booking.getPassenger().getId() : null)
                .passengerName(booking.getPassenger() != null ? booking.getPassenger().getName() : null)
                .driverId(booking.getDriver() != null ? booking.getDriver().getId() : null)
                .driverName(booking.getDriver() != null ? booking.getDriver().getName() : null)
                .pickupLocationLatitude(Double.parseDouble(booking.getPickupLocationLatitude()))
                .pickupLocationLongitude(Double.parseDouble(booking.getPickupLocationLongitude()))
                .dropoffLocation(booking.getDropoffLocation())
                .dropoffLocationLatitude(Double.parseDouble(booking.getDropoffLocationLatitude()))
                .dropoffLocationLongitude(Double.parseDouble(booking.getDropoffLocationLongitude()))
                .status(booking.getStatus())
                .fare(booking.getFare())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .scheduledPickupTime(booking.getScheduledPickupTime())
                .actualPickupTime(booking.getActualPickupTime())
                .completedAt(booking.getCompletedAt())
                .build();
    }

    public void updateEntity(Booking booking, BookingRequest request, Passenger passenger, Driver driver) {
        booking.setPassenger(passenger);
        booking.setDriver(driver);
        booking.setPickupLocationLatitude(request.getPickupLocationLatitude().toString());
        booking.setPickupLocationLongitude(request.getPickupLocationLongitude().toString());
        booking.setDropoffLocation(request.getDropoffLocation());
        booking.setDropoffLocationLatitude(request.getDropoffLocationLatitude().toString());
        booking.setDropoffLocationLongitude(request.getDropoffLocationLongitude().toString());
        booking.setFare(request.getFare());
        booking.setScheduledPickupTime(request.getScheduledPickupTime());

        // Update status based on driver assignment
        if (driver != null && booking.getStatus() == Booking.BookingStatus.PENDING) {
            booking.setStatus(Booking.BookingStatus.CONFIRMED);
        }
    }
}
