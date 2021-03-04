package com.financial.exchange.market.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.financial.exchange.market.models.entity.Device;

public interface IDeviceDao extends CrudRepository<Device, Long> {

}
