package com.financial.exchange.market.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.financial.exchange.market.models.entity.Operation;
import com.financial.exchange.market.models.entity.OperationStatus;
import com.financial.exchange.market.models.entity.OperationType;

public interface IOperationDao extends JpaRepository<Operation, Long> {

	
	@Query("SELECT o "
			+  "FROM Operation o"
			+  " JOIN o.operationStatus os"
			+  " WHERE os.description IN ('Ingresada','Aceptada','Arribada','Asignada')"
			+  " AND o.state = 1"
			+  " ORDER BY o.priority")
	List<Operation> findAllActives();
	
	@Query(		"SELECT o "
			+ 	"FROM Operation o "
			+ 		"JOIN o.operationStatus os "
			+ 		"JOIN o.operationType ot "
			+ 	"WHERE (os IN (?1)) "
			+ 	"AND (ot IN (?2)) "
			+ 	"AND ((?3 IS NULL) OR (o.state = ?3)) "
			+ 	"AND ((?4 IS NULL) OR (o.broker = ?4)) "
			+   "AND ((?5 IS NULL) OR (o.modifiedDate >= ?5)) "
			+   "AND ((?6 IS NULL) OR (o.modifiedDate <= ?6)) " 
			+	"AND ((?7 IS NULL) OR (o.originAmount >= ?7)) "
			+	"AND ((?8 IS NULL) OR (o.originAmount <= ?8)) "
			+	"AND ((?9 IS NULL) OR (o.alert = ?9)) "
			+	"AND ((?10 IS NULL) OR (o.address = NULL)) "
			+	"AND ((?11 IS NULL) OR (o.address != NULL)) "
			+	"AND ((?12 IS NULL) OR (o.id = ?12)) "
			+ 	"GROUP BY o")
	Page<Operation> findOperationsByVar(
			@Param("estados") List<OperationStatus> estados,
			@Param("tipos") List<OperationType> tipos, 
			@Param("state") Integer state, 
			@Param("idBroker") Long idBroker,
			@Param("desde") Date desde, 
			@Param("hasta") Date hasta,
			@Param("minOriginAmount") Float minOriginAmount, 
			@Param("maxOriginAmount") Float maxoriginAmount,
			@Param("alert") Integer alert,
			@Param("onlyPresencial") Boolean presencialOperation,
			@Param("onlyDelivery") Boolean onlyDelivery,
			@Param("idOperation") Long idOperation,
			Pageable pageable);	

	@Query("SELECT o "
			+  "FROM Operation o"
			+  " JOIN o.broker b"
			+  " WHERE b.id = (?1)"
			+  " AND o.state = 1")
	List<Operation> findByUser(@Param("idUser") Long idUser);

	
	@Query("SELECT o "
			+  "FROM Operation o"
			+  " JOIN o.transactions t"
			+  " JOIN o.operationStatus os "
			+  " JOIN o.operationType ot "
			+  " JOIN t.account ac"
			+  " WHERE ac.id = (?1)"
			+  " AND o.state = 1"
			+ 	"AND (os IN (?2)) "
			+ 	"AND (ot IN (?3)) "
			+ 	"AND ((?4 IS NULL) OR (o.state = ?4)) "
			+ 	"AND ((?5 IS NULL) OR (o.broker = ?5)) "
			+   "AND ((?6 IS NULL) OR (o.modifiedDate >= ?6)) "
			+   "AND ((?7 IS NULL) OR (o.modifiedDate <= ?7)) " 
			+	"AND ((?8 IS NULL) OR (o.originAmount >= ?8)) "
			+	"AND ((?9 IS NULL) OR (o.originAmount <= ?9)) "
			+	"AND ((?10 IS NULL) OR (o.alert = ?10)) "
			+	"AND ((?11 IS NULL) OR (o.id = ?11)) "
			+ 	"GROUP BY o")
	Page<Operation> findOperationsByAccount(
			@Param("idUser") Long idUser,
			@Param("estados") List<OperationStatus> estados,
			@Param("tipos") List<OperationType> tipos, 
			@Param("state") Integer state, 
			@Param("idBroker") Long idBroker,
			@Param("desde") Date desde, 
			@Param("hasta") Date hasta,
			@Param("minOriginAmount") Float minOriginAmount, 
			@Param("maxOriginAmount") Float maxoriginAmount,
			@Param("alert") Integer alert,
			@Param("idOperation") Long idOperation,

			Pageable pageable);

}
