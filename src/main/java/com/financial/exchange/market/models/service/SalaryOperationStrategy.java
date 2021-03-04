package com.financial.exchange.market.models.service;

import com.financial.exchange.market.models.dto.OperationDto;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SalaryOperationStrategy extends AbstractWorkingDayOperation implements IOperationStrategy {

	@Override
	@Transactional
	public void save(OperationDto operation) {
		Long opearionId = operation.getId();
		String status = operation.getOperationStatus();
		switch (status) {
			case ENTERED:
				enterOperation(operation, EOperationType.SALARY.getState());
				break;
			case FINALIZED:
				closeOperation(opearionId);
				break;
			default:
				changeStatus(operation);
		}
	}
}
