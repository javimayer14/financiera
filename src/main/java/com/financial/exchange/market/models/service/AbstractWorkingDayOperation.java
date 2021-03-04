package com.financial.exchange.market.models.service;

import com.financial.exchange.market.models.dto.OperationDto;

public class AbstractWorkingDayOperation extends AbstractOperationStrategy {

	public void enterOperation(OperationDto operation, Long operationType) {

		operationService.enterWorkingDayOperation(operation, operationType);
	}
}
