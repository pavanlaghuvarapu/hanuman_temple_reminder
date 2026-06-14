package com.temple.reminder.service;

import com.temple.reminder.dto.CoupleDto;
import com.temple.reminder.entity.Couple;
import com.temple.reminder.exception.DuplicateEntryException;
import com.temple.reminder.exception.ResourceNotFoundException;
import com.temple.reminder.exception.WeekFullException;
import com.temple.reminder.repository.CoupleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Couple management operations.
 * Handles all business logic for couple CRUD operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CoupleService {

    private static final int MAX_COUPLES_PER_WEEK = 2;

    private final CoupleRepository coupleRepository;

    /**
     * Retrieves all couples with pagination.
     */
    @Transactional(readOnly = true)
    public Page<CoupleDto> getAllCouples(Pageable pageable) {
        log.debug("Fetching all couples with pagination: {}", pageable);
        return coupleRepository.findAllByOrderByWeekNumberAscCreatedAtDesc(pageable)
                .map(this::toDto);
    }

    /**
     * Retrieves all couples (no pagination) for simple lists.
     */
    @Transactional(readOnly = true)
    public List<CoupleDto> getAllCouplesList() {
        return coupleRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Finds a couple by ID.
     */
    @Transactional(readOnly = true)
    public CoupleDto getCoupleById(Long id) {
        log.debug("Fetching couple with id: {}", id);
        return coupleRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Couple", "id", id));
    }

    /**
     * Creates a new couple with validations.
     */
    public CoupleDto createCouple(CoupleDto dto) {
        log.debug("Creating new couple: {}", dto);

        // Validate mobile number uniqueness
        if (coupleRepository.existsByMobileNumber(dto.getMobileNumber())) {
            throw new DuplicateEntryException(
                "A couple with mobile number " + dto.getMobileNumber() + " already exists.");
        }

        // Validate week capacity
        validateWeekCapacity(dto.getWeekNumber(), null);

        Couple couple = toEntity(dto);
        Couple saved = coupleRepository.save(couple);
        log.info("Created couple with id: {}", saved.getId());
        return toDto(saved);
    }

    /**
     * Updates an existing couple.
     */
    public CoupleDto updateCouple(Long id, CoupleDto dto) {
        log.debug("Updating couple with id: {}", id);

        Couple existing = coupleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Couple", "id", id));

        // Validate mobile number uniqueness (excluding current couple)
        if (coupleRepository.existsByMobileNumberAndIdNot(dto.getMobileNumber(), id)) {
            throw new DuplicateEntryException(
                "A couple with mobile number " + dto.getMobileNumber() + " already exists.");
        }

        // Validate week capacity if week number changed
        if (!existing.getWeekNumber().equals(dto.getWeekNumber())) {
            validateWeekCapacity(dto.getWeekNumber(), id);
        }

        existing.setHusbandName(dto.getHusbandName());
        existing.setWifeName(dto.getWifeName());
        existing.setMobileNumber(dto.getMobileNumber());
        existing.setWeekNumber(dto.getWeekNumber());

        Couple updated = coupleRepository.save(existing);
        log.info("Updated couple with id: {}", updated.getId());
        return toDto(updated);
    }

    /**
     * Deletes a couple by ID.
     */
    public void deleteCouple(Long id) {
        log.debug("Deleting couple with id: {}", id);
        if (!coupleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Couple", "id", id);
        }
        coupleRepository.deleteById(id);
        log.info("Deleted couple with id: {}", id);
    }

    /**
     * Gets couples assigned to a specific week.
     */
    @Transactional(readOnly = true)
    public List<CoupleDto> getCouplesByWeek(int weekNumber) {
        log.debug("Fetching couples for week: {}", weekNumber);
        return coupleRepository.findByWeekNumber(weekNumber).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Searches couples by name or mobile number.
     */
    @Transactional(readOnly = true)
    public Page<CoupleDto> searchCouples(String query, Pageable pageable) {
        log.debug("Searching couples with query: {}", query);
        return coupleRepository.searchCouples(query, pageable)
                .map(this::toDto);
    }

    /**
     * Gets total count of couples.
     */
    @Transactional(readOnly = true)
    public long getTotalCouples() {
        return coupleRepository.count();
    }

    /**
     * Gets count of distinct occupied weeks.
     */
    @Transactional(readOnly = true)
    public long getOccupiedWeeksCount() {
        return coupleRepository.countDistinctWeekNumbers();
    }

    /**
     * Gets remaining capacity for a given week.
     */
    @Transactional(readOnly = true)
    public int getRemainingCapacity(int weekNumber) {
        long current = coupleRepository.countByWeekNumber(weekNumber);
        return (int) Math.max(0, MAX_COUPLES_PER_WEEK - current);
    }

    // ===================== PRIVATE HELPERS =====================

    /**
     * Validates that a week has not reached its 2-couple limit.
     */
    private void validateWeekCapacity(int weekNumber, Long excludeId) {
        long count;
        if (excludeId != null) {
            count = coupleRepository.countByWeekNumberAndIdNot(weekNumber, excludeId);
        } else {
            count = coupleRepository.countByWeekNumber(weekNumber);
        }

        if (count >= MAX_COUPLES_PER_WEEK) {
            throw new WeekFullException(weekNumber);
        }
    }

    /**
     * Converts Couple entity to CoupleDto.
     */
    public CoupleDto toDto(Couple couple) {
        return CoupleDto.builder()
                .id(couple.getId())
                .husbandName(couple.getHusbandName())
                .wifeName(couple.getWifeName())
                .mobileNumber(couple.getMobileNumber())
                .weekNumber(couple.getWeekNumber())
                .createdAt(couple.getCreatedAt())
                .build();
    }

    /**
     * Converts CoupleDto to Couple entity.
     */
    private Couple toEntity(CoupleDto dto) {
        return Couple.builder()
                .id(dto.getId())
                .husbandName(dto.getHusbandName())
                .wifeName(dto.getWifeName())
                .mobileNumber(dto.getMobileNumber())
                .weekNumber(dto.getWeekNumber())
                .build();
    }
}
