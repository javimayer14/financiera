package com.financial.exchange.market.models.dto;

import java.io.Serializable;

import com.financial.exchange.market.models.entity.Account;

import lombok.Data;

@Data
public class ClientAccountDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String firstName;
	private String lastName;
	private Account account;

	public ClientAccountDTO(Long id, String firstName, String lastName, Account account) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.account = account;
	}

}
