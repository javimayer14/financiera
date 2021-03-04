package com.financial.exchange.market.models.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.financial.exchange.market.models.dto.OperationDto;

@Component
public class OpenOperationStrategy extends AbstractWorkingDayOperation implements IOperationStrategy {

	@Override
	@Transactional
	public void save(OperationDto operation) {
		Long opearionId = operation.getId();
		String status = operation.getOperationStatus();

		switch (status) {
		case ENTERED:
			enterOperation(operation, EOperationType.DAY_START.getState());
			break;
		case APPROVED:
			changeStatus(operation);
			break;
		case FINALIZED:
			closeOperation(opearionId);
			break;
		}
	}

}
