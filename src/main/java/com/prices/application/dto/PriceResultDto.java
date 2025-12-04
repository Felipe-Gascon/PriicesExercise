package com.prices.application.dto;

import java.time.LocalDateTime;

public record PriceResultDto(
        Long productId,
        Long brandId,
        Long priceList,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Double price,
        String currency
) {}
