package com.financial.exchange.market.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.financial.exchange.market.models.entity.OperationStatus;
import com.financial.exchange.market.models.entity.OperationType;
import com.financial.exchange.market.models.entity.Transaction;

public interface ITransactionDao extends CrudRepository<Transaction, Long> {

	@Query("FROM Transaction t " + "JOIN t.operation o " + "WHERE o.id = :idOperation")
	List<Transaction> findTransactionsByOperationId(@Param("idOperation") Long idOperation);

	@Query("FROM Transaction t "
			+ "	WHERE ((:id IS NULL) OR (t.id = :id)) "
			+ " AND (t.operation.operationType IN (:operationTypes)) "
			+ " AND (t.operation.operationStatus IN (:operationStatus)) "
			+ " AND ((:timeSince IS NULL) OR (t.modifiedDate >= :timeSince)) "
			+ " AND ((:timeUntil IS NULL) OR (t.modifiedDate <= :timeUntil)) "
			+ "	AND ((:accountId IS NULL) OR (t.account.id = :accountId)) "
			+ " AND ((:minAmount IS NULL) OR (t.amount >= :minAmount)) "
			+ " AND ((:maxAmount IS NULL) OR (t.amount <= :maxAmount)) ")
			Page<Transaction> findByVar(@Param("id") Long  id, 
									@Param("operationTypes") List<OperationType> operationTypes, 
									@Param("operationStatus") List<OperationStatus> operationStatus, 
									@Param("timeSince") Date timeSince, 
									@Param("timeUntil") Date timeUntil, 
									@Param("minAmount") Float minAmount, 
									@Param("maxAmount") Float maxAmount,
									@Param("accountId") Long accountId, 
									Pageable pageable);


}
