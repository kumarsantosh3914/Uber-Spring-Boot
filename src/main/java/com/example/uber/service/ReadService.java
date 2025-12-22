package com.example.uber.service;

import java.util.List;
import java.util.Optional;

/**
 * Generic interface for write operations
 * Following Interface Segregation Principle (ISP)
 */

public interface ReadService <T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
}
