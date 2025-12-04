package com.prices.infrastructure.adapter.in.rest;

import com.prices.domain.model.Price;
import org.springframework.stereotype.Component;

/**
 * Mapper from domain to REST response
 */
@Component
public class PriceResponseMapper {

    public PriceResponse toResponse(Price price) {
        return PriceResponse.builder()
                .productId(price.getProductId())
                .brandId(price.getBrandId())
                .priceList(price.getPriceList())
                .startDate(price.getStartDate())
                .endDate(price.getEndDate())
                .price(price.getPrice())
                .currency(price.getCurrency())
                .build();
    }
}

