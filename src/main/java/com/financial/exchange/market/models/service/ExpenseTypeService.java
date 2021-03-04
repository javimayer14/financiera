package com.financial.exchange.market.models.service;

import java.util.ArrayList;
import java.util.List;

import com.financial.exchange.market.models.dao.IExpenseTypeDao;
import com.financial.exchange.market.models.dto.ExpenseTypeDTO;
import com.financial.exchange.market.models.entity.ExpenseType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpenseTypeService implements IExpenseTypeService {

    @Autowired
    IExpenseTypeDao expenseTypeDao;

    @Autowired
    private MapperService mapperService;

    @Override
    @Transactional(readOnly = true)
    public ExpenseTypeDTO findById(Long id) {
        ExpenseType expenseType = expenseTypeDao.findById(id).orElse(null);
        if (expenseType == null) {
            return null;
        }
        ExpenseTypeDTO dto = mapperService.mapExpenseTypeToDTO(expenseType);
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseType findEntityById(Long id) {
        return expenseTypeDao.findById(id).orElse(null);
    }

    @Override
    public List<ExpenseTypeDTO> findAll() {
        List<ExpenseType> expenseTypes = expenseTypeDao.findAll();
        List<ExpenseTypeDTO> dtos = mapExpenseTypesToDTO(expenseTypes);
        return dtos;
    }

    @Override
    public Page<ExpenseTypeDTO> findAll(Pageable pageable) {
        Page<ExpenseType> expenseTypes = expenseTypeDao.findAll(pageable);
        Page<ExpenseTypeDTO> dtos = expenseTypes.map(this::expenseTypeToDto);
        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExpenseTypeDTO> findByParam(String param, Pageable pageable) {
        Page<ExpenseType> expenseTypes = expenseTypeDao.findByParam(param, pageable);
        Page<ExpenseTypeDTO> dtos = expenseTypes.map(this::expenseTypeToDto);
        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseTypeDTO> findByDescription(String description) {
        List<ExpenseType> expenseTypes = expenseTypeDao.findByDescription(description);
        List<ExpenseTypeDTO> dtos = mapExpenseTypesToDTO(expenseTypes);
        return dtos;
    }

    private ExpenseTypeDTO expenseTypeToDto(ExpenseType expenseType) {
        return mapperService.modelMapper().map(expenseType, ExpenseTypeDTO.class);
    }

    private List<ExpenseTypeDTO> mapExpenseTypesToDTO(List<ExpenseType> expenseTypes) {
        List<ExpenseTypeDTO> expenseTypesDTO = new ArrayList<ExpenseTypeDTO>();
        for (ExpenseType o : expenseTypes) {
            expenseTypesDTO.add(mapperService.mapExpenseTypeToDTO(o));
        }
        return expenseTypesDTO;
    }

    @Override
    public ExpenseType save(ExpenseType expenseType) {
        return expenseTypeDao.save(expenseType);
    }

    @Override
    public ExpenseType update(ExpenseType currentType, ExpenseType expenseType) {
        currentType.setDescription(expenseType.getDescription());
        currentType.setEmployeeRequire(expenseType.getEmployeeRequire());
        return expenseTypeDao.save(currentType);
    }

    @Override
    public void delete(Long id) {
        expenseTypeDao.deleteById(id);
    }

}
