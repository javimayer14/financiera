package com.financial.exchange.market.models.service;

public enum EOperationType {
	DAY_START(1L),
	DAY_CLOSE(2L),
	BUY_CURRENCY(3L),
	SELL_CURRENCY(4L),
	TRANSFER(5L),
	EXPENSE(6L),
	SALARY(7L),
	PAYMENT(8L),
	CHARGE(9L),
	REST(10L);


	private final Long value;

	EOperationType(Long i) {
		this.value = i;
	}

	public Long getState() {
		return value;
	}

}
