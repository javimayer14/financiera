package com.financial.exchange.market.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.financial.exchange.market.models.dto.OperationDetailDTO;
import com.financial.exchange.market.models.dto.OperationDto;
import com.financial.exchange.market.models.dto.OperationTransferDTO;
import com.financial.exchange.market.models.dto.SearchOperationDTO;
import com.financial.exchange.market.models.dto.TradingOperationDetailDTO;
import com.financial.exchange.market.models.dto.WorkingDayDTO;
import com.financial.exchange.market.models.entity.Operation;
import com.financial.exchange.market.models.service.IOperationService;
import com.financial.exchange.market.models.service.IOperationStrategy;

@RestController
@RequestMapping("/api")
public class OperationRestController {

	@Autowired
	private IOperationService operationService;

	@Autowired
	Map<String, IOperationStrategy> operationStrategies = new HashMap<>();

	@GetMapping("/operations")
	public List<OperationDetailDTO> index() {
		return operationService.findAllActives();
	}

	@GetMapping("/operations/page/{page}/limit/{limit}")
	public Page<OperationDetailDTO> index(@PathVariable Integer page, @PathVariable Integer limit) {
		return operationService.findAll(PageRequest.of(page, limit));
	}

	@GetMapping("/operations/{id}")
	public OperationDetailDTO show(@PathVariable Long id) {
		return operationService.findAndMapById(id);
	}

	@PostMapping("/operations/accounts/{id}")
	public Page<OperationDetailDTO> showAccounts(@PathVariable Long id, @RequestBody SearchOperationDTO operation,
			Pageable pageRequest) {
		return operationService.findOperationsByAccount(id, operation.getOperationsStatus(),
				operation.getOperationType(), operation.getState(), operation.getBrokerId(), operation.getDesde(),
				operation.getHasta(), operation.getMinOriginAmount(), operation.getMaxOriginAmount(),
				operation.getAlert(),operation.getId(), pageRequest);
	}

	@ResponseStatus(HttpStatus.ACCEPTED)
	@PutMapping("/operations")
	public OperationDto updateOperation(@RequestBody OperationDto operation) {
		return operationService.updateOperation(operation);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/operations")
	public void save(@RequestBody OperationDto operation, @RequestParam(value = "type", required = false) String type) {
		operationStrategies.get(type).save(operation);
	}

	@PostMapping("/operations/search")
	public Page<OperationDetailDTO> search(@RequestBody SearchOperationDTO operation, Pageable pageRequest,
			Authentication authentication) {
		List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
		return operationService.findByVar(operation.getOperationsStatus(), operation.getOperationType(),
				operation.getState(), operation.getBrokerId(), operation.getDesde(), operation.getHasta(),
				operation.getMinOriginAmount(), operation.getMaxOriginAmount(), operation.getAlert(), authorities,
				operation.getOnlyDelivery(), operation.getId(), pageRequest);
	}

	@GetMapping("/operations/working-day/{id}")
	public WorkingDayDTO showWorkingDayOperation(@PathVariable Long id) {
		return operationService.findWorkingDayOperation(id);
	}

	@GetMapping("/operations/trading/{id}")
	public TradingOperationDetailDTO showTradingOperation(@PathVariable Long id) {
		return operationService.findTradingOperation(id);
	}

	@GetMapping("/operations/expense/{id}")
	public OperationDetailDTO showExpenseOperation(@PathVariable Long id) {
		return operationService.findExpenseOperation(id);
	}

	@GetMapping("/operations/pay-charge/{id}")
	public TradingOperationDetailDTO showPayChargeOperation(@PathVariable Long id) {
		return operationService.findPayChargeOperation(id);
	}

	@GetMapping("/operations/cable/{id}")
	public OperationTransferDTO showCableOperation(@PathVariable Long id) {
		return operationService.findCableOperation(id);
	}

	@ResponseStatus(HttpStatus.ACCEPTED)
	@PutMapping("/operations/{id}/alert")
	public Operation alertOperation(@PathVariable Long id) {
		return operationService.alertOperation(id);
	}

}
