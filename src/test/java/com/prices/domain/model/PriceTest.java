package com.prices.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for Price domain model
 */
@DisplayName("Price Domain Model Tests")
class PriceTest {

    @Test
    @DisplayName("Should build Price with all properties using builder")
    void shouldBuildPriceWithBuilder() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 12, 31, 23, 59);

        // When
        Price price = Price.builder()
                .id(1L)
                .brandId(1L)
                .productId(35455L)
                .priceList(1L)
                .priority(0)
                .price(new BigDecimal("35.50"))
                .currency("EUR")
                .startDate(startDate)
                .endDate(endDate)
                .build();

        // Then
        assertThat(price).isNotNull();
        assertThat(price.getId()).isEqualTo(1L);
        assertThat(price.getBrandId()).isEqualTo(1L);
        assertThat(price.getProductId()).isEqualTo(35455L);
        assertThat(price.getPriceList()).isEqualTo(1L);
        assertThat(price.getPriority()).isEqualTo(0);
        assertThat(price.getPrice()).isEqualByComparingTo("35.50");
        assertThat(price.getCurrency()).isEqualTo("EUR");
        assertThat(price.getStartDate()).isEqualTo(startDate);
        assertThat(price.getEndDate()).isEqualTo(endDate);
    }

    @Test
    @DisplayName("Should return true when date is within validity period")
    void shouldBeApplicableAtGivenDate() {
        // Given
        Price price = Price.builder()
                .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59))
                .build();

        LocalDateTime testDate = LocalDateTime.of(2020, 6, 15, 10, 0);

        // When
        boolean isApplicable = price.isApplicableAt(testDate);

        // Then
        assertThat(isApplicable).isTrue();
    }

    @Test
    @DisplayName("Should return false when date is before start date")
    void shouldNotBeApplicableBeforeStartDate() {
        // Given
        Price price = Price.builder()
                .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59))
                .build();

        LocalDateTime testDate = LocalDateTime.of(2020, 6, 13, 23, 59);

        // When
        boolean isApplicable = price.isApplicableAt(testDate);

        // Then
        assertThat(isApplicable).isFalse();
    }

    @Test
    @DisplayName("Should return false when date is after end date")
    void shouldNotBeApplicableAfterEndDate() {
        // Given
        Price price = Price.builder()
                .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59))
                .build();

        LocalDateTime testDate = LocalDateTime.of(2021, 1, 1, 0, 0);

        // When
        boolean isApplicable = price.isApplicableAt(testDate);

        // Then
        assertThat(isApplicable).isFalse();
    }

    @Test
    @DisplayName("Should return true when date equals start date")
    void shouldBeApplicableAtStartDate() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
        Price price = Price.builder()
                .startDate(startDate)
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59))
                .build();

        // When
        boolean isApplicable = price.isApplicableAt(startDate);

        // Then
        assertThat(isApplicable).isTrue();
    }

    @Test
    @DisplayName("Should return true when date equals end date")
    void shouldBeApplicableAtEndDate() {
        // Given
        LocalDateTime endDate = LocalDateTime.of(2020, 12, 31, 23, 59);
        Price price = Price.builder()
                .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                .endDate(endDate)
                .build();

        // When
        boolean isApplicable = price.isApplicableAt(endDate);

        // Then
        assertThat(isApplicable).isTrue();
    }

    @Test
    @DisplayName("Should correctly compare priority between two prices")
    void shouldCompareHigherPriority() {
        // Given
        Price lowerPriorityPrice = Price.builder().priority(0).build();
        Price higherPriorityPrice = Price.builder().priority(1).build();

        // When & Then
        assertThat(higherPriorityPrice.hasHigherPriorityThan(lowerPriorityPrice)).isTrue();
        assertThat(lowerPriorityPrice.hasHigherPriorityThan(higherPriorityPrice)).isFalse();
    }
}

