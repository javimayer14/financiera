package com.financial.exchange.market.models.service;

import com.financial.exchange.market.models.dto.ExpenseOperationDTO;

public interface IExpenseOperationStrategy {
    void save(ExpenseOperationDTO operation);
}
