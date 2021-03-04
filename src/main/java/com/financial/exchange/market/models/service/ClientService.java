package com.financial.exchange.market.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.financial.exchange.market.models.dao.IAccountDao;
import com.financial.exchange.market.models.dao.IClientDao;
import com.financial.exchange.market.models.dto.ClientAccountDTO;
import com.financial.exchange.market.models.entity.Account;
import com.financial.exchange.market.models.entity.Address;
import com.financial.exchange.market.models.entity.Client;
import com.financial.exchange.market.models.entity.Phone;

@Service
public class ClientService implements IClientService {

	@Autowired
	IClientDao clientDao;

	@Autowired
	private IAccountDao accountDao;

	@Override
	@Transactional(readOnly = true)
	public List<Client> findAll() {
		return (List<Client>) clientDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Client findById(Long id) {
		return clientDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Client save(Client client) {
		Client newClient = new Client();
		setClientAddresses(newClient, client);
		setClientPhones(newClient, client);
		newClient.setAlias(client.getAlias());
		newClient.setFirstName(client.getFirstName());
		newClient.setLastName(client.getLastName());
		newClient.setNotes(client.getNotes());
		newClient.setState(EState.ACTIVE.getState());
		return clientDao.save(newClient);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		clientDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Account> findAllAccountClientById(Long id) {
		List<Account> accountList = accountDao.findAllAccountClientById(id);
		return accountList;
	}

	@Override
	public Account findAccountClientById(Long idClient, Long idAccount) {
		Client currentClient = clientDao.findById(idClient).orElse(null);
		List<Account> accountList = accountDao.findAllAccountClientById(idClient);
		Account currentAccount = accountDao.findById(idAccount).orElse(null);
		if (currentClient == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el cliente con id " + idClient);
		if (!accountList.contains(currentAccount))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"No existe una cuenta con id " + idAccount + " para el cliente con id " + idClient);
		return currentAccount;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Client> findClientByVar(String var, Pageable pageable) {
		return clientDao.findClientsbyVar(var, pageable);
	}

	private void setClientAddresses(Client currentClient, Client client) {
		List<Address> newAddresses = client.getAddress();
		for (Address a : newAddresses) {
			a.setClient(currentClient);
		}
		if (!newAddresses.isEmpty() && newAddresses != null) {
			if (currentClient.getAddress() != null) {
				currentClient.getAddress().clear();
				currentClient.getAddress().addAll(newAddresses);
			} else {
				currentClient.setAddress(newAddresses);
			}
		}
	}

	private void setClientPhones(Client currentClient, Client client) {
		List<Phone> newPhones = client.getPhones();
		for (Phone p : newPhones) {
			p.setClient(currentClient);
		}
		if (!newPhones.isEmpty() && newPhones != null) {
			if (currentClient.getPhones() != null) {
				currentClient.getPhones().clear();
				currentClient.getPhones().addAll(newPhones);

			} else {
				currentClient.setPhones(newPhones);
			}
		}
	}

	@Override
	public Client update(Client currentClient, Client client) {
		setClientAddresses(currentClient, client);
		setClientPhones(currentClient, client);
		currentClient.setAlias(client.getAlias());
		currentClient.setFirstName(client.getFirstName());
		currentClient.setLastName(client.getLastName());
		currentClient.setNotes(client.getNotes());
		return clientDao.save(currentClient);
	}
	
	public void updateState(Client currentClient) {
		 clientDao.save(currentClient);
	}

	@Override
	public List<ClientAccountDTO> findDebtorsCreditors(Long idCurency) {
		List<ClientAccountDTO> result = accountDao.findAccountCreditorDebtor(idCurency);
		return result;
	}
}
