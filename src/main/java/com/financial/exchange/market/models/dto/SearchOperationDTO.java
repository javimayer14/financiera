package com.financial.exchange.market.models.dto;

import java.util.Date;
import java.util.List;

import com.financial.exchange.market.models.entity.OperationStatus;
import com.financial.exchange.market.models.entity.OperationType;

import lombok.Data;

@Data
public class SearchOperationDTO {
	Long id;
	List<OperationStatus> operationsStatus;
	List<OperationType> operationType;
	Date desde;
	Date hasta;
	Integer state;
	Long brokerId;
	Float minOriginAmount;
	Float maxOriginAmount;
	Integer alert;
	Boolean onlyDelivery;

}
