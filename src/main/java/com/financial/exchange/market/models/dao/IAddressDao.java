package com.financial.exchange.market.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.financial.exchange.market.models.entity.Address;

public interface IAddressDao extends CrudRepository<Address, Long> {

}
