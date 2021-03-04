package com.financial.exchange.market.models.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class OperationDto implements Serializable {

	private static final long serialVersionUID = 1148064726943477637L;
	private Long id;
	private Long enterpriseId;
	private Long brokerId;
	private List<AmountDto> amounts;
	private Float destinationAmount;
	private Float originAmount;
	private Float convertionRate;
	private String notes;
	private String lastUpdateDate;
	public String operationStatus;
	public Integer operationStatusId;
	public Integer priority;
	private Integer alert;
	private List<Long> priorities;
	private Long addressId;
	private Date timeSince;
	private Date timeUntil;
	private Long originCurrencyId;
	private Long destinationCurrencyId;
	private CommissionDTO originCommissionClient;
	private CommissionDTO destinationCommissionClient;
	private CommissionPersonDTO originCommissionAgent;
	private CommissionPersonDTO destinationCommissionAgent;
	private Date datePaid;
	private String ticketNumber;
	private Long subTypeId;
	private Date createdDate;
	private Date modifiedDate;
	private List<AmountDto> restAmounts;
	private Long originClientId;
	private Long destinationClientId;

}
