package com.financial.exchange.market.models.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class ClientDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String firstName;
	private String lastName;
	private String notes;
	private Date createdDate;
	private Date modifiedDate;
}
