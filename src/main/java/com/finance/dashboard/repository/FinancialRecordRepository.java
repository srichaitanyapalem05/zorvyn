package com.finance.dashboard.repository;

import com.finance.dashboard.model.FinancialRecord;
import com.finance.dashboard.model.FinancialRecord.RecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long>,
        JpaSpecificationExecutor<FinancialRecord> {

    Optional<FinancialRecord> findByIdAndDeletedFalse(Long id);

    // Total income
    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM FinancialRecord r WHERE r.type = 'INCOME' AND r.deleted = false")
    BigDecimal sumTotalIncome();

    // Total expense
    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM FinancialRecord r WHERE r.type = 'EXPENSE' AND r.deleted = false")
    BigDecimal sumTotalExpense();

    // Category-wise aggregation
    @Query("SELECT r.category, SUM(r.amount) FROM FinancialRecord r WHERE r.deleted = false GROUP BY r.category ORDER BY SUM(r.amount) DESC")
    List<Object[]> findCategoryWiseTotals();

    // Monthly aggregation
    @Query(value = """
        SELECT EXTRACT(YEAR FROM date) AS yr, EXTRACT(MONTH FROM date) AS mo,
               COALESCE(SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END), 0),
               COALESCE(SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END), 0)
        FROM financial_records
        WHERE deleted = false
        GROUP BY yr, mo
        ORDER BY yr, mo
        """, nativeQuery = true)
    List<Object[]> findMonthlyTrends();

    // Recent transactions
    Page<FinancialRecord> findAllByDeletedFalseOrderByDateDesc(Pageable pageable);

    // Count active records
    long countByDeletedFalse();
}
