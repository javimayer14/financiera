package com.financial.exchange.market.models.service;

public enum EAccountType {
	CASH(1L),
	CURRENT_ACCOUNT(2L);

	private final Long value;

	EAccountType(Long i) {
		this.value = i;
	}

	public Long getState() {
		return value;
	}

}