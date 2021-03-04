package com.financial.exchange.market.models.service;

import com.financial.exchange.market.models.entity.Account;

public interface IAccountService {

	public Account checkAccountUser(Account userAccount, Long userId, Long originCurrencyId, Long operationTypeId);

	public Account checkAccountClient(Account clientAccount, Long clientId, Long originCurrencyId,
			Long accountType);

	public Account findAccountUserById(Long idUser, Long idCurency, Long idType);
	
	public Account findAccountClientById(Long idClient, Long idCurency, Long idType);

	public Account save (Account account);
}
