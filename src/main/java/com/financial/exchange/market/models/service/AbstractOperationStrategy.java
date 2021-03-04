package com.financial.exchange.market.models.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.financial.exchange.market.models.dao.IOperationDao;
import com.financial.exchange.market.models.dao.ITransactionDao;
import com.financial.exchange.market.models.dao.IUserDao;
import com.financial.exchange.market.models.dto.OperationDto;

public abstract class AbstractOperationStrategy {

	@Autowired
	public IAccountService accountService;

	@Autowired
	public IOperationDao operationDao;

	@Autowired
	public ITransactionDao transactionDao;

	@Autowired
	public IUserDao userDao;

	@Autowired
	public ITransactionService transactionService;

	@Autowired
	public IOperationService operationService;

	public Long operationType;
	public static final String ENTERED = "INGRESADO";
	public static final String ACCEPTED = "ACEPTADO";
	public static final String FINALIZED = "FINALIZADO";
	public static final String APPROVED = "APROBADO";
	public static final String ASSIGNED = "ASIGNADO";
	public static final String ARRIVED = "ARRIBADO";
	public static final String SUSPENDED = "SUSPENDIDO";
	public static final String CANCELLED = "CANCELADO";
	public static final String REJECTED = "RECHAZADO";

	public void changeStatus(OperationDto operation) {
		operationService.changeStatus(operation);
	}

	public void closeOperation(Long opearionId) {
		operationService.closeOperation(opearionId);

	}
	
	public void closeOperationRest(OperationDto operation) {
		operationService.closeOperationRest(operation);

	}

}
