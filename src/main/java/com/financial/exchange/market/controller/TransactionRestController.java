package com.financial.exchange.market.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.financial.exchange.market.models.entity.Transaction;
import com.financial.exchange.market.models.service.ITransactionService;

@RestController
@RequestMapping("/api")
public class TransactionRestController {

	@Autowired
	ITransactionService transactionService;

	@GetMapping("/transactions")
	public List<Transaction> index() {
		return transactionService.findAll();
	}

	@GetMapping("transactions/{id}")
	public Transaction show(@PathVariable Long id) {
		return transactionService.findById(id);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/transactions")
	public Transaction create (@RequestBody Transaction transaction) {
		return transactionService.save(transaction);
		
	}
	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("/transactions/{id}")
	public Transaction update (@RequestBody Transaction transaction, @PathVariable Long id) {
		Transaction currentTransaction = transactionService.findById(id);
		return currentTransaction;
	}
	
	

}
