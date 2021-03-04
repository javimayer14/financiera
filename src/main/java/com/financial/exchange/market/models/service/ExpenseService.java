package com.financial.exchange.market.models.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import com.financial.exchange.market.models.dao.IExpenseDao;
import com.financial.exchange.market.models.entity.Expense;
import com.financial.exchange.market.models.dto.ExpenseOperationDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpenseService implements IExpenseService {

    @Autowired
    private IExpenseDao expenseDao;

    @Autowired
    private MapperService mapperService;

    @Override
    public ExpenseOperationDTO findById(Long id) {
        Expense expense = expenseDao.findById(id).orElse(null);
        if (expense == null) {
            return null;
        }
        ExpenseOperationDTO dto = mapperService.mapExpenseOperationToDTO(expense);
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExpenseOperationDTO> findByParam(String param, Date dateSince, Date dateUntil, Float amountSince,
            Float amountUntil, Pageable pageable) {

        Date dateUntilF = null;
        if (dateUntil != null && dateUntil != dateUntilF) {
            dateUntilF = DateUtils.addDays(dateUntil, 1);
        }

        Page<Expense> expenses = expenseDao.findByParam(param, dateSince, dateUntilF, amountSince, amountUntil,
                pageable);
        Page<ExpenseOperationDTO> dtos = expenses.map(this::expenseToDto);
        return dtos;
    }

    @Override
    public List<ExpenseOperationDTO> findAll() {
        List<Expense> expenses = expenseDao.findAll();
        List<ExpenseOperationDTO> dtos = mapExpensesToDTO(expenses);
        return dtos;
    }

    @Override
    public Page<ExpenseOperationDTO> findAll(Pageable pageable) {
        Page<Expense> expenses = expenseDao.findAll(pageable);
        Page<ExpenseOperationDTO> dtos = expenses.map(this::expenseToDto);
        return dtos;
    }

    private ExpenseOperationDTO expenseToDto(Expense expense) {
        return mapperService.modelMapper().map(expense, ExpenseOperationDTO.class);
    }

    private List<ExpenseOperationDTO> mapExpensesToDTO(List<Expense> expenses) {
        List<ExpenseOperationDTO> expensesDTO = new ArrayList<ExpenseOperationDTO>();
        for (Expense o : expenses) {
            expensesDTO.add(mapperService.mapExpenseOperationToDTO(o));
        }
        return expensesDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseOperationDTO> findByDescription(String description) {
        List<Expense> expenses = expenseDao.findByDescription(description);
        List<ExpenseOperationDTO> dtos = mapExpensesToDTO(expenses);
        return dtos;
    }

    @Override
    public Expense save(Expense expense) {
        return expenseDao.save(expense);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub

    }

}
