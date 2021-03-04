package com.financial.exchange.market.models.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.financial.exchange.market.models.entity.Currency;
import com.financial.exchange.market.models.entity.OperationStatus;
import com.financial.exchange.market.models.entity.OperationType;

import lombok.Data;

@Data
public class OperationDetailDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private UserDTO broker;
	private AddressDTO address;
	private List<AmountDto> amounts;
	private OperationStatus operationStatus;
	private OperationType operationType;
	private Currency originCurrency;
	private Currency destinationCurrency;
	private Date timeSince;
	private Date timeUntil;
	private Float originAmount;
	private String notes;
	private Float ConvertionRate;
	private Float destinationAmount;
	private Integer priority;
	private Integer alert;
	private ExpenseOperationDTO expense;
	private ClientDTO originClient;
	private ClientDTO destinationClient;
	private Date createdDate;
	private Date modifiedDate;

}
