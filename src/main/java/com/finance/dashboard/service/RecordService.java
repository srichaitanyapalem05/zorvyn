package com.finance.dashboard.service;

import com.finance.dashboard.dto.RecordRequestDTO;
import com.finance.dashboard.dto.RecordResponseDTO;
import com.finance.dashboard.model.FinancialRecord.RecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface RecordService {
    RecordResponseDTO createRecord(RecordRequestDTO dto, String creatorEmail);
    Page<RecordResponseDTO> getAllRecords(RecordType type, String category, LocalDate startDate, LocalDate endDate, Pageable pageable);
    RecordResponseDTO updateRecord(Long id, RecordRequestDTO dto);
    void deleteRecord(Long id);
}
