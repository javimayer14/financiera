package com.financial.exchange.market.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.financial.exchange.market.models.dto.ClientAccountDTO;
import com.financial.exchange.market.models.entity.Account;
import com.financial.exchange.market.models.entity.Client;
import com.financial.exchange.market.models.service.IClientService;

@RestController
@RequestMapping("/api")
public class ClientRestController {

	public static final Integer ESTADO_ACTIVO = 1;
	public static final Integer ESTADO_INACTIVO = 0;

	@Autowired
	IClientService clientService;

	@GetMapping("/clients")
	public List<Client> index() {
		return clientService.findAll();
	}

	@GetMapping("/clients/{id}")
	public Client show(@PathVariable Long id) {
		return clientService.findById(id);
	}
	
	@GetMapping("/clients/debtors")
	public List<ClientAccountDTO> showDebtorsCreditors(@RequestParam(value = "currency", required = false) Long currency) {
		return clientService.findDebtorsCreditors(currency);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/clients")
	public Client create(@RequestBody Client client) {
		return clientService.save(client);
	}

	@PutMapping("clients/{id}")
	public Client update(@RequestBody Client client, @PathVariable Long id) {
		Client currentClient = clientService.findById(id);
		if (currentClient == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra el usuario con id " + id);
		}

		return clientService.update(currentClient, client);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("clients-state/{id}")
	public void destroy(@RequestBody Client client, @PathVariable Long id) {
		Client currentClient = clientService.findById(id);
		if (currentClient == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra el usuario con id " + id);
		}
		if (client.getState() == ESTADO_INACTIVO)
			currentClient.setState(ESTADO_INACTIVO);
		if (client.getState() == ESTADO_ACTIVO)
			currentClient.setState(ESTADO_ACTIVO);
		clientService.updateState(currentClient);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/clients/{id}")
	public void destroy(@PathVariable Long id) {
		clientService.delete(id);

	}

	@GetMapping("/clients/{id}/accounts")
	public List<Account> accountUser(@PathVariable Long id) {
		return clientService.findAllAccountClientById(id);
	}

	@GetMapping("/clients/{idClient}/accounts/{idAcount}")
	public Account userAccount(@PathVariable Long idClient, @PathVariable Long idAcount) {
		return clientService.findAccountClientById(idClient, idAcount);
	}

	@GetMapping("/clients/filter/page/{page}/limit/{limit}")
	public Page<Client> find(@RequestParam(value = "var", required = false) String var, @PathVariable Integer page, @PathVariable Integer limit) {
		return clientService.findClientByVar(var, PageRequest.of(page, limit));
	}

}
