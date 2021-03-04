package com.financial.exchange.market.models.service;

import java.util.List;

import com.financial.exchange.market.models.dto.AmountDto;
import com.financial.exchange.market.models.dto.CommissionDTO;
import com.financial.exchange.market.models.dto.CommissionPersonDTO;
import com.financial.exchange.market.models.entity.Account;
import com.financial.exchange.market.models.entity.Operation;
import com.financial.exchange.market.models.entity.Transaction;

public interface ITransactionService {

	public List<Transaction> findAll();

	public Transaction findById(Long id);

	public Transaction save(Transaction user);

	public void delete(Long id);

	public Float checkCommissionAmmount(CommissionDTO commission, Float operationAmount);

	public Float checkCommissionAmount(CommissionPersonDTO commission, Float operationAmount);

	public Transaction createTransaction(Float amount, Long type, Account clientAccount, Operation newOperation);

	public List<Transaction> createTransactions(AmountDto am, Long userDeliveryId, Long userEnterpriseId,
			Operation newOperation, Long accountType, Long operationType);
}
