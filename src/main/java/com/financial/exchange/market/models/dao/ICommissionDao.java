package com.financial.exchange.market.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.financial.exchange.market.models.entity.Commission;

public interface ICommissionDao extends CrudRepository<Commission, Long> {

}
