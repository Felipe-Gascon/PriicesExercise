package com.prices.infrastructure.adapter.in.rest;

import com.prices.domain.port.in.GetApplicablePriceQuery;
import com.prices.domain.port.in.GetApplicablePriceUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * REST Controller - Input Adapter
 * Follows REST principles with proper HTTP methods and status codes
 */
@RestController
@RequestMapping("/api/v1/prices")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Prices", description = "Price management API")
public class PriceController {

    private final GetApplicablePriceUseCase getApplicablePriceUseCase;
    private final PriceResponseMapper responseMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get applicable price",
            description = "Returns the applicable price for a product at a given date and brand",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Price found successfully",
                            content = @Content(schema = @Schema(implementation = PriceResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Price not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request parameters",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<PriceResponse> getApplicablePrice(
            @Parameter(description = "Application date and time", required = true, example = "2020-06-14T10:00:00")
            @RequestParam
            @NotNull(message = "Application date is required")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime applicationDate,

            @Parameter(description = "Product identifier", required = true, example = "35455")
            @RequestParam
            @NotNull(message = "Product ID is required")
            Long productId,

            @Parameter(description = "Brand identifier", required = true, example = "1")
            @RequestParam
            @NotNull(message = "Brand ID is required")
            Long brandId) {

        log.info("GET /api/v1/prices - Request received: applicationDate={}, productId={}, brandId={}",
                applicationDate, productId, brandId);

        var query = new GetApplicablePriceQuery(applicationDate, productId, brandId);
        var price = getApplicablePriceUseCase.execute(query);
        var response = responseMapper.toResponse(price);

        log.info("GET /api/v1/prices - Response: priceList={}, price={}",
                response.getPriceList(), response.getPrice());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}

