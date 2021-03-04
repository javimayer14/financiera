package com.financial.exchange.market.models.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.financial.exchange.market.models.entity.ExpenseType;
import com.financial.exchange.market.models.dto.ExpenseTypeDTO;

public interface IExpenseTypeService {

    public ExpenseTypeDTO findById(Long id);

    public ExpenseType findEntityById(Long id);

    public List<ExpenseTypeDTO> findAll();

    public Page<ExpenseTypeDTO> findAll(Pageable pageable);

    public Page<ExpenseTypeDTO> findByParam(String param, Pageable pageable);

    public List<ExpenseTypeDTO> findByDescription(String description);

    public ExpenseType save(ExpenseType expenseType);

    public ExpenseType update(ExpenseType currentType, ExpenseType expenseType);

    public void delete(Long id);
}
