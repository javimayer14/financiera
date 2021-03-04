package com.financial.exchange.market.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.financial.exchange.market.models.entity.Transaction;

public interface ITransactionDao extends CrudRepository<Transaction, Long> {

	@Query("FROM Transaction t " + "JOIN t.operation o " + "WHERE o.id = :idOperation")
	List<Transaction> findTransactionsByOperationId(@Param("idOperation") Long idOperation);
}
