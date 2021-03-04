package com.financial.exchange.market.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.financial.exchange.market.models.entity.PairQuote;

public interface IPairQuoteDao extends CrudRepository<PairQuote, Long>{

}
