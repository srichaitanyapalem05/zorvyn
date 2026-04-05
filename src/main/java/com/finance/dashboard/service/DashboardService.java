package com.finance.dashboard.service;

import com.finance.dashboard.dto.CategorySummaryDTO;
import com.finance.dashboard.dto.DashboardSummaryDTO;
import com.finance.dashboard.dto.MonthlyTrendDTO;
import com.finance.dashboard.dto.RecordResponseDTO;

import java.util.List;

public interface DashboardService {
    DashboardSummaryDTO getSummary();
    List<CategorySummaryDTO> getCategoryWise();
    List<MonthlyTrendDTO> getMonthlyTrends();
    List<RecordResponseDTO> getRecentTransactions(int limit);
}
