package com.prices.domain.port.out;

import com.prices.domain.model.Price;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Output port (repository interface) - Dependency Inversion Principle
 * Infrastructure layer will implement this
 */
public interface PriceRepository {
    /**
     * Find all prices applicable for given criteria
     *
     */
    List<Price> findApplicablePrices(Long productId, Long brandId, LocalDateTime applicationDate);
}

