package com.financial.exchange.market.models.dto;

import java.io.Serializable;

import com.financial.exchange.market.models.entity.CommissionType;

import lombok.Data;

@Data
public class CommissionDTO implements Serializable {

	private static final long serialVersionUID = 3505118920096851788L;

	private Long id;
	private CommissionType commissionType;
	private Float commissionAmount;
	private Long transactionType;
}
