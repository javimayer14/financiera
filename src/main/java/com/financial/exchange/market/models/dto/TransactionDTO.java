package com.financial.exchange.market.models.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class TransactionDTO {
    private Long id;
    private Long operationId;
    private OperationTypeStatusDTO operationType;
    private OperationTypeStatusDTO transactionType;
    private OperationTypeStatusDTO operationStatus;
    private Date modifiedDate;
    private Float amount;
    private Boolean isCommission;
    private List<AddressDTO> address;
    private BrokerDTO broker;
    private CurrencyDTO currency;
}
