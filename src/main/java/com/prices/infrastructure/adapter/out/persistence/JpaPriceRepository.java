package com.prices.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA Repository
 * Optimized query with index usage for scalability
 */
@Repository
public interface JpaPriceRepository extends JpaRepository<PriceEntity, Long> {

    /**
     * Find applicable prices with optimized query
     * Uses index on (product_id, brand_id, start_date, end_date)
     * Orders by priority DESC to facilitate max selection
     */
    @Query("SELECT p FROM PriceEntity p " +
            "WHERE p.productId = :productId " +
            "AND p.brandId = :brandId " +
            "AND :applicationDate BETWEEN p.startDate AND p.endDate " +
            "ORDER BY p.priority DESC")
    List<PriceEntity> findApplicablePrices(
            @Param("productId") Long productId,
            @Param("brandId") Long brandId,
            @Param("applicationDate") LocalDateTime applicationDate
    );
}

