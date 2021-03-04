package com.financial.exchange.market.models.dto;

import java.io.Serializable;
import java.util.Date;

import com.financial.exchange.market.models.entity.Currency;

import lombok.Data;

@Data
public class TradingOperationDetailDTO implements Serializable {

	private static final long serialVersionUID = 4184491449108995333L;

	private Long id;
	private BrokerDTO broker;
	private AddressDTO address;
	private Float ConvertionRate;
	private Float destinationAmount;
	private Float originAmount;
	private Currency originCurrency;
	private Currency destinationCurrency;
	private Date timeSince;
	private Date timeUntil;
	private Integer alert;
	private Integer priority;
	private String notes;
	private OperationTypeStatusDTO operationType;
	private OperationTypeStatusDTO operationStatus;
	private CommissionPersonDTO originCommissionAgent;
	private Date createdDate;
	private Date modifiedDate;
	private ClientDTO originClient;

}
