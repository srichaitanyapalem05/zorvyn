package com.finance.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MonthlyTrendDTO {
    private int year;
    private int month;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
}
