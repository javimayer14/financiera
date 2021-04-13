package com.financial.exchange.market.models.service;

public enum EOperationStatus {

	ENTERED(1L),
	APPROVED(2L),
	ASSIGNED(3L),
	ACCEPTED(4L),
	ARRIVED(5L),
	FINISHED(6L),
	SUSPENDED(7L),
	CANCELLED(8L),
	REJECTED(9L),
	ARCHIVED(10L);

	private final Long value;

	EOperationStatus(Long i) {
		this.value = i;
	}

	public Long getStatus() {
		return value;
	}
}

