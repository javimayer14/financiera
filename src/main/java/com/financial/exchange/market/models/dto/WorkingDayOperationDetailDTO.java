package com.financial.exchange.market.models.dto;

import java.util.List;

import lombok.Data;

@Data
public class WorkingDayOperationDetailDTO extends OperationDetailDTO{
	
	private List<AmountDto> amounts;

	private static final long serialVersionUID = 1L;

}
