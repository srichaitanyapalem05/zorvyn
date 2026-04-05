package com.finance.dashboard.service.impl;

import com.finance.dashboard.dto.RecordRequestDTO;
import com.finance.dashboard.dto.RecordResponseDTO;
import com.finance.dashboard.exception.ResourceNotFoundException;
import com.finance.dashboard.mapper.RecordMapper;
import com.finance.dashboard.model.FinancialRecord;
import com.finance.dashboard.model.FinancialRecord.RecordType;
import com.finance.dashboard.model.User;
import com.finance.dashboard.repository.FinancialRecordRepository;
import com.finance.dashboard.repository.UserRepository;
import com.finance.dashboard.service.RecordService;
import com.finance.dashboard.util.RecordSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecordServiceImpl implements RecordService {

    private final FinancialRecordRepository recordRepository;
    private final UserRepository userRepository;
    private final RecordMapper recordMapper;

    @Override
    public RecordResponseDTO createRecord(RecordRequestDTO dto, String creatorEmail) {
        User creator = userRepository.findByEmailAndDeletedFalse(creatorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + creatorEmail));

        FinancialRecord record = FinancialRecord.builder()
                .amount(dto.getAmount())
                .type(dto.getType())
                .category(dto.getCategory())
                .date(dto.getDate())
                .description(dto.getDescription())
                .createdBy(creator)
                .build();

        RecordResponseDTO response = recordMapper.toResponseDTO(recordRepository.save(record));
        log.info("Record created: id={}, type={}, amount={}, by={}", response.getId(), response.getType(), response.getAmount(), creatorEmail);
        return response;
    }

    @Override
    public Page<RecordResponseDTO> getAllRecords(RecordType type, String category,
                                                  LocalDate startDate, LocalDate endDate,
                                                  Pageable pageable) {
        log.info("Fetching records with filters: type={}, category={}, startDate={}, endDate={}", type, category, startDate, endDate);
        Specification<FinancialRecord> spec = RecordSpecification.filter(type, category, startDate, endDate);
        return recordRepository.findAll(spec, pageable).map(recordMapper::toResponseDTO);
    }

    @Override
    public RecordResponseDTO updateRecord(Long id, RecordRequestDTO dto) {
        FinancialRecord record = recordRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));

        record.setAmount(dto.getAmount());
        record.setType(dto.getType());
        record.setCategory(dto.getCategory());
        record.setDate(dto.getDate());
        record.setDescription(dto.getDescription());

        log.info("Record updated: id={}", id);
        return recordMapper.toResponseDTO(recordRepository.save(record));
    }

    @Override
    public void deleteRecord(Long id) {
        FinancialRecord record = recordRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));
        record.setDeleted(true);
        recordRepository.save(record);
        log.info("Record soft-deleted: id={}", id);
    }
}
