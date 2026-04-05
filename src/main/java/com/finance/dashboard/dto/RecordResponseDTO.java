package com.finance.dashboard.dto;

import com.finance.dashboard.model.FinancialRecord.RecordType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class RecordResponseDTO {
    private Long id;
    private BigDecimal amount;
    private RecordType type;
    private String category;
    private LocalDate date;
    private String description;
    private String createdByName;
}
