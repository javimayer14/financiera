package com.financial.exchange.market.models.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.financial.exchange.market.models.dto.OperationDto;

@Component
public class BuyOperationStrategy extends AbstractTradingOperation implements IOperationStrategy {

	@Override
	@Transactional
	public void save(OperationDto operation) {
		Long opearionId = operation.getId();
		String status = operation.getOperationStatus();
		switch (status) {
		case ENTERED:
			enterOperation(operation, EOperationType.BUY_CURRENCY.getState());
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
