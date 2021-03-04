package com.financial.exchange.market.models.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.financial.exchange.market.models.dto.OperationDto;

@Component
public class ExpenseOperationStrategy extends AbstractTradingOperation implements IOperationStrategy {

	@Override
	@Transactional
	public void save(OperationDto operation) {

		String status = operation.getOperationStatus();
		switch (status) {
			case ENTERED:
				operationService.enterExpenseOperation(operation, EOperationType.EXPENSE.getState());
				break;
			case CANCELLED:
				operationService.cancelExpenseOperation(operation);
				break;
			default:
				changeStatus(operation);
		}
	}
}
