package com.financial.exchange.market.models.service;

import com.financial.exchange.market.models.dto.OperationDto;

public interface IOperationStrategy {

	void save(OperationDto operation);
}
