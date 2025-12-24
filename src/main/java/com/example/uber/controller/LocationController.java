package com.example.uber.controller;

import com.example.uber.dto.DriverLocationDTO;
import com.example.uber.dto.NearByDriversRequestDTO;
import com.example.uber.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/driver-location" )
    public ResponseEntity<Boolean> saveDriverLocation(@RequestBody DriverLocationDTO driverLocation) {
        Boolean saved = locationService.saveDriverLocation(
                driverLocation.getDriverId(),
                driverLocation.getLatitude(),
                driverLocation.getLongitude()
        );

        return ResponseEntity.ok(saved);
    }

    @PostMapping("/nearby-drivers" )
    public ResponseEntity<List<DriverLocationDTO>> getNearbyDrivers(@RequestBody NearByDriversRequestDTO request) {
        List<DriverLocationDTO> nearbyDrivers = locationService.getNearbyDrivers(
                request.getLatitude(),
                request.getLongitude(),
                request.getRadius()
        );

        return ResponseEntity.ok(nearbyDrivers);
    }
}
