package com.example.uber.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {
    @NotNull(message = "Passenger ID is required")
    private Long passengerId;
    private Long driverId;
    @NotNull(message = "Pickup location lat is required")
    private Double pickupLocationLatitude;
    @NotNull(message = "Pickup location long is required")
    private Double pickupLocationLongitude;
    private String dropoffLocation;
    @NotNull(message = "Dropoff location lat is required")
    private Double dropoffLocationLatitude;
    @NotNull(message = "Dropoff location long is required")
    private Double dropoffLocationLongitude;
    private BigDecimal fare;
    private LocalDateTime scheduledPickupTime;
}
