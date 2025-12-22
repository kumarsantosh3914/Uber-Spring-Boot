package com.example.uber.service;

/**
 * Generic interface for write operations
 * Following Interface Segregation Principle (ISP)
 */

public interface WriteService<T, ID> {
    T create(T entity);
    T update(ID id, T entity);
    void delete(ID id);
}
