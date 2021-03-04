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

import com.financial.exchange.market.models.entity.PairQuote;
import com.financial.exchange.market.models.service.IPairQuoteService;

@RestController
@RequestMapping("/api")
public class PairQuoteRestController {

	@Autowired
	IPairQuoteService PairQuoteService;

	@GetMapping("/pairquotes")
	public List<PairQuote> index() {
		return PairQuoteService.findAll();
	}

	@GetMapping("/pairquotes/{id}")
	public PairQuote show(@PathVariable Long id) {
		return PairQuoteService.findById(id);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/pairquotes")
	public PairQuote create(@RequestBody PairQuote pairQuote) {
		return PairQuoteService.save(pairQuote);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("/pairquotes/{id}")
	public PairQuote update(@RequestBody PairQuote pairQuote, @PathVariable Long id) {
		PairQuote currentPairQuote = PairQuoteService.findById(id);
		currentPairQuote.setBid(pairQuote.getBid());
		currentPairQuote.setAsk(pairQuote.getAsk());
		return PairQuoteService.save(currentPairQuote);

	}
}
