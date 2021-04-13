package com.financial.exchange.market.models.dto;

import java.util.Date;
import java.util.List;

import com.financial.exchange.market.models.entity.OperationStatus;
import com.financial.exchange.market.models.entity.OperationType;

import lombok.Data;

@Data
public class SearchTransactionDto {
    Long id;
    Long accountId;
    List<OperationType> operationType;
    List<OperationStatus> operationStatus;
    Date timeSince;
    Date timeUntil;
    Float minAmount;
    Float maxAmount;
}