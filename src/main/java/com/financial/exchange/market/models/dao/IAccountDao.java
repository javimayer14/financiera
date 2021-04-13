package com.financial.exchange.market.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.financial.exchange.market.models.dto.ClientAccountDTO;
import com.financial.exchange.market.models.entity.Account;

public interface IAccountDao extends CrudRepository<Account, Long> {

	@Query("FROM Account a JOIN a.user u WHERE u.id =:idUser")
	public List<Account> findAllAccountUserById(@Param("idUser") Long idUser);

	@Query("FROM Account a JOIN a.client c WHERE c.id =:idClient")
	public List<Account> findAllAccountClientById(@Param("idClient") Long idClient);
	
	@Query(	  "	FROM Account a "
			+ "		JOIN a.user u "
			+ "		JOIN a.currency c"
			+ "	WHERE u.id =:idUser "
			+ " AND c.id = :idCurency")
	public Account findAccountUserById(@Param("idUser") Long idUser, @Param("idCurency") Long idCurency);
	
	@Query(	  "	FROM Account a "
			+ "		JOIN a.user u "
			+ "		JOIN a.accountType at "
			+ "		JOIN a.currency c"
			+ "	WHERE u.id =:idUser "
			+ " AND c.id = :idCurency "
			+ " AND at.id = :idType")
	public Account findAccountUserById(@Param("idUser") Long idUser, @Param("idCurency") Long idCurency, @Param("idType") Long idType);
	
	@Query(	  "	FROM Account a "
			+ "		JOIN a.client u "
			+ "		JOIN a.currency c"
			+ "	WHERE u.id =:idClient "
			+ " AND c.id = :idCurency")
	public Account findAccountClientById(@Param("idClient") Long idUser, @Param("idCurency") Long idCurency);
	
	@Query(	  "	FROM Account a "
			+ "		JOIN a.client u "
			+ "		JOIN a.accountType at "
			+ "		JOIN a.currency c"
			+ "	WHERE u.id =:idClient "
			+ " AND c.id = :idCurency "
			+ " AND at.id = :idType")
	public Account findAccountClientById(@Param("idClient") Long idUser, @Param("idCurency") Long idCurency,  @Param("idType") Long idType);
	
	@Query(	 
			  "SELECT "
			  + " new com.financial.exchange.market.models.dto.ClientAccountDTO(u.id, u.firstName,u.lastName, a) "
			+ "FROM Account a "
			+ "		JOIN a.client u "
			+ "		JOIN a.accountType at "
			+ "		JOIN a.currency c "
			+ "	WHERE a.balance != 0 "
			+ " AND ((:idCurrency IS NULL) OR (c.id = :idCurrency)) "
			+ " AND at.id = 2")
	List<ClientAccountDTO> findAccountCreditorDebtor(@Param("idCurrency") Long idCurrency);

	
	@Query("FROM Account a JOIN a.user u JOIN a.accountType at WHERE u.id =:idUser AND at.id = 1")
	public List<Account> findAllAccountDeliveryById(@Param("idUser") Long idUser);

}
