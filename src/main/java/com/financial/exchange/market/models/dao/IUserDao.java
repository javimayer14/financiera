package com.financial.exchange.market.models.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.financial.exchange.market.models.entity.Operation;
import com.financial.exchange.market.models.entity.Role;
import com.financial.exchange.market.models.entity.User;

public interface IUserDao extends JpaRepository<User, Long> {

	User findByUsername(String username);

	@Query("SELECT u " + "FROM User u " + "WHERE u.state = 1")
	List<User> findAllActives();

	@Query("SELECT u " + "FROM User u " + "WHERE :state = NULL OR u.state = 1")
	List<User> findAll(@Param("state") Integer state);

	@Query("SELECT u " + "FROM User u " + "WHERE u.state = 0")
	List<User> findAllInactives();

	@Query("SELECT u " + "FROM User u " + "JOIN u.roles r " + "WHERE (r IN (?1)) "
			+ "AND ((?2 IS NULL) OR (u.username LIKE %?2%)) " + "AND ((?3 IS NULL) OR (u.name LIKE %?3%)) "
			+ "AND ((?4 IS NULL) OR (u.surname LIKE %?4%)) " + "GROUP BY u")
	List<User> findUsersbyMultipleVar(@Param("idRole") List<Role> roles, @Param("userName") String userName,
			@Param("name") String name, @Param("surname") String surname);

	@Query("SELECT c " + "FROM User c " + "WHERE ((?1 IS NULL) " + "		OR (c.name LIKE %?1%)"
			+ "		OR (c.surname LIKE %?1%)" + "		OR (c.username LIKE %?1%)) ")
	Page<User> findUsersbyVar(@Param("userVar") String userVar, Pageable pageable);

	@Query("SELECT o " + "FROM Operation o" + " JOIN o.operationStatus os" + " JOIN o.broker u" + " WHERE u.id = :idUser"
			+ " AND o.state = 1" + " AND os.id IN (3,4,5)" + " ORDER BY o.priority")
	List<Operation> findCurrentOperation(@Param("idUser") Long idUser);

}
