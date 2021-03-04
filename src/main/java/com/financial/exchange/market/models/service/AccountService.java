package com.financial.exchange.market.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.financial.exchange.market.models.dao.IAccountDao;
import com.financial.exchange.market.models.entity.Account;
import com.financial.exchange.market.models.entity.AccountType;
import com.financial.exchange.market.models.entity.Client;
import com.financial.exchange.market.models.entity.Currency;
import com.financial.exchange.market.models.entity.User;

@Service
public class AccountService implements IAccountService {

	@Autowired
	private IAccountDao accountDao;

	@Override
	public Account checkAccountClient(Account clientAccount, Long clientId, Long originCurrencyId,
			Long accountType) {
		if (clientAccount == null) {
			AccountType ac = new AccountType();
			ac.setId(accountType);
			Account account = new Account();
			Currency currency = new Currency();
			Client client = new Client();
			client.setId(clientId);
			currency.setId(originCurrencyId);
			account.setAccountType(ac);
			account.setClient(client);
			account.setState(EState.ACTIVE.getState());
			account.setBalance(0f);
			account.setCurrency(currency);
			return accountDao.save(account);
		} else {
			return clientAccount;
		}
	}

	@Override
	public Account checkAccountUser(Account userAccount, Long userId, Long originCurrencyId, Long accountType) {
		if (userAccount == null) {
			AccountType ac = new AccountType();
			ac.setId(accountType);
			Account account = new Account();
			Currency currency = new Currency();
			User user = new User();
			user.setId(userId);
			currency.setId(originCurrencyId);
			account.setAccountType(ac);
			account.setUser(user);
			account.setState(EState.ACTIVE.getState());
			account.setBalance(0f);
			account.setCurrency(currency);
			return accountDao.save(account);
		} else {
			return userAccount;
		}
	}

	@Override
	public Account findAccountUserById(Long idUser, Long idCurency, Long idType) {
		return accountDao.findAccountUserById(idUser, idCurency, idType);
	}

	@Override
	public Account save(Account account) {
		return accountDao.save(account);
	}

	@Override
	public Account findAccountClientById(Long idClient, Long idCurency, Long idType) {
		return accountDao.findAccountClientById(idClient, idCurency, idType);
	}

}
