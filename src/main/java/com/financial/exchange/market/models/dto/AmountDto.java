package com.financial.exchange.market.models.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class AmountDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long currencyId;
	private Float amount;

}
