package com.financial.exchange.market.models.dto;

import java.io.Serializable;
import java.util.Date;

import com.financial.exchange.market.models.entity.Currency;

import lombok.Data;

@Data
public class OperationTransferDTO implements Serializable {

  private static final long serialVersionUID = 1L;
  private Long id;
  private Long enterpriseId;
  private Long brokerId;
  private Float originAmount;
  private Currency originCurrency;
  private String notes;
  private Integer alert;
  private Integer priority;
  private OperationTypeStatusDTO operationType;
  private OperationTypeStatusDTO operationStatus;
  private ClientDTO originClient;
  private ClientDTO destinationClient;
  private CommissionDTO originCommissionClient;
  private CommissionDTO destinationCommissionClient;
  private CommissionPersonDTO originCommissionAgent;
  private CommissionPersonDTO destinationCommissionAgent;
  private Date createdDate;
  private Date modifiedDate;

}