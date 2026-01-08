package com.example.uber.service.impl;

import com.example.uber.service.FareCalculationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Implementation of FareCalculationService.
 * Uses Haversine formula to calculate distance and applies configurable rates.
 */
@Service
public class FareCalculationServiceImpl implements FareCalculationService {

    private static final double EARTH_RADIUS_KM = 6371.0;

    @Value("${uber.fare.base:50.0}")
    private double baseFare;

    @Value("${uber.fare.per-km:12.0}")
    private double perKmRate;

    @Value("${uber.fare.minimum:50.0}")
    private double minimumFare;

    @Override
    public BigDecimal calculateFare(Double pickupLatitude, Double pickupLongitude,
            Double dropoffLatitude, Double dropoffLongitude) {
        double distanceKm = calculateDistance(pickupLatitude, pickupLongitude,
                dropoffLatitude, dropoffLongitude);

        double fare = baseFare + (distanceKm * perKmRate);

        // Ensure minimum fare
        fare = Math.max(fare, minimumFare);

        return BigDecimal.valueOf(fare).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        // Haversine formula to calculate distance between two points on Earth
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}
