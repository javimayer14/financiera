package com.financial.exchange.market.models.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.financial.exchange.market.models.entity.Client;

public interface IClientDao extends CrudRepository<Client, Long> {

	@Query(		"SELECT c "
			+ 	"FROM Client c "
			+ 	"WHERE ((?1 IS NULL) "
			+ "		OR (c.firstName LIKE %?1%)"
			+ "		OR (c.alias LIKE %?1%)"
			+ "		OR (c.lastName LIKE %?1%)) ")
	Page<Client> findClientsbyVar(@Param("clientVar") String clientVar, Pageable pageable);
	
}
