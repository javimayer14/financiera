package com.financial.exchange.market.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.financial.exchange.market.models.entity.Currency;

public interface ICurrencyDao extends CrudRepository<Currency, Long> {

}
