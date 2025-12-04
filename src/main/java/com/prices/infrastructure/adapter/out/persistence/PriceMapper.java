package com.prices.infrastructure.adapter.out.persistence;

import com.prices.domain.model.Price;
import org.springframework.stereotype.Component;

/**
 * Mapper between domain and persistence models
 * Single Responsibility: Transform entities
 */
@Component
public class PriceMapper {

    public Price toDomain(PriceEntity entity) {
        return Price.builder()
                .id(entity.getId())
                .brandId(entity.getBrandId())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .priceList(entity.getPriceList())
                .productId(entity.getProductId())
                .priority(entity.getPriority())
                .price(entity.getPrice())
                .currency(entity.getCurrency())
                .build();
    }

    public PriceEntity toEntity(Price domain) {
        return PriceEntity.builder()
                .id(domain.getId())
                .brandId(domain.getBrandId())
                .startDate(domain.getStartDate())
                .endDate(domain.getEndDate())
                .priceList(domain.getPriceList())
                .productId(domain.getProductId())
                .priority(domain.getPriority())
                .price(domain.getPrice())
                .currency(domain.getCurrency())
                .build();
    }
}

