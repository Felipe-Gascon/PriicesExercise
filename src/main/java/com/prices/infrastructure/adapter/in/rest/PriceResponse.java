package com.prices.infrastructure.adapter.in.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * REST Response DTO
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Price information response")
public class PriceResponse {

    @Schema(description = "Product identifier", example = "35455")
    private Long productId;

    @Schema(description = "Brand identifier", example = "1")
    private Long brandId;

    @Schema(description = "Price list identifier", example = "1")
    private Long priceList;

    @Schema(description = "Start date of price validity", example = "2020-06-14T00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @Schema(description = "End date of price validity", example = "2020-12-31T23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    @Schema(description = "Final price to apply", example = "35.50")
    private BigDecimal price;

    @Schema(description = "Currency code", example = "EUR")
    private String currency;
}

