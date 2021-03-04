package com.financial.exchange.market.models.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.financial.exchange.market.models.dao.IClientDao;
import com.financial.exchange.market.models.dao.ICurrencyDao;
import com.financial.exchange.market.models.dao.IExpenseDao;
import com.financial.exchange.market.models.dao.IExpenseTypeDao;
import com.financial.exchange.market.models.dao.IOperationDao;
import com.financial.exchange.market.models.dao.ITransactionDao;
import com.financial.exchange.market.models.dao.IUserDao;
import com.financial.exchange.market.models.dto.AmountDto;
import com.financial.exchange.market.models.dto.CommissionDTO;
import com.financial.exchange.market.models.dto.CommissionPersonDTO;
import com.financial.exchange.market.models.dto.ExpenseOperationDTO;
import com.financial.exchange.market.models.dto.OperationDetailDTO;
import com.financial.exchange.market.models.dto.OperationDto;
import com.financial.exchange.market.models.dto.OperationTransferDTO;
import com.financial.exchange.market.models.dto.PersonDTO;
import com.financial.exchange.market.models.dto.TradingOperationDetailDTO;
import com.financial.exchange.market.models.dto.WorkingDayDTO;
import com.financial.exchange.market.models.entity.Account;
import com.financial.exchange.market.models.entity.Address;
import com.financial.exchange.market.models.entity.Client;
import com.financial.exchange.market.models.entity.CommissionType;
import com.financial.exchange.market.models.entity.Currency;
import com.financial.exchange.market.models.entity.Expense;
import com.financial.exchange.market.models.entity.ExpenseType;
import com.financial.exchange.market.models.entity.Operation;
import com.financial.exchange.market.models.entity.OperationStatus;
import com.financial.exchange.market.models.entity.OperationType;
import com.financial.exchange.market.models.entity.Transaction;
import com.financial.exchange.market.models.entity.TransactionStatus;
import com.financial.exchange.market.models.entity.User;

@Service
public class OperationService implements IOperationService {

	@Autowired
	private IUserDao userDao;
	@Autowired
	private IOperationDao operationDao;

	@Autowired
	private IExpenseDao expenseDao;

	@Autowired
	private IExpenseTypeDao expenseTypeDao;

	@Autowired
	private IClientDao clientDao;

	@Autowired
	private MapperService mapperService;

	@Autowired
	private IAccountService accountService;

	@Autowired
	public ITransactionDao transactionDao;

	@Autowired
	public ITransactionService transactionService;

	@Autowired
	public ICurrencyDao currencyDao;

	// Mensajes de excepciones
	private static final String CANT_BE_ZERO = "El monto de la operación no puede ser 0";
	private static final String OPERATION_NOT_FOUND = "No se encuentra la operacion con id ";
	private static final String MISSING_ID = "Falta id de operacion, exception: ";
	private static final String TRANSACTION_TYPE_ERROR = "	Error en el tipo de transaccion. Transaction id:";
	private static final String ROLE_TES = "ROLE_TESORERO";

	@Override
	public List<Operation> findAll() {
		return (List<Operation>) operationDao.findAll();
	}

	@Override
	public List<OperationDetailDTO> findAllActives() {
		List<Operation> operations = (List<Operation>) operationDao.findAll();
		List<OperationDetailDTO> operationsDTO = mapOperationsToDTO(operations);
		return operationsDTO;
	}

	@Override
	public Page<OperationDetailDTO> findAll(Pageable pageable) {
		Page<Operation> operations = operationDao.findAll(pageable);
		Page<OperationDetailDTO> dtos = operations.map(this::operationToDetailDto);
		return dtos;
	}

	private OperationDetailDTO operationToDetailDto(Operation operation) {
		return mapperService.modelMapper().map(operation, OperationDetailDTO.class);
	}

	@Override
	public Operation findById(Long id) {
		return operationDao.findById(id).orElse(null);
	}

	@Override
	public OperationDetailDTO findAndMapById(Long id) {
		Operation operation = operationDao.findById(id).orElse(null);
		OperationDetailDTO operationDetail = SelectMap(operation);
		return operationDetail;
	}

	@Override
	public void delete(Long id) {
		operationDao.deleteById(id);
	}

	private List<OperationDetailDTO> mapOperationsToDTO(List<Operation> operations) {
		List<OperationDetailDTO> operationsDTO = new ArrayList<OperationDetailDTO>();
		for (Operation o : operations) {
			operationsDTO.add(mapperService.mapOperationToDTO(o));
		}
		return operationsDTO;
	}

	private Page<OperationDetailDTO> mapOperationsPageToDTO(Page<Operation> operations) {
		List<OperationDetailDTO> operationsDTO = new ArrayList<OperationDetailDTO>();
		for (Operation o : operations) {
			operationsDTO.add(mapperService.mapOperationToDTO(o));
		}
		Page<OperationDetailDTO> page = new PageImpl<OperationDetailDTO>(operationsDTO);
		return page;
	}

	@Override
	public Page<OperationDetailDTO> findByVar(List<OperationStatus> states, List<OperationType> types, Integer state,
			Long brokerId, Date desde, Date hasta, Float minOriginAmount, Float maxOriginAmount, Integer alert,
			List<GrantedAuthority> authorities, Boolean onlyDelivery, Long idOperation, Pageable pageable) {
		Boolean onlyPresencial = null;
		
		if (onlyDelivery != null && onlyDelivery == true)
			onlyDelivery = true;
		else
			onlyDelivery = null;

		if (states == null || states.isEmpty())
			states = allStates();

		if (types == null || types.isEmpty())
			types = allTypes();

		if (isOnlyTeso(authorities) )
			onlyPresencial = true;

		Date fechaHasta = null;
		if (hasta != null && hasta != fechaHasta) {
			fechaHasta = DateUtils.addDays(hasta, 1);
		}
		Page<Operation> operations = operationDao.findOperationsByVar(states, types, state, brokerId, desde, fechaHasta,
				minOriginAmount, maxOriginAmount, alert,onlyPresencial, onlyDelivery, idOperation, pageable);
		Page<OperationDetailDTO> operationsDTO = operations.map(this::operationToDetailDto);

		return operationsDTO;
	}

	private Boolean isOnlyTeso(List<GrantedAuthority> authorities) {
		Boolean onlyOne = authorities.size() == 1;
		if (onlyOne) {
			for (GrantedAuthority gt : authorities) {
				if (gt.getAuthority().equals(ROLE_TES)) {
					return true;
				}
			}
		}
		return false;
	}

	public List<Transaction> changeAccountsInTransactions(Operation currentOperation, Long userId, Long accountType) {
		List<Transaction> transactions = currentOperation.getTransactions();
		for (Transaction t : transactions) {
			Account currentAcount = t.getAccount();
			Currency currentCurrency = currentAcount.getCurrency();
			Account newAccount = accountService.findAccountUserById(userId, currentCurrency.getId(), accountType);
			accountService.checkAccountUser(newAccount, userId, currentCurrency.getId(), accountType);
			User currentUser = currentAcount.getUser();
			if (currentUser != null) {
				t.setAccount(newAccount);

			}
		}
		return transactions;
	}

	public void changePriority(List<Long> prioritys, Long brokerId) {
		if (prioritys == null)
			return;
		List<Operation> operationsByBroker = operationDao.findByUser(brokerId);
		for (Operation o : operationsByBroker) {
			if (prioritys.contains(o.getId())) {
				Integer priority = prioritys.indexOf(o.getId());
				o.setPriority(priority + 1);
			}
		}
		operationDao.saveAll(operationsByBroker);
	}

	public Operation createCableOperation(Operation newOperation, Long operationType, List<Transaction> transactions,
			OperationDto operation) {

		checkOperationAmount(operation.getOriginAmount(), operation.getDestinationAmount());
		Currency originCurrency = new Currency();
		originCurrency.setId(operation.getOriginCurrencyId());
		newOperation.setOperationStatus(new OperationStatus(EOperationStatus.ENTERED.getStatus()));
		newOperation.setOperationType(new OperationType(operationType));
		newOperation.setState(EState.ACTIVE.getState());
		newOperation.setTransactions(transactions);
		newOperation.setDestinationAmount(operation.getDestinationAmount());
		newOperation.setOriginAmount(operation.getOriginAmount());
		newOperation.setOriginCurrency(originCurrency);
		newOperation.setPriority(operation.getPriority());
		newOperation.setNotes(operation.getNotes());
		newOperation.setOriginClient(this.getClientById(operation.getOriginClientId()));
		newOperation.setDestinationClient(this.getClientById(operation.getDestinationClientId()));

		Operation result = operationDao.save(newOperation);
		return result;
	}

	private List<OperationType> allTypes() {
		List<OperationType> types = new ArrayList<>();
		types.add(new OperationType(EOperationType.DAY_START.getState()));
		types.add(new OperationType(EOperationType.DAY_CLOSE.getState()));
		types.add(new OperationType(EOperationType.BUY_CURRENCY.getState()));
		types.add(new OperationType(EOperationType.SELL_CURRENCY.getState()));
		types.add(new OperationType(EOperationType.TRANSFER.getState()));
		types.add(new OperationType(EOperationType.PAYMENT.getState()));
		types.add(new OperationType(EOperationType.CHARGE.getState()));
		return types;
	}

	private List<OperationStatus> allStates() {
		List<OperationStatus> estados = new ArrayList<>();
		estados.add(new OperationStatus(EOperationStatus.ACCEPTED.getStatus()));
		estados.add(new OperationStatus(EOperationStatus.APPROVED.getStatus()));
		estados.add(new OperationStatus(EOperationStatus.ARRIVED.getStatus()));
		estados.add(new OperationStatus(EOperationStatus.ASSIGNED.getStatus()));
		estados.add(new OperationStatus(EOperationStatus.CANCELLED.getStatus()));
		estados.add(new OperationStatus(EOperationStatus.ENTERED.getStatus()));
		estados.add(new OperationStatus(EOperationStatus.FINISHED.getStatus()));
		estados.add(new OperationStatus(EOperationStatus.REJECTED.getStatus()));
		estados.add(new OperationStatus(EOperationStatus.SUSPENDED.getStatus()));
		return estados;
	}

	@Override
	public Page<OperationDetailDTO> findOperationsByAccount(Long id, List<OperationStatus> states,
			List<OperationType> types, Integer state, Long brokerId, Date desde, Date hasta, Float minOriginAmount,
			Float maxOriginAmount, Integer alert, Long idOperation, Pageable pageable) {
		
		if (states == null || states.isEmpty()) {
			states = allStates();
		}
		if (types == null || types.isEmpty()) {
			types = allTypes();
		}
		Date fechaHasta = null;
		if (hasta != null && hasta != fechaHasta) {
			fechaHasta = DateUtils.addDays(hasta, 1);
		}
		Page<Operation> operations = operationDao.findOperationsByAccount(id, states, types, state, brokerId, desde,
				fechaHasta, minOriginAmount, maxOriginAmount, alert,idOperation, pageable);
		Page<OperationDetailDTO> operationsDTO = operations.map(this::operationToDetailDto);

		return operationsDTO;
	}

	public void assignedOperation(OperationDto operation, Long accountType) {
		Long newBrokerId = operation.getBrokerId();
		Long opearionId = operation.getId();
		Long enterpriseId = operation.getEnterpriseId();
		List<Long> priorities = operation.getPriorities();
		try {
			Operation currentOperation = operationDao.findById(opearionId).orElse(null);
			if (currentOperation == null) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, OPERATION_NOT_FOUND + opearionId);
			}
			if (newBrokerId == null) {
				currentOperation.setOperationStatus(new OperationStatus(EOperationStatus.ACCEPTED.getStatus()));
				currentOperation.setBroker(null);
				List<Transaction> transactions = changeAccountsInTransactions(currentOperation, enterpriseId,
						accountType);
				currentOperation.setTransactions(transactions);
				operationDao.save(currentOperation);
			} else {
				User newBroker = userDao.findById(newBrokerId).orElse(null);
				currentOperation.setOperationStatus(new OperationStatus(EOperationStatus.ASSIGNED.getStatus()));
				currentOperation.setBroker(newBroker);
				List<Transaction> transactions = changeAccountsInTransactions(currentOperation, newBrokerId,
						accountType);
				currentOperation.setTransactions(transactions);
				operationDao.save(currentOperation);
				changePriority(priorities, newBrokerId);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MISSING_ID + e);
		}

	}

	@Override
	@Transactional
	public Operation createTradingOperation(Operation newOperation, OperationDto operation, Long operationType,
			List<Transaction> transactions) {
		if (operation.getBrokerId() != null) {
			User broker = userDao.findById(operation.getBrokerId()).orElse(null);
			newOperation.setBroker(broker);
		}
		if (operation.getEnterpriseId() != null) {
			User enterprise = userDao.findById(operation.getEnterpriseId()).orElse(null);
			newOperation.setEnterprise(enterprise);
		}
		Currency originCurrency = null;
		Currency destinationCurrency = null;

		if (operation.getOriginCurrencyId() != null) {
			originCurrency = currencyDao.findById(operation.getOriginCurrencyId()).orElse(null);
			newOperation.setOriginCurrency(originCurrency);
		}
		if (operation.getDestinationCurrencyId() != null) {
			destinationCurrency = currencyDao.findById(operation.getDestinationCurrencyId()).orElse(null);
			newOperation.setDestinationCurrency(destinationCurrency);
		}

		if (operation.getAddressId() != null) {
			Address address = new Address();
			address.setId(operation.getAddressId());
			newOperation.setAddress(address);

		}

		checkOperationAmount(operation.getOriginAmount(), operation.getDestinationAmount());

		newOperation.setOriginClient(this.getClientById(operation.getOriginClientId()));
		newOperation.setDestinationClient(this.getClientById(operation.getDestinationClientId()));

		newOperation.setDestinationAmount(operation.getDestinationAmount());
		newOperation.setOriginAmount(operation.getOriginAmount());
		newOperation.setOperationStatus(new OperationStatus(EOperationStatus.ENTERED.getStatus()));
		newOperation.setOperationType(new OperationType(operationType));
		newOperation.setState(EState.ACTIVE.getState());
		newOperation.setTransactions(transactions);
		newOperation.setPriority(operation.getPriority());
		newOperation.setConvertionRate(operation.getConvertionRate());
		newOperation.setTimeSince(operation.getTimeSince());
		newOperation.setTimeUntil(operation.getTimeUntil());
		newOperation.setNotes(operation.getNotes());

		Operation result = operationDao.save(newOperation);
		return result;
	}

	private void checkOperationAmount(Float originAmount, Float destinationAmount) {
		if (originAmount != null && originAmount == 0)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CANT_BE_ZERO);
		if (destinationAmount != null && destinationAmount == 0)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CANT_BE_ZERO);

	}

	public Operation createWorkigDayOperation(Operation newOperation, Long operationType,
			List<Transaction> transactions, Long userBrokerId) {
		User broker = userDao.findById(userBrokerId).orElse(null);
		newOperation.setOperationStatus(new OperationStatus(EOperationStatus.ENTERED.getStatus()));
		newOperation.setOperationType(new OperationType(operationType));
		newOperation.setBroker(broker);
		newOperation.setState(EState.ACTIVE.getState());
		newOperation.setTransactions(transactions);
		Operation result = operationDao.save(newOperation);
		return result;
	}

	public void changeStatus(OperationDto operation) {
		Long brokerId = operation.getBrokerId();
		Long opearionId = operation.getId();
		String status = operation.getOperationStatus();
		List<Long> priorities = operation.getPriorities();
		if (opearionId == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta id de operacion");
		Operation currentOperation = operationDao.findById(opearionId).orElse(null);
		if (currentOperation == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, OPERATION_NOT_FOUND + opearionId);
		}
		EOperationStatus parceledState = selectStatus(status);
		if (parceledState == null)
			return;
		OperationType ot = currentOperation.getOperationType();
		Boolean isBuySell = ot.getId() == EOperationType.BUY_CURRENCY.getState()
				|| ot.getId() == EOperationType.SELL_CURRENCY.getState();
		Boolean isAproved = parceledState.getStatus() == EOperationStatus.APPROVED.getStatus();
		if (isBuySell && isAproved)
			currentOperation.setBroker(null);
		currentOperation.setOperationStatus(new OperationStatus(parceledState.getStatus()));
		if (operation.getConvertionRate() != null) {
			currentOperation.setConvertionRate(operation.getConvertionRate());
		}
		changeAmounts(operation, currentOperation);
		changePriority(priorities, brokerId);
		operationDao.save(currentOperation);
	}

	public void closeOperation(Long opearionId) {
		Operation currentOperation = operationDao.findById(opearionId).orElse(null);
		if (currentOperation == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, OPERATION_NOT_FOUND + opearionId);
		}
		List<Transaction> transactions = transactionDao.findTransactionsByOperationId(opearionId);
		for (Transaction t : transactions) {
			Float ammount = t.getAmount();
			Long idTransactionType = t.getTransactionType().getId();
			Float mathOperation = null;
			Account userAccount = t.getAccount();
			Float balance = userAccount.getBalance();
			Boolean isTransactionEntry = (idTransactionType == ETransactionType.ENTRY.getValue());
			Boolean isTransactionDischarge = (idTransactionType == ETransactionType.DISCHARGE.getValue());
			if (isTransactionEntry)
				mathOperation = balance + ammount;
			else if (isTransactionDischarge)
				mathOperation = balance - ammount;
			else
				throw new ResponseStatusException(HttpStatus.CONFLICT, TRANSACTION_TYPE_ERROR + t.getId());
			userAccount.setBalance(mathOperation);
			accountService.save(userAccount);
			t.setTransactionStatus(new TransactionStatus(ETransactionStatus.ACCREDITED.getValue()));

		}
		currentOperation.setOperationStatus(new OperationStatus(EOperationStatus.FINISHED.getStatus()));
		transactionDao.saveAll(transactions);
		operationDao.save(currentOperation);
	}

	public void enterTradingOperation(OperationDto operation, Long operationType) {
		Long clientId = operation.getOriginClientId();
		Long userEnterpriseId = operation.getEnterpriseId();
		Long userBrokerId = operation.getBrokerId();
		Long accountType = EAccountType.CASH.getState();
		Operation newOperation = new Operation();
		List<Transaction> transactions = new ArrayList<>();
		Long originCurrencyId = operation.getOriginCurrencyId();
		Long destinationCurrencyId = operation.getDestinationCurrencyId();

		Float desAmount = operation.getDestinationAmount();

		if (userBrokerId == null)
			userBrokerId = userEnterpriseId;
		Account clientAccountOrigin = accountService.findAccountClientById(clientId, originCurrencyId, accountType);
		Account clientAccountDestination = accountService.findAccountClientById(clientId, destinationCurrencyId,
				accountType);
		Account userAccountOrigin = accountService.findAccountUserById(userBrokerId, originCurrencyId, accountType);
		Account userAccountDestination = accountService.findAccountUserById(userBrokerId, destinationCurrencyId,
				accountType);

		Account clientAccountOriginChecked = accountService.checkAccountClient(clientAccountOrigin, clientId,
				originCurrencyId, accountType);
		Account clientAccountDestinationChecked = accountService.checkAccountClient(clientAccountDestination, clientId,
				destinationCurrencyId, accountType);

		transactions.add(transactionService.createTransaction(operation.getOriginAmount(),
				ETransactionType.ENTRY.getValue(), clientAccountOriginChecked, newOperation));
		transactions.add(transactionService.createTransaction(desAmount, ETransactionType.DISCHARGE.getValue(),
				clientAccountDestinationChecked, newOperation));
		transactions.add(transactionService.createTransaction(desAmount, ETransactionType.ENTRY.getValue(),
				accountService.checkAccountUser(userAccountDestination, userBrokerId, destinationCurrencyId,
						accountType),
				newOperation));
		transactions.add(
				transactionService.createTransaction(operation.getOriginAmount(), ETransactionType.DISCHARGE.getValue(),
						accountService.checkAccountUser(userAccountOrigin, userBrokerId, originCurrencyId, accountType),
						newOperation));

		CommissionPersonDTO ageCommissionDTO = operation.getOriginCommissionAgent();
		if (ageCommissionDTO != null && ageCommissionDTO.getCommissionAmount() != null) {
			Float comOriginalAmount = ageCommissionDTO.getCommissionAmount();
			Float comAmount = comOriginalAmount;
			CommissionType comType = ageCommissionDTO.getCommissionType();
			if (comType.getId() == ECommissionType.PORCENT.getValue()) {
				comAmount = desAmount * comOriginalAmount / 100;
			}

			PersonDTO person = ageCommissionDTO.getPerson();
			if (person != null && person.getId() != null) {
				Long userAgent = person.getId();

				Account userAgentDestination = accountService.findAccountUserById(userAgent, destinationCurrencyId,
						accountType);

				Transaction comOut = transactionService.createTransaction(comAmount, ETransactionType.ENTRY.getValue(),
						accountService.checkAccountUser(userAgentDestination, userAgent, destinationCurrencyId,
								accountType),
						newOperation);

				comOut.setCommissionType(comType);
				comOut.setCommision(comOriginalAmount.toString());
				transactions.add(comOut);
			}

			Transaction comIn = transactionService.createTransaction(comAmount, ETransactionType.DISCHARGE.getValue(),
					clientAccountDestinationChecked, newOperation);

			comIn.setCommissionType(comType);
			comIn.setCommision(comOriginalAmount.toString());
			transactions.add(comIn);
		}
		createTradingOperation(newOperation, operation, operationType, transactions);

	}

	public void enterWorkingDayOperation(OperationDto operation, Long operationType) {
		List<Transaction> transactions = new ArrayList<>();
		Operation newOperation = new Operation();

		Long userDeliveryId = operation.getBrokerId();
		Long userEnterpriseId = operation.getEnterpriseId();
		for (AmountDto am : operation.getAmounts()) {
			if (am.getAmount() == 0 && operationType != EOperationType.DAY_CLOSE.getState())
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CANT_BE_ZERO);
			transactions.addAll(transactionService.createTransactions(am, userDeliveryId, userEnterpriseId,
					newOperation, EAccountType.CASH.getState(), operationType));

		}
		newOperation.setOriginClient(this.getClientById(operation.getOriginClientId()));
		newOperation.setDestinationClient(this.getClientById(operation.getDestinationClientId()));

		createWorkigDayOperation(newOperation, operationType, transactions, userDeliveryId);
	}

	public void enterCableOperation(OperationDto operation, Long operationType) {
		List<Transaction> transactions = new ArrayList<>();
		Operation newOperation = new Operation();
		Long accountType = EAccountType.CURRENT_ACCOUNT.getState();
		Long clientOneId = operation.getOriginClientId();
		Long clientTwoId = operation.getDestinationClientId();
		CommissionDTO commissionClientOrigin = operation.getOriginCommissionClient();
		CommissionDTO commissionClientDestination = operation.getDestinationCommissionClient();
		CommissionPersonDTO commissionAgentOne = operation.getOriginCommissionAgent();
		CommissionPersonDTO commissionAgentTwo = operation.getDestinationCommissionAgent();
		Long originCurrencyId = operation.getOriginCurrencyId();
		Long enterpriseId = operation.getEnterpriseId();

		Float commissionOneAmount = transactionService.checkCommissionAmmount(commissionClientOrigin,
				operation.getOriginAmount());
		Float commissionTwoAmount = transactionService.checkCommissionAmmount(commissionClientDestination,
				operation.getOriginAmount());

		Float comClientOriginAmount = commissionClientOrigin.getCommissionAmount();
		Float comClientDestinAmount = commissionClientDestination.getCommissionAmount();
		CommissionType comClientOriginType = commissionClientOrigin.getCommissionType();
		CommissionType comClientDestinType = commissionClientDestination.getCommissionType();

		Account enterpriseIdAccount = accountService.findAccountUserById(enterpriseId, originCurrencyId, accountType);
		Account enterpriseAccountChecked = accountService.checkAccountUser(enterpriseIdAccount, enterpriseId,
				originCurrencyId, accountType);

		Account clientAccountOrigin = accountService.findAccountClientById(clientOneId, originCurrencyId, accountType);
		Account clientAccountOriginChecked = accountService.checkAccountClient(clientAccountOrigin, clientOneId,
				originCurrencyId, accountType);

		Account clientAccountDestination = accountService.findAccountClientById(clientTwoId, originCurrencyId,
				accountType);
		Account clientAccountDestinationChecked = accountService.checkAccountClient(clientAccountDestination,
				clientTwoId, originCurrencyId, accountType);

		// Transacciones del monto de la operacion
		transactions.add(transactionService.createTransaction(operation.getOriginAmount(),
				ETransactionType.DISCHARGE.getValue(), clientAccountOriginChecked, newOperation));
		transactions.add(transactionService.createTransaction(operation.getOriginAmount(),
				ETransactionType.ENTRY.getValue(), clientAccountDestinationChecked, newOperation));

		// Transacciones por las comisiones fijas
		Transaction clientOriginTOut = transactionService.createTransaction(commissionOneAmount,
				ETransactionType.DISCHARGE.getValue(), clientAccountOriginChecked, newOperation);
		Transaction clientDestinationTOut = transactionService.createTransaction(commissionTwoAmount,
				ETransactionType.DISCHARGE.getValue(), clientAccountDestinationChecked, newOperation);
		Transaction clientOriginTIn = transactionService.createTransaction(commissionOneAmount,
				ETransactionType.ENTRY.getValue(), enterpriseAccountChecked, newOperation);
		Transaction clientDestinationTIn = transactionService.createTransaction(commissionTwoAmount,
				ETransactionType.ENTRY.getValue(), enterpriseAccountChecked, newOperation);

		clientOriginTOut.setCommissionType(comClientOriginType);
		clientOriginTOut.setCommision(comClientOriginAmount.toString());
		clientOriginTIn.setCommissionType(comClientOriginType);
		clientOriginTIn.setCommision(comClientOriginAmount.toString());

		clientDestinationTOut.setCommissionType(comClientDestinType);
		clientDestinationTOut.setCommision(comClientDestinAmount.toString());
		clientDestinationTIn.setCommissionType(comClientDestinType);
		clientDestinationTIn.setCommision(comClientDestinAmount.toString());

		transactions.add(clientOriginTOut);
		transactions.add(clientDestinationTOut);
		transactions.add(clientOriginTIn);
		transactions.add(clientDestinationTIn);

		// Transacciones por las comisiones de los comisionistas
		if (commissionAgentOne != null && commissionAgentOne.getPerson() != null) {
			PersonDTO agentOrigin = commissionAgentOne.getPerson();
			Long commissionAgenOnetId = agentOrigin.getId();

			Account commissionAgentAccountOrigin = accountService.findAccountUserById(commissionAgenOnetId,
					originCurrencyId, accountType);
			Account commissionAgentAccountOriginChecked = accountService.checkAccountUser(commissionAgentAccountOrigin,
					commissionAgenOnetId, originCurrencyId, accountType);

			Float commissionAgentOneAmount = transactionService.checkCommissionAmount(commissionAgentOne,
					operation.getOriginAmount());
			Transaction agentOriginTOut = transactionService.createTransaction(commissionAgentOneAmount,
					ETransactionType.DISCHARGE.getValue(), clientAccountOriginChecked, newOperation);
			Transaction agentOriginTIn = transactionService.createTransaction(commissionAgentOneAmount,
					ETransactionType.ENTRY.getValue(), commissionAgentAccountOriginChecked, newOperation);

			CommissionType comType = commissionAgentOne.getCommissionType();
			Float comAmount = commissionAgentOne.getCommissionAmount();

			agentOriginTOut.setCommissionType(comType);
			agentOriginTOut.setCommision(comAmount.toString());
			agentOriginTIn.setCommissionType(comType);
			agentOriginTIn.setCommision(comAmount.toString());
			transactions.add(agentOriginTOut);
			transactions.add(agentOriginTIn);
		}
		if (commissionAgentTwo != null && commissionAgentTwo.getPerson() != null) {
			PersonDTO agentDestination = commissionAgentTwo.getPerson();
			Long commissionAgenTwotId = agentDestination.getId();

			Account commissionAgentAccountDestination = accountService.findAccountUserById(commissionAgenTwotId,
					originCurrencyId, accountType);
			Account commissionAgentAccountDestinationChecked = accountService.checkAccountUser(
					commissionAgentAccountDestination, commissionAgenTwotId, originCurrencyId, accountType);

			Float commissionAgentTwoAmount = transactionService.checkCommissionAmount(commissionAgentTwo,
					operation.getOriginAmount());
			Transaction agentDestinationTOut = transactionService.createTransaction(commissionAgentTwoAmount,
					ETransactionType.DISCHARGE.getValue(), clientAccountDestinationChecked, newOperation);
			Transaction agentDestinationTIn = transactionService.createTransaction(commissionAgentTwoAmount,
					ETransactionType.ENTRY.getValue(), commissionAgentAccountDestinationChecked, newOperation);

			CommissionType comType = commissionAgentTwo.getCommissionType();
			Float comAmount = commissionAgentTwo.getCommissionAmount();

			agentDestinationTOut.setCommissionType(comType);
			agentDestinationTOut.setCommision(comAmount.toString());
			agentDestinationTIn.setCommissionType(comType);
			agentDestinationTIn.setCommision(comAmount.toString());
			transactions.add(agentDestinationTOut);
			transactions.add(agentDestinationTIn);
		}

		createCableOperation(newOperation, operationType, transactions, operation);

	}

	public Operation createPayChargeOperation(Operation newOperation, OperationDto operation, Long operationType,
			List<Transaction> transactions) {
		if (operation.getBrokerId() != null) {
			User broker = userDao.findById(operation.getBrokerId()).orElse(null);
			newOperation.setBroker(broker);
		}
		if (operation.getEnterpriseId() != null) {
			User enterprise = userDao.findById(operation.getEnterpriseId()).orElse(null);
			newOperation.setEnterprise(enterprise);
		}
		Currency originCurrency = new Currency();
		Currency destinationCurrency = new Currency();

		if (operation.getAddressId() != null) {
			Address address = new Address();
			address.setId(operation.getAddressId());
			newOperation.setAddress(address);

		}
		checkOperationAmount(operation.getOriginAmount(), operation.getDestinationAmount());

		destinationCurrency.setId(operation.getDestinationCurrencyId());
		originCurrency.setId(operation.getOriginCurrencyId());
		newOperation.setOriginAmount(operation.getOriginAmount());
		newOperation.setOriginCurrency(originCurrency);
		newOperation.setOperationStatus(new OperationStatus(EOperationStatus.ENTERED.getStatus()));
		newOperation.setOperationType(new OperationType(operationType));
		newOperation.setState(EState.ACTIVE.getState());
		newOperation.setTransactions(transactions);
		newOperation.setPriority(operation.getPriority());
		newOperation.setConvertionRate(operation.getConvertionRate());
		newOperation.setNotes(operation.getNotes());
		newOperation.setOriginClient(this.getClientById(operation.getOriginClientId()));
		newOperation.setDestinationClient(this.getClientById(operation.getDestinationClientId()));

		Operation result = operationDao.save(newOperation);
		return result;
	}

	public Operation createExpenseOperation(Operation newOperation, OperationDto operation, Long operationType,
			List<Transaction> transactions) {

		Expense newExpense = new Expense();

		if (operation.getBrokerId() != null) {
			User broker = userDao.findById(operation.getBrokerId()).orElse(null);
			newOperation.setBroker(broker);
			newExpense.setUser(broker);
		}
		if (operation.getEnterpriseId() != null) {
			User enterprise = userDao.findById(operation.getEnterpriseId()).orElse(null);
			newOperation.setEnterprise(enterprise);
		}
		Long expenseTypeId = operation.getSubTypeId();
		Float amount = operation.getOriginAmount();
		String notes = operation.getNotes();

		Currency originCurrency = new Currency();
		originCurrency.setId(operation.getOriginCurrencyId());

		newOperation.setOriginAmount(amount);
		newOperation.setOriginCurrency(originCurrency);
		newOperation.setOperationStatus(new OperationStatus(EOperationStatus.ENTERED.getStatus()));
		newOperation.setOperationType(new OperationType(operationType));
		newOperation.setState(EState.ACTIVE.getState());
		newOperation.setTransactions(transactions);
		newOperation.setPriority(operation.getPriority());
		newOperation.setNotes(notes);
		// newOperation.setExpense(newExpense);
		Operation result = operationDao.save(newOperation);

		ExpenseType expenseType = expenseTypeDao.findById(expenseTypeId).orElse(null);

		newExpense.setExpenseType(expenseType);
		newExpense.setDatePaid(operation.getDatePaid());
		newExpense.setTicketNumber(operation.getTicketNumber());
		newExpense.setAmount(amount);
		newExpense.setOperation(result);
		newExpense.setNotes(notes);

		Expense resultEx = expenseDao.save(newExpense);
		// result.setExpense(resultEx);
		Integer require = expenseType.getEmployeeRequire();

		if (!Integer.valueOf(1).equals(require)) {
			closeOperation(result.getId());
		}

		return result;
	}

	public void enterExpenseOperation(OperationDto operation, Long operationType) {

		Long userEnterpriseId = operation.getEnterpriseId();
		Long userBrokerId = operation.getBrokerId();
		Long accountType = EAccountType.CASH.getState();
		Operation newOperation = new Operation();
		List<Transaction> transactions = new ArrayList<>();
		Long originCurrencyId = operation.getOriginCurrencyId();
		Float originAmount = operation.getOriginAmount();

		Account userAccountOrigin = accountService.findAccountUserById(userEnterpriseId, originCurrencyId, accountType);

		if (operation.getBrokerId() != null) {
			Account userAccountDestination = accountService.findAccountUserById(userBrokerId, originCurrencyId,
					accountType);

			transactions.add(transactionService.createTransaction(originAmount, ETransactionType.ENTRY.getValue(),
					accountService.checkAccountUser(userAccountDestination, userBrokerId, originCurrencyId,
							accountType),
					newOperation));
		}

		transactions.add(transactionService.createTransaction(originAmount, ETransactionType.DISCHARGE.getValue(),
				accountService.checkAccountUser(userAccountOrigin, userEnterpriseId, originCurrencyId, accountType),
				newOperation));

		createExpenseOperation(newOperation, operation, operationType, transactions);
	}

	public void cancelExpenseOperation(OperationDto operation) {

		Long userEnterpriseId = operation.getEnterpriseId();
		Long userBrokerId = operation.getBrokerId();
		Long accountType = EAccountType.CASH.getState();
		Operation newOperation = new Operation();
		List<Transaction> transactions = new ArrayList<>();
		Long originCurrencyId = operation.getOriginCurrencyId();
		Float originAmount = operation.getOriginAmount() * -1;

		Account userAccountOrigin = accountService.findAccountUserById(userEnterpriseId, originCurrencyId, accountType);

		if (operation.getBrokerId() != null) {
			Account userAccountDestination = accountService.findAccountUserById(userBrokerId, originCurrencyId, accountType);

			transactions.add(transactionService.createTransaction(originAmount, ETransactionType.ENTRY.getValue(),
					accountService.checkAccountUser(userAccountDestination, userBrokerId, originCurrencyId, accountType),
					newOperation));
		}

		transactions.add(transactionService.createTransaction(originAmount, ETransactionType.DISCHARGE.getValue(),
				accountService.checkAccountUser(userAccountOrigin, userEnterpriseId, originCurrencyId, accountType),
				newOperation));

		Long operationId = operation.getId();
		Operation currentOperation = operationDao.findById(operationId).orElse(null);
		if (currentOperation == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, OPERATION_NOT_FOUND + operationId);
		}
		String status = operation.getOperationStatus();
		EOperationStatus parceledState = selectStatus(status);
		if (parceledState == null)
			return;

		currentOperation.setOperationStatus(new OperationStatus(parceledState.getStatus()));
		operationDao.save(currentOperation);

		operation.setSubTypeId(EOperationStatus.CANCELLED.getStatus());
		Operation cancelOperation = createExpenseOperation(newOperation, operation, EOperationType.EXPENSE.getState(),
				transactions);

		cancelOperation.setOperationStatus(new OperationStatus(parceledState.getStatus()));
		operationDao.save(cancelOperation);

	}

	public void enterChargeOperation(OperationDto operation, Long operationType) {
		Long clientId = operation.getOriginClientId();
		Long userEnterpriseId = operation.getEnterpriseId();
		Long userBrokerId = operation.getBrokerId();
		Long accountType = EAccountType.CURRENT_ACCOUNT.getState();
		Operation newOperation = new Operation();
		List<Transaction> transactions = new ArrayList<>();
		Long originCurrencyId = operation.getOriginCurrencyId();

		if (userBrokerId == null)
			userBrokerId = userEnterpriseId;
		Account clientAccountOrigin = accountService.findAccountClientById(clientId, originCurrencyId, accountType);

		Account userAccountOrigin = accountService.findAccountUserById(userBrokerId, originCurrencyId, accountType);

		transactions.add(
				transactionService.createTransaction(operation.getOriginAmount(), ETransactionType.DISCHARGE.getValue(),
						accountService.checkAccountClient(clientAccountOrigin, clientId, originCurrencyId, accountType),
						newOperation));

		transactions.add(
				transactionService.createTransaction(operation.getOriginAmount(), ETransactionType.ENTRY.getValue(),
						accountService.checkAccountUser(userAccountOrigin, userBrokerId, originCurrencyId, accountType),
						newOperation));
		createPayChargeOperation(newOperation, operation, operationType, transactions);

	}

	public void enterPayOperation(OperationDto operation, Long operationType) {
		Long clientId = operation.getOriginClientId();
		Long userEnterpriseId = operation.getEnterpriseId();
		Long userBrokerId = operation.getBrokerId();
		Long accountType = EAccountType.CURRENT_ACCOUNT.getState();
		Operation newOperation = new Operation();
		List<Transaction> transactions = new ArrayList<>();
		Long originCurrencyId = operation.getOriginCurrencyId();

		if (userBrokerId == null)
			userBrokerId = userEnterpriseId;
		Account clientAccountOrigin = accountService.findAccountClientById(clientId, originCurrencyId, accountType);

		Account userAccountOrigin = accountService.findAccountUserById(userBrokerId, originCurrencyId, accountType);

		transactions.add(
				transactionService.createTransaction(operation.getOriginAmount(), ETransactionType.ENTRY.getValue(),
						accountService.checkAccountClient(clientAccountOrigin, clientId, originCurrencyId, accountType),
						newOperation));

		transactions.add(
				transactionService.createTransaction(operation.getOriginAmount(), ETransactionType.DISCHARGE.getValue(),
						accountService.checkAccountUser(userAccountOrigin, userBrokerId, originCurrencyId, accountType),
						newOperation));
		createPayChargeOperation(newOperation, operation, operationType, transactions);

	}

	@Override
	public WorkingDayDTO findWorkingDayOperation(Long id) {
		Operation operation = operationDao.findById(id).orElse(null);
		if (operation == null || operation.getOperationType().getId() != EOperationType.DAY_START.getState()
				&& operation.getOperationType().getId() != EOperationType.DAY_CLOSE.getState()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, OPERATION_NOT_FOUND + id);
		}
		return mapperService.mapWorkingDayToDTO(operation);
	}

	@Override
	public TradingOperationDetailDTO findTradingOperation(Long id) {
		Operation operation = operationDao.findById(id).orElse(null);
		if (operation == null || operation.getOperationType().getId() != EOperationType.BUY_CURRENCY.getState()
				&& operation.getOperationType().getId() != EOperationType.SELL_CURRENCY.getState()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, OPERATION_NOT_FOUND + id);
		}

		TradingOperationDetailDTO operationDto = mapperService.mapTradingOperationToDTO(operation);

		List<Transaction> transactions = operation.getTransactions();
		Transaction traComm = transactions.stream()
				.filter((t) -> t.getCommissionType() != null
						&& t.getTransactionType().getId() == ETransactionType.ENTRY.getValue())
				.findFirst().orElse(null);

		if (traComm != null) {
			CommissionPersonDTO comDTO = new CommissionPersonDTO();
			CommissionType comType = new CommissionType();
			comType.setId(traComm.getCommissionType().getId());
			comType.setDescription(traComm.getCommissionType().getDescription());

			comDTO.setCommissionType(comType);
			comDTO.setCommissionAmount(Float.parseFloat(traComm.getCommision()));
			Account currentAcount = traComm.getAccount();
			User currentUser = currentAcount.getUser();
			if (currentUser != null) {
				PersonDTO person = mapperService.mapUserToPersonDTO(currentUser);
				comDTO.setPerson(person);
			}
			operationDto.setOriginCommissionAgent(comDTO);

		}

		return operationDto;
	}

	@Override
	public TradingOperationDetailDTO findPayChargeOperation(Long id) {
		Operation operation = operationDao.findById(id).orElse(null);
		if (operation == null || operation.getOperationType().getId() != EOperationType.PAYMENT.getState()
				&& operation.getOperationType().getId() != EOperationType.CHARGE.getState()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, OPERATION_NOT_FOUND + id);
		}
		return mapperService.mapTradingOperationToDTO(operation);
	}

	@Override
	public OperationDetailDTO findExpenseOperation(Long id) {
		Operation operation = operationDao.findById(id).orElse(null);
		if (operation == null || operation.getOperationType().getId() != EOperationType.EXPENSE.getState()
				&& operation.getOperationType().getId() != EOperationType.SALARY.getState()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, OPERATION_NOT_FOUND + id);
		}

		OperationDetailDTO operationDTO = mapperService.mapOperationToDTO(operation);
		ExpenseOperationDTO expenseDTO = mapperService.mapExpenseOperationToDTO(operation.getExpense());

		operationDTO.setExpense(expenseDTO);

		return operationDTO;
	}

	@Override
	public OperationTransferDTO findCableOperation(Long id) {
		Operation operation = operationDao.findById(id).orElse(null);
		if (operation == null || operation.getOperationType().getId() != EOperationType.TRANSFER.getState()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, OPERATION_NOT_FOUND + id);
		}

		List<Transaction> transactions = operation.getTransactions();

		List<Transaction> traComm = transactions.stream()
				.filter((t) -> t.getCommissionType() != null
						&& t.getTransactionType().getId() == ETransactionType.ENTRY.getValue())
				.collect(Collectors.toList());

		List<CommissionPersonDTO> list = new ArrayList<>();

		for (Transaction t : traComm) {
			if (t != null) {
				CommissionPersonDTO comDTO = new CommissionPersonDTO();
				CommissionType comType = new CommissionType();
				comType.setId(t.getCommissionType().getId());
				comType.setDescription(t.getCommissionType().getDescription());

				comDTO.setCommissionType(comType);
				comDTO.setCommissionAmount(Float.parseFloat(t.getCommision()));
				Account currentAcount = t.getAccount();
				// Client currentClient = currentAcount.getClient();
				User currentUser = currentAcount.getUser();
				if (currentUser != null && currentUser.getId() != null) {
					PersonDTO person = mapperService.mapUserToPersonDTO(currentUser);
					comDTO.setPerson(person);
					// } else {
					// if (currentClient != null && currentClient.getId() != null) {
					// PersonDTO person = mapperService.mapClientToPersonDTO(currentClient);
					// comDTO.setPerson(person);
					// }
				}
				list.add(comDTO);
			}
		}

		OperationTransferDTO dto = mapperService.modelMapper().map(operation, OperationTransferDTO.class);

		if (list != null && list.size() > 0) {
			CommissionPersonDTO clientOne = list.get(0);
			if (clientOne != null && clientOne.getCommissionType() != null) {
				CommissionDTO clientOneCom = new CommissionDTO();
				clientOneCom.setCommissionAmount(clientOne.getCommissionAmount());
				clientOneCom.setCommissionType(clientOne.getCommissionType());
				dto.setOriginCommissionClient(clientOneCom);
			}

			CommissionPersonDTO clientTwo = list.get(1);
			if (clientTwo != null && clientTwo.getCommissionType() != null) {
				CommissionDTO clientTwoCom = new CommissionDTO();
				clientTwoCom.setCommissionAmount(clientTwo.getCommissionAmount());
				clientTwoCom.setCommissionType(clientTwo.getCommissionType());
				dto.setDestinationCommissionClient(clientTwoCom);
			}

			CommissionPersonDTO agentOne = list.size() > 2 ? list.get(2) : null;
			if (agentOne != null && agentOne.getCommissionType() != null) {
				dto.setOriginCommissionAgent(agentOne);
			}

			CommissionPersonDTO agentTwo = list.size() > 3 ? list.get(3) : null;
			if (agentTwo != null && agentTwo.getCommissionType() != null) {
				dto.setDestinationCommissionAgent(agentOne);
			}
		}

		return dto;
	}

	@Override
	public Operation save(Operation operation) {
		return operationDao.save(operation);
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	private EOperationStatus selectStatus(String status) {
		switch (status) {
		case AbstractOperationStrategy.APPROVED:
			return EOperationStatus.APPROVED;
		case AbstractOperationStrategy.ARRIVED:
			return EOperationStatus.ARRIVED;
		case AbstractOperationStrategy.SUSPENDED:
			return EOperationStatus.SUSPENDED;
		case AbstractOperationStrategy.CANCELLED:
			return EOperationStatus.CANCELLED;
		case AbstractOperationStrategy.REJECTED:
			return EOperationStatus.REJECTED;
		case AbstractOperationStrategy.ACCEPTED:
			return EOperationStatus.ACCEPTED;

		}
		return null;
	}

	private OperationDetailDTO SelectMap(Operation operation) {

		OperationType operationType = operation.getOperationType();
		Long OperationTypeId = operationType.getId();
		OperationDetailDTO operationDetail = new OperationDetailDTO();

		if (OperationTypeId == EOperationType.DAY_START.getState())
			operationDetail = mapperService.mapGenericOperationToDTO(operation);

		else if (OperationTypeId == EOperationType.DAY_CLOSE.getState())
			operationDetail = mapperService.mapGenericOperationToDTO(operation);

		else if (OperationTypeId == EOperationType.BUY_CURRENCY.getState())
			operationDetail = mapperService.mapGenericOperationToDTO(operation);

		else if (OperationTypeId == EOperationType.SELL_CURRENCY.getState())
			operationDetail = mapperService.mapGenericOperationToDTO(operation);

		else if (OperationTypeId == EOperationType.TRANSFER.getState())
			operationDetail = mapperService.mapGenericOperationToDTO(operation);

		else if (OperationTypeId == EOperationType.EXPENSE.getState())
			operationDetail = mapperService.mapGenericOperationToDTO(operation);

		else if (OperationTypeId == EOperationType.SALARY.getState())
			operationDetail = mapperService.mapGenericOperationToDTO(operation);
		return operationDetail;
	}

	@Override
	public Operation alertOperation(Long id) {
		Operation op = operationDao.findById(id).orElse(null);
		if (op == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, OPERATION_NOT_FOUND + id);
		}
		op.setAlert(1);
		return operationDao.save(op);
	}

	private void changeAmounts(OperationDto operation, Operation currentOperation) {
		if (currentOperation.getOperationType().getId() == EOperationStatus.REJECTED.getStatus())
			return;
		List<Transaction> transactions = currentOperation.getTransactions();
		Boolean isBuySell = currentOperation.getConvertionRate() != null;
		Boolean isWorkingDay = currentOperation.getConvertionRate() == null;
		Float originAmount = operation.getOriginAmount();
		Float destinationAmount = operation.getDestinationAmount();
		currentOperation.setOriginAmount(originAmount);
		currentOperation.setDestinationAmount(destinationAmount);

		if (isBuySell) {
			for (Transaction t : transactions) {

				Account account = t.getAccount();

				if (account.getCurrency().getId() == operation.getOriginCurrencyId())
					t.setAmount(originAmount);
				else if ((account.getCurrency().getId() == operation.getDestinationCurrencyId()))
					t.setAmount(destinationAmount);
			}
		}
	}

	@Override
	@Transactional
	public OperationDto updateOperation(OperationDto operationDto) {
		Operation operation = operationDao.findById(operationDto.getId()).orElse(null);

		if (operation == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, OPERATION_NOT_FOUND + operationDto.getId());
		}

		List<Transaction> updatedTransactions = new ArrayList<>();

		for (Transaction t : operation.getTransactions()) {
			CommissionType comType = t.getCommissionType();
			if (comType != null && comType.getId() == ECommissionType.PORCENT.getValue()) {
				Float amountDes = operationDto.getDestinationAmount();
				Float amountPercent = Float.parseFloat(t.getCommision());
				Float amountF = amountPercent * amountDes / 100;
				t.setAmount(amountF);
			} else {
				Float amount = t.getAmount();
				Float amountOE = operation.getOriginAmount();
				Float amountODto = operationDto.getOriginAmount();
				Float amountDE = operation.getDestinationAmount();
				Float amountDDto = operationDto.getDestinationAmount();

				if (amount != null && amountOE != null && amount.equals(amountOE)) {
					t.setAmount(amountODto);
				} else if (amount != null && amountDE != null && amount.equals(amountDE)) {
					t.setAmount(amountDDto);
				}
			}

			updatedTransactions.add(t);
		}

		operation.setDestinationAmount(operationDto.getDestinationAmount());
		operation.setOriginAmount(operationDto.getOriginAmount());
		operation.setConvertionRate(operationDto.getConvertionRate());
		operation.setAlert(operationDto.getAlert());
		operation.setTransactions(updatedTransactions);

		Operation updatedOperation = operationDao.save(operation);

		return mapperService.modelMapper().map(updatedOperation, OperationDto.class);
	}

	@Override
	public void closeOperationRest(OperationDto operation) {
		Long operationId = operation.getId();
		closeOperation(operationId);
		Operation operationResult = enterRestOperation(operation, EOperationType.REST.getState());
		closeOperation(operationResult.getId());

	}

	public Operation enterRestOperation(OperationDto operation, Long operationType) {
		Long accountType = EAccountType.CASH.getState();
		List<Transaction> transactions = new ArrayList<>();
		Operation newOperation = new Operation();
		Long userEnterpriseId = operation.getEnterpriseId();
		for (AmountDto am : operation.getRestAmounts()) {
			Account enterpriseAccount = accountService.findAccountUserById(userEnterpriseId, am.getCurrencyId(),
					accountType);
			Account enterpriseAccountResult = accountService.checkAccountUser(enterpriseAccount, userEnterpriseId,
					am.getCurrencyId(), accountType);

			transactions.add(transactionService.createTransaction(am.getAmount(), ETransactionType.ENTRY.getValue(),
					enterpriseAccountResult, newOperation));
		}
		return createRestOperation(newOperation, operationType, transactions);
	}

	public Operation createRestOperation(Operation newOperation, Long operationType, List<Transaction> transactions) {
		newOperation.setOperationStatus(new OperationStatus(EOperationStatus.ENTERED.getStatus()));
		newOperation.setOperationType(new OperationType(operationType));
		newOperation.setState(EState.ACTIVE.getState());
		newOperation.setTransactions(transactions);
		Operation result = operationDao.save(newOperation);
		return result;
	}

	private Client getClientById(Long clientId) {
		Client client = null;
		if (clientId != null) {
			client = clientDao.findById(clientId).orElse(null);
		}
		return client;
	}

}
