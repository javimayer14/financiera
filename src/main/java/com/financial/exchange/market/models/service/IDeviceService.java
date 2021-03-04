package com.financial.exchange.market.models.service;

import java.util.List;

import com.financial.exchange.market.models.entity.Device;

public interface IDeviceService {

	public List<Device> findAll();

	public Device findById(Long id);

	public Device save(Device device);
}
