package com.financial.exchange.market.models.service;

import com.financial.exchange.market.models.dto.OperationDto;

public abstract class AbstractTradingOperation extends AbstractOperationStrategy {

	public void enterOperation(OperationDto operation, Long operationType) {
		operationService.enterTradingOperation(operation, operationType);
	}

	public void assignedOperation(OperationDto operation, Long accountType) {
		operationService.assignedOperation(operation, accountType);
	}

}
