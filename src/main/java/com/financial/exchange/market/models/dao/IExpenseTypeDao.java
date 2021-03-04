package com.financial.exchange.market.models.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.financial.exchange.market.models.entity.ExpenseType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IExpenseTypeDao extends JpaRepository<ExpenseType, Long> {

    @Query("SELECT e " + "FROM ExpenseType e " + "WHERE (?1 IS NULL) OR (e.description LIKE %?1%) " + "AND (e.id <> 8)")
    List<ExpenseType> findByDescription(@Param("description") String description);

    @Query("SELECT e " + "FROM ExpenseType e " + "WHERE (?1 IS NULL) OR (e.description LIKE %?1%) " + "AND (e.id <> 8)")
    Page<ExpenseType> findByParam(@Param("description") String description, Pageable pageable);

}
