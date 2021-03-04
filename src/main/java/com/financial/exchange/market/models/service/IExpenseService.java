package com.financial.exchange.market.models.service;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.financial.exchange.market.models.dto.ExpenseOperationDTO;
import com.financial.exchange.market.models.entity.Expense;

public interface IExpenseService {

    public ExpenseOperationDTO findById(Long id);

    public List<ExpenseOperationDTO> findAll();

    public Page<ExpenseOperationDTO> findByParam(String param, Date dateSince, Date dateUntil, Float amountSince,
            Float amountUntil, Pageable pageable);

    public Page<ExpenseOperationDTO> findAll(Pageable pageable);

    public List<ExpenseOperationDTO> findByDescription(String description);

    public Expense save(Expense expense);

    public void delete(Long id);

}
