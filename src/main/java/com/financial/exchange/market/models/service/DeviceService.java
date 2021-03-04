package com.financial.exchange.market.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.financial.exchange.market.models.dao.IDeviceDao;
import com.financial.exchange.market.models.entity.Device;

@Service
public class DeviceService implements IDeviceService {

	@Autowired
	private IDeviceDao deviceDao;

	@Override
	@Transactional(readOnly = true)
	public List<Device> findAll() {
		return (List<Device>) deviceDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Device findById(Long id) {
		return deviceDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Device save(Device device) {
		return deviceDao.save(device);
	}

}
