package com.financial.exchange.market.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.financial.exchange.market.models.entity.Device;
import com.financial.exchange.market.models.service.IDeviceService;

@RestController
@RequestMapping("/api")
public class DeviceRestController {

	@Autowired
	private IDeviceService deviceService;

	@GetMapping("/devices")
	public List<Device> index() {
		return deviceService.findAll();
	}

	@GetMapping("/devices/{id}")
	public Device show(@PathVariable Long id) {
		return deviceService.findById(id);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/devices")
	public Device create(@RequestBody Device user) {
		return deviceService.save(user);
	}
}
