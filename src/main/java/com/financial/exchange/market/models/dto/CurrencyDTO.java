package com.financial.exchange.market.models.dto;

import lombok.Data;

@Data
public class CurrencyDTO {
    private Long id;
	private String symbol;
	private String description;
}
