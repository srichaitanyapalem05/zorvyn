package com.finance.dashboard.service.impl;

import com.finance.dashboard.dto.CategorySummaryDTO;
import com.finance.dashboard.dto.DashboardSummaryDTO;
import com.finance.dashboard.dto.MonthlyTrendDTO;
import com.finance.dashboard.dto.RecordResponseDTO;
import com.finance.dashboard.mapper.RecordMapper;
import com.finance.dashboard.repository.FinancialRecordRepository;
import com.finance.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final FinancialRecordRepository recordRepository;
    private final RecordMapper recordMapper;

    @Override
    public DashboardSummaryDTO getSummary() {
        BigDecimal totalIncome = recordRepository.sumTotalIncome();
        BigDecimal totalExpense = recordRepository.sumTotalExpense();
        BigDecimal netBalance = totalIncome.subtract(totalExpense);
        long totalRecords = recordRepository.countByDeletedFalse();

        return DashboardSummaryDTO.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .netBalance(netBalance)
                .totalRecords(totalRecords)
                .build();
    }

    @Override
    public List<CategorySummaryDTO> getCategoryWise() {
        List<Object[]> rows = recordRepository.findCategoryWiseTotals();
        List<CategorySummaryDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            result.add(new CategorySummaryDTO((String) row[0], (BigDecimal) row[1]));
        }
        return result;
    }

    @Override
    public List<MonthlyTrendDTO> getMonthlyTrends() {
        List<Object[]> rows = recordRepository.findMonthlyTrends();
        List<MonthlyTrendDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            result.add(new MonthlyTrendDTO(
                    ((Number) row[0]).intValue(),
                    ((Number) row[1]).intValue(),
                    row[2] != null ? (BigDecimal) row[2] : BigDecimal.ZERO,
                    row[3] != null ? (BigDecimal) row[3] : BigDecimal.ZERO
            ));
        }
        return result;
    }

    @Override
    public List<RecordResponseDTO> getRecentTransactions(int limit) {
        return recordRepository.findAllByDeletedFalseOrderByDateDesc(PageRequest.of(0, limit))
                .stream()
                .map(recordMapper::toResponseDTO)
                .toList();
    }
}
