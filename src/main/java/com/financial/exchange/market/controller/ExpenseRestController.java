package com.financial.exchange.market.controller;

import java.util.Date;
import java.util.List;

import com.financial.exchange.market.models.dto.ExpenseOperationDTO;
import com.financial.exchange.market.models.dto.ExpenseTypeDTO;
import com.financial.exchange.market.models.entity.ExpenseType;
import com.financial.exchange.market.models.service.IExpenseService;
import com.financial.exchange.market.models.service.IExpenseTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api")
public class ExpenseRestController {

    @Autowired
    IExpenseService expenseService;

    @Autowired
    IExpenseTypeService expenseTypeService;

    @GetMapping("expenses")
    public List<ExpenseOperationDTO> list(@RequestParam(value = "description", required = false) String description) {
        return expenseService.findByDescription(description);
    }

    @GetMapping("/expenses/filter/page/{page}/limit/{limit}")
    public Page<ExpenseOperationDTO> listByType(@RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "dateSince", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateSince,
            @RequestParam(value = "dateUntil", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateUntil,
            @RequestParam(value = "amountSince", required = false) Float amountSince,
            @RequestParam(value = "amountUntil", required = false) Float amountUntil, @PathVariable Integer page,
            @PathVariable Integer limit) {
        return expenseService.findByParam(type, dateSince, dateUntil, amountSince, amountUntil,
                PageRequest.of(page, limit));
    }

    @GetMapping("expenses/{id}")
    public ExpenseOperationDTO getById(@PathVariable Long id) {
        ExpenseOperationDTO expense = expenseService.findById(id);
        if (expense == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra el gasto con id " + id);
        }
        return expense;
    }

    @GetMapping("/expenses/type/filter/page/{page}/limit/{limit}")
    public Page<ExpenseTypeDTO> find(@RequestParam(value = "var", required = false) String var,
            @PathVariable Integer page, @PathVariable Integer limit) {
        return expenseTypeService.findByParam(var, PageRequest.of(page, limit));
    }

    @GetMapping("expenses/type")
    public List<ExpenseTypeDTO> getByDescription(
            @RequestParam(value = "description", required = false) String description) {
        return expenseTypeService.findByDescription(description);
    }

    @GetMapping("expenses/type/{id}")
    public ExpenseTypeDTO getTypeById(@PathVariable Long id) {
        ExpenseTypeDTO currentType = expenseTypeService.findById(id);
        if (currentType == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra el tipo de gasto con id " + id);
        }
        return currentType;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/expenses/type")
    public ExpenseType createExpenseType(@RequestBody ExpenseType expenseType) {
        return expenseTypeService.save(expenseType);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("expenses/type/{id}")
    public ExpenseType updateType(@RequestBody ExpenseType expenseType, @PathVariable Long id) {
        ExpenseType currentType = expenseTypeService.findEntityById(id);
        if (currentType == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra el tipo de gasto con id " + id);
        }
        return expenseTypeService.update(currentType, expenseType);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("expenses/type/{id}")
    public void destroy(@PathVariable Long id) {
        ExpenseType currentType = expenseTypeService.findEntityById(id);
        if (currentType == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra el tipo de gasto con id " + id);
        }
        if (currentType.getExpenses().size() > 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No es posible eliminar el tipo de gasto con id " + id + " el mismo posee gastos asociados");
        }
        expenseTypeService.delete(id);
    }

}
