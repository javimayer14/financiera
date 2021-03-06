package com.financial.exchange.market.models.service;

import org.springframework.stereotype.Component;

import com.financial.exchange.market.models.dto.OperationDto;

@Component

public class CableOperationStrategy extends AbstractTradingOperation implements IOperationStrategy {

	@Override
	public void save(OperationDto operation) {
		Long opearionId = operation.getId();
		String status = operation.getOperationStatus();
		switch (status) {
			case ENTERED:
				operationService.enterCableOperation(operation, EOperationType.TRANSFER.getState());
				break;
			case FINALIZED:
				closeOperation(opearionId);
				break;
			case ASSIGNED:
				assignedOperation(operation, EAccountType.CASH.getState());
				break;
			default:
				changeStatus(operation);
		}
	}

}
