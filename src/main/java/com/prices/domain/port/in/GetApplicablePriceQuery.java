package com.prices.domain.port.in;

import lombok.Value;

import java.time.LocalDateTime;

/**
 * Query object for GetApplicablePriceUseCase
 * Encapsulates input parameters following CQS pattern
 */
@Value
public class GetApplicablePriceQuery {
    LocalDateTime applicationDate;
    Long productId;
    Long brandId;
}

