package com.financial.exchange.market.models.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

import com.financial.exchange.market.models.entity.Account;
import com.financial.exchange.market.models.entity.Role;
import com.financial.exchange.market.models.entity.Address;

import lombok.Data;

@Data
public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String username;
	private String name;
	private String surname;

	private String password;

	private Integer state;
	private String dni;
	private String email;
	private String notes;

	private Date createdDate;
	private Date modifiedDate;
	private String createdBy;
	private String modifiedBy;

	private List<Account> accounts;
	private List<Role> roles;
	private List<Address> addresses;

}
