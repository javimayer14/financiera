package com.financial.exchange.market.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.financial.exchange.market.models.dao.IPairQuoteDao;
import com.financial.exchange.market.models.entity.PairQuote;

@Service
public class PairQuoteService implements IPairQuoteService {

	@Autowired
	IPairQuoteDao pairQuoteDao;

	@Override
	@Transactional(readOnly = true)
	public List<PairQuote> findAll() {
		return (List<PairQuote>) pairQuoteDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public PairQuote findById(Long id) {
		return pairQuoteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public PairQuote save(PairQuote pairQuote) {
		return pairQuoteDao.save(pairQuote);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		pairQuoteDao.deleteById(id);
	}

}
