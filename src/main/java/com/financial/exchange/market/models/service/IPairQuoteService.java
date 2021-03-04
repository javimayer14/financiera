package com.financial.exchange.market.models.service;

import java.util.List;

import com.financial.exchange.market.models.entity.PairQuote;

public interface IPairQuoteService {

	public List<PairQuote> findAll();

	public PairQuote findById(Long id);

	public PairQuote save(PairQuote pairQuote);

	public void delete(Long id);
}
