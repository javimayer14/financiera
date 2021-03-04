package com.financial.exchange.market.models.service;

public enum ECommissionType {


	PORCENT(1L),
	FIXEDAMOUNT(2L);

	private final Long value;

	ECommissionType(Long i) {
		this.value = i;
	}

	public Long getValue() {
		return value;
	}

}
