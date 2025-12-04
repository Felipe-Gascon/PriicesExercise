package com.prices.domain.port.in;

import com.prices.domain.model.Price;

/**
 * Input port (use case interface) - Dependency Inversion Principle
 * Application layer will implement this
 */
public interface GetApplicablePriceUseCase {
    Price execute(GetApplicablePriceQuery query);
}

