package com.prices.infrastructure.adapter.out.persistence;

import com.prices.domain.model.Price;
import com.prices.domain.port.out.PriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adapter that implements domain port (PriceRepository)
 * Adapts JPA repository to domain interface - Dependency Inversion
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PriceRepositoryAdapter implements PriceRepository {

    private final JpaPriceRepository jpaPriceRepository;
    private final PriceMapper priceMapper;

    @Override
    public List<Price> findApplicablePrices(Long productId, Long brandId, LocalDateTime applicationDate) {
        log.debug("Fetching applicable prices from database - productId: {}, brandId: {}, date: {}",
                productId, brandId, applicationDate);

        List<PriceEntity> entities = jpaPriceRepository.findApplicablePrices(
                productId,
                brandId,
                applicationDate
        );

        log.debug("Retrieved {} price entities from database", entities.size());

        return entities.stream()
                .map(priceMapper::toDomain)
                .collect(Collectors.toList());
    }
}

