package com.prices.domain.port.in;

import lombok.Value;

import java.time.LocalDateTime;

/**
 * Query object for GetApplicablePriceUseCase
 */
@Value
public class GetApplicablePriceQuery {
    LocalDateTime applicationDate;
    Long productId;
    Long brandId;
}

