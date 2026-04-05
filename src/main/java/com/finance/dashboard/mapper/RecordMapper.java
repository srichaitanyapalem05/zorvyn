package com.finance.dashboard.mapper;

import com.finance.dashboard.dto.RecordResponseDTO;
import com.finance.dashboard.model.FinancialRecord;
import org.springframework.stereotype.Component;

@Component
public class RecordMapper {

    public RecordResponseDTO toResponseDTO(FinancialRecord record) {
        return RecordResponseDTO.builder()
                .id(record.getId())
                .amount(record.getAmount())
                .type(record.getType())
                .category(record.getCategory())
                .date(record.getDate())
                .description(record.getDescription())
                .createdByName(record.getCreatedBy().getName())
                .build();
    }
}
