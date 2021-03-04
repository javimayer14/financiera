package com.financial.exchange.market.models.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

import lombok.Data;

@Data
public class WorkingDayDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private BrokerDTO broker;
    private OperationTypeStatusDTO operationType;
    private OperationTypeStatusDTO operationStatus;
    private List<AmountDto> amounts;
    private Date createdDate;
    private Date modifiedDate;

}
