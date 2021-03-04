package com.financial.exchange.market.models.dto;

import java.io.Serializable;
import java.util.Date;

import com.financial.exchange.market.models.entity.OperationStatus;

import lombok.Data;

@Data
public class ExpenseOperationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long operationId;
    private Long expenseTypeId;
    private String ticketNumber;
    private Long originCurrencyId;
    private Float originAmount;
    private String notes;
    private Date datePaid;
    private ExpenseTypeDTO expenseType;
    private OperationStatus operationStatus;

    private UserDTO user;

    private Float amount;
}
