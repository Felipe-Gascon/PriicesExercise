package com.prices.domain.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Value
@Builder
public class Price {
    Long id;
    Long brandId;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Long priceList;
    Long productId;
    Integer priority;
    BigDecimal price;
    String currency;

    /**
     * Business logic: Check if this price is applicable at given date
     */
    public boolean isApplicableAt(LocalDateTime date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Business logic: Check if this price has higher priority than another
     */
    public boolean hasHigherPriorityThan(Price other) {
        return this.priority > other.priority;
    }
}

