package com.prices.application.service;

import com.prices.domain.exception.PriceNotFoundException;
import com.prices.domain.model.Price;
import com.prices.domain.port.in.GetApplicablePriceQuery;
import com.prices.domain.port.in.GetApplicablePriceUseCase;
import com.prices.domain.port.out.PriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

/**
 * Application Service - Implements use case
 * Single Responsibility: Orchestrate the price selection logic
 * Uses domain port (interface) for repository - Dependency Inversion
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GetApplicablePriceService implements GetApplicablePriceUseCase {

    private final PriceRepository priceRepository;

    @Override
    public Price execute(GetApplicablePriceQuery query) {
        log.debug("Executing GetApplicablePriceUseCase for product: {}, brand: {}, date: {}",
                query.getProductId(), query.getBrandId(), query.getApplicationDate());

        List<Price> applicablePrices = priceRepository.findApplicablePrices(
                query.getProductId(),
                query.getBrandId(),
                query.getApplicationDate()
        );

        log.debug("Found {} applicable prices", applicablePrices.size());

        return applicablePrices.stream()
                .max(Comparator.comparing(Price::getPriority))
                .orElseThrow(() -> {
                    log.warn("No price found for query: {}", query);
                    return new PriceNotFoundException(
                            query.getProductId(),
                            query.getBrandId(),
                            query.getApplicationDate().toString()
                    );
                });
    }
}
