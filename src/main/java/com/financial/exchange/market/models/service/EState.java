package com.financial.exchange.market.models.service;

public enum EState {
    DELETED(0),
    ACTIVE(1);

	private final Integer value;

	EState(Integer i) {
		this.value = i;
	}
	
	public Integer getState() {
		return value;
	}
}
