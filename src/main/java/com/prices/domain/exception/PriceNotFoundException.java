package com.prices.domain.exception;

/**
 * Domain exception thrown when no applicable price is found
 */
public class PriceNotFoundException extends RuntimeException {
    public PriceNotFoundException(String message) {
        super(message);
    }

    public PriceNotFoundException(Long productId, Long brandId, String date) {
        super(String.format("No price found for product %d, brand %d at %s", productId, brandId, date));
    }
}

