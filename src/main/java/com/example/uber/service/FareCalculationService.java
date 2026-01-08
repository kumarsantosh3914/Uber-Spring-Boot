package com.example.uber.service;

import java.math.BigDecimal;

/**
 * Service interface for calculating ride fares.
 */
public interface FareCalculationService {

    /**
     * Calculate fare based on pickup and dropoff coordinates.
     * 
     * @param pickupLatitude   pickup location latitude
     * @param pickupLongitude  pickup location longitude
     * @param dropoffLatitude  dropoff location latitude
     * @param dropoffLongitude dropoff location longitude
     * @return calculated fare
     */
    BigDecimal calculateFare(Double pickupLatitude, Double pickupLongitude,
            Double dropoffLatitude, Double dropoffLongitude);

    /**
     * Calculate distance between two coordinates using Haversine formula.
     * 
     * @param lat1 latitude of first point
     * @param lon1 longitude of first point
     * @param lat2 latitude of second point
     * @param lon2 longitude of second point
     * @return distance in kilometers
     */
    double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2);
}
