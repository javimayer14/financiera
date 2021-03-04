package com.financial.exchange.market.models.service;

public enum ETransactionStatus {


	PENDING_ACCREDITATION(1L),
	ACCREDITED(2L);

	private final Long value;

	ETransactionStatus(Long i) {
		this.value = i;
	}

	public Long getValue() {
		return value;
	}
}
