package com.financial.exchange.market.models.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.financial.exchange.market.models.dao.ITransactionDao;
import com.financial.exchange.market.models.dto.AmountDto;
import com.financial.exchange.market.models.dto.CommissionDTO;
import com.financial.exchange.market.models.dto.CommissionPersonDTO;
import com.financial.exchange.market.models.entity.Account;
import com.financial.exchange.market.models.entity.CommissionType;
import com.financial.exchange.market.models.entity.Operation;
import com.financial.exchange.market.models.entity.Transaction;
import com.financial.exchange.market.models.entity.TransactionStatus;
import com.financial.exchange.market.models.entity.TransactionType;

@Service
public class TransactionService implements ITransactionService {

	@Autowired
	private ITransactionDao transactionDao;

	@Autowired
	private IAccountService accountService;

	@Override
	public List<Transaction> findAll() {
		return (List<Transaction>) transactionDao.findAll();
	}

	@Override
	public Transaction findById(Long id) {
		return transactionDao.findById(id).orElse(null);
	}

	@Override
	public Transaction save(Transaction transaction) {
		return transactionDao.save(transaction);
	}

	@Override
	public void delete(Long id) {
		transactionDao.deleteById(id);
	}

	public Float checkCommissionAmmount(CommissionDTO commission, Float operationAmount) {
		Float comisionAmount = commission.getCommissionAmount();
		CommissionType type = commission.getCommissionType();
		if (ECommissionType.PORCENT.getValue() == type.getId()) {
			Float amountResult = (comisionAmount * operationAmount) / 100;
			comisionAmount = amountResult;
		}
		return comisionAmount;
	}

	public Float checkCommissionAmount(CommissionPersonDTO commission, Float operationAmount) {
		Float comisionAmount = commission.getCommissionAmount();
		CommissionType type = commission.getCommissionType();
		if (ECommissionType.PORCENT.getValue() == type.getId()) {
			Float amountResult = (comisionAmount * operationAmount) / 100;
			comisionAmount = amountResult;
		}
		return comisionAmount;
	}

	public List<Transaction> createTransactions(AmountDto am, Long userDeliveryId, Long userEnterpriseId,
			Operation newOperation, Long accountType, Long operationType) {
		List<Transaction> transactions = new ArrayList<>();
		Long idCurrency = am.getCurrencyId();
		Account deliveryAccount = accountService.findAccountUserById(userDeliveryId, am.getCurrencyId(),
				EAccountType.CASH.getState());
		Account enterpriseAccount = accountService.findAccountUserById(userEnterpriseId, am.getCurrencyId(),
				EAccountType.CASH.getState());
		Long type1 = ETransactionType.DISCHARGE.getValue();
		Long type2 = ETransactionType.ENTRY.getValue();
		if (operationType == EOperationType.DAY_START.getState()) {
			type1 = ETransactionType.ENTRY.getValue();
			type2 = ETransactionType.DISCHARGE.getValue();
		}
		transactions.add(createTransaction(am.getAmount(), type1,
				accountService.checkAccountUser(deliveryAccount, userDeliveryId, idCurrency, accountType), newOperation));
		transactions.add(createTransaction(am.getAmount(), type2,
				accountService.checkAccountUser(enterpriseAccount, userEnterpriseId, idCurrency, accountType), newOperation));

		return transactions;
	}

	public Transaction createTransaction(Float amount, Long type, Account account, Operation newOperation) {
		Transaction newTransaction = new Transaction();
		newTransaction.setOperation(newOperation);
		newTransaction.setAmount(amount);
		newTransaction.setAccount(account);
		newTransaction.setState(EState.ACTIVE.getState());
		newTransaction.setTransactionStatus(new TransactionStatus(ETransactionStatus.PENDING_ACCREDITATION.getValue()));
		newTransaction.setTransactionType(new TransactionType(type));
		return newTransaction;

	}
	

}
