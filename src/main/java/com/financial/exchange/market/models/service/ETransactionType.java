package com.financial.exchange.market.models.service;

public enum ETransactionType {

	ENTRY(1L),
	DISCHARGE(2L);

	private final Long value;

	ETransactionType(Long i) {
		this.value = i;
	}

	public Long getValue() {
		return value;
	}
}
