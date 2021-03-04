package com.financial.exchange.market.models.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class AddressDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Integer number;
	private String floor;
	private String apt;
	private String notes;
	private String street;
	private String alias;
	private ClientDTO client;

}
