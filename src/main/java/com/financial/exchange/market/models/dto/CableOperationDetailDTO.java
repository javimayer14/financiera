package com.financial.exchange.market.models.dto;

import lombok.Data;

@Data
public class CableOperationDetailDTO extends OperationDetailDTO{

	private static final long serialVersionUID = 6006332924678613151L;

	private ClientDTO originClient;
	private ClientDTO destinationClient;
	private UserDTO commissionAgentOne;
	private UserDTO commissionAgentTwo;
}
