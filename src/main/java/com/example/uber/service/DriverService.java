package com.example.uber.service;

/**
 * Main service interface for Deriver related operations
 * Extends both DriverReadService and DriverWriteService interfaces
 * Following Dependency Inversion Principle (DIP)
 * */

public interface DriverService extends DriverReadService, DriverWriteService{
}
