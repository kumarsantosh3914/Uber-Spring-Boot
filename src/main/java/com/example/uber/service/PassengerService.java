package com.example.uber.service;

/**
 * Main service interface for Passenger related operations
 * Extends both PassengerReadService and PassengerWriteService interfaces
 * Following Dependency Inversion Principle (DIP)
 * */

public interface PassengerService extends PassengerReadService, PassengerWriteService{
}
