package com.prices.domain.port.in;

import com.prices.domain.model.Price;


public interface GetApplicablePriceUseCase {
    Price execute(GetApplicablePriceQuery query);
}

