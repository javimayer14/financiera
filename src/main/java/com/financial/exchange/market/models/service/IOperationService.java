package com.financial.exchange.market.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;

import com.financial.exchange.market.models.dto.OperationDetailDTO;
import com.financial.exchange.market.models.dto.OperationTransferDTO;
import com.financial.exchange.market.models.dto.OperationDto;
import com.financial.exchange.market.models.dto.TradingOperationDetailDTO;
import com.financial.exchange.market.models.dto.WorkingDayDTO;
import com.financial.exchange.market.models.entity.Operation;
import com.financial.exchange.market.models.entity.OperationStatus;
import com.financial.exchange.market.models.entity.OperationType;
import com.financial.exchange.market.models.entity.Transaction;

public interface IOperationService {

	public List<Operation> findAll();

	public Page<OperationDetailDTO> findAll(Pageable pageable);

	public OperationDetailDTO findAndMapById(Long id);

	public WorkingDayDTO findWorkingDayOperation(Long id);

	public TradingOperationDetailDTO findTradingOperation(Long id);

	public TradingOperationDetailDTO findPayChargeOperation(Long id);

	public OperationDetailDTO findExpenseOperation(Long id);

	public OperationTransferDTO findCableOperation(Long id);

	public Operation findById(Long id);

	public Operation save(Operation operation);

	public void delete(Long id);

	public OperationDto updateOperation(OperationDto operation);

	void save();

	public List<OperationDetailDTO> findAllActives();

	Page<OperationDetailDTO> findByVar(List<OperationStatus> estados, List<OperationType> tipos, Integer state,
			Long brokerId, Date desde, Date hasta, Float minOriginAmount, Float maxOriginAmount, Integer alert,
			List<GrantedAuthority> GT, Boolean onlyDelivery, Long idOperation, Pageable pageable);

	public Operation createCableOperation(Operation newOperation, Long operationType, List<Transaction> transactions,
			OperationDto operation);

	public void changePriority(List<Long> prioritys, Long brokerId);

	public List<Transaction> changeAccountsInTransactions(Operation currentOperation, Long userId, Long accountType);

	public void assignedOperation(OperationDto operation, Long accountType);

	public Operation createTradingOperation(Operation newOperation, OperationDto operation, Long operationType,
			List<Transaction> transactions);

	public Operation createWorkigDayOperation(Operation newOperation, Long operationType, List<Transaction> transactions,
			Long userBrokerId);

	public Operation createPayChargeOperation(Operation newOperation, OperationDto operation, Long operationType,
			List<Transaction> transactions);

	public Operation createExpenseOperation(Operation newOperation, OperationDto operation, Long operationType,
			List<Transaction> transactions);

	public void changeStatus(OperationDto operation);

	public void closeOperation(Long opearionId);

	public void enterTradingOperation(OperationDto operation, Long operationType);

	public void enterWorkingDayOperation(OperationDto operation, Long operationType);

	public void enterCableOperation(OperationDto operation, Long operationType);

	public void enterChargeOperation(OperationDto operation, Long operationType);

	public void enterPayOperation(OperationDto operation, Long operationType);

	public void enterExpenseOperation(OperationDto operation, Long operationType);

	public void cancelExpenseOperation(OperationDto operation);

	public Operation alertOperation(Long id);

	public void closeOperationRest(OperationDto operation);

	public Operation createRestOperation(Operation newOperation, Long operationType, List<Transaction> transactions);

	Page<OperationDetailDTO> findOperationsByAccount(Long id, List<OperationStatus> states, List<OperationType> types,
			Integer state, Long brokerId, Date desde, Date hasta, Float minOriginAmount, Float maxOriginAmount, Integer alert,
			Long idOperation, Pageable pageable);
}
