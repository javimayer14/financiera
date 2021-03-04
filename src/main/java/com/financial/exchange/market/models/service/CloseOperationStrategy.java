package com.financial.exchange.market.models.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.financial.exchange.market.models.dto.AmountDto;
import com.financial.exchange.market.models.dto.OperationDto;

@Component
public class CloseOperationStrategy extends AbstractWorkingDayOperation implements IOperationStrategy {

	@Override
	@Transactional
	public void save(OperationDto operation) {
		Long opearionId = operation.getId();
		String status = operation.getOperationStatus();
		List<AmountDto> rest = operation.getRestAmounts();

		switch (status) {
		case ENTERED:
			enterOperation(operation, EOperationType.DAY_CLOSE.getState());
			break;
		case APPROVED:
			changeStatus(operation);
			break;
		case FINALIZED:
			if(rest != null && !rest.isEmpty()) {
				closeOperationRest(operation);
			}else {
				closeOperation(opearionId);				
			}
			break;
		}
	}

}
