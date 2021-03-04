package com.financial.exchange.market.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.financial.exchange.market.models.entity.Expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IExpenseDao extends JpaRepository<Expense, Long> {

        @Query("SELECT e " + "FROM Expense e " + "WHERE (?1 IS NULL) OR (e.expenseType.description LIKE %?1%)")
        List<Expense> findByDescription(@Param("description") String description);

        @Query("SELECT e " + "FROM Expense e " + "WHERE (?1 IS NULL) OR (e.expenseType.description LIKE %?1%)"
                        + "AND (e.expenseType.id <> 8)" + "AND ((?2 IS NULL) OR (e.datePaid > ?2)) "
                        + "AND ((?3 IS NULL) OR (e.datePaid <= ?3)) " + "AND ((?4 IS NULL) OR (e.amount >= ?4)) "
                        + "AND ((?5 IS NULL) OR (e.amount <= ?5)) ")
        Page<Expense> findByParam(@Param("description") String description, @Param("dateSince") Date dateSince,
                        @Param("dateUntil") Date dateUntil, @Param("amountSince") Float amountSince,
                        @Param("amountUntil") Float amountUntil, Pageable pageable);

}
