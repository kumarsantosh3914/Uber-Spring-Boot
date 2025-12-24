package com.example.uber.service;

import com.example.uber.dto.DriverLocationDTO;

import java.util.List;

public interface LocationService {

    Boolean saveDriverLocation(String driverId, Double latitude, Double longitude);
    List<DriverLocationDTO> getNearbyDrivers(Double latitude, Double longitude, Double radius);
}
