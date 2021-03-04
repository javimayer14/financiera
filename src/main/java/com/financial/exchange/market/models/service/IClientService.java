package com.financial.exchange.market.models.service;

import java.util.List;

import com.financial.exchange.market.models.dto.ClientAccountDTO;
import com.financial.exchange.market.models.entity.Account;
import com.financial.exchange.market.models.entity.Client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IClientService {

	public List<Client> findAll();

	public Client findById(Long id);

	public Client save(Client client);

	public void delete(Long id);

	public List<Account> findAllAccountClientById(Long id);

	public Page<Client> findClientByVar(String var, Pageable pageable);

	public Account findAccountClientById(Long idClient, Long idAccount);

	public Client update(Client currentClient, Client client);

	public List<ClientAccountDTO> findDebtorsCreditors(Long idCurency);

	public void updateState(Client currentClient);

}
