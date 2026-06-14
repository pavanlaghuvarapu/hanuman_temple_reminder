package com.temple.reminder.repository;

import com.temple.reminder.entity.Couple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Couple entity.
 * Provides data access methods for couple management.
 */
@Repository
public interface CoupleRepository extends JpaRepository<Couple, Long> {

    /**
     * Find all couples assigned to a specific week number.
     */
    List<Couple> findByWeekNumber(Integer weekNumber);

    /**
     * Count couples assigned to a specific week number.
     * Used to enforce max 2 couples per week rule.
     */
    long countByWeekNumber(Integer weekNumber);

    /**
     * Find couple by mobile number.
     */
    Optional<Couple> findByMobileNumber(String mobileNumber);

    /**
     * Check if mobile number exists (excluding a specific couple id).
     */
    boolean existsByMobileNumberAndIdNot(String mobileNumber, Long id);

    /**
     * Check if mobile number exists.
     */
    boolean existsByMobileNumber(String mobileNumber);

    /**
     * Check if week is full (has 2 couples), excluding a specific couple.
     */
    long countByWeekNumberAndIdNot(Integer weekNumber, Long id);

    /**
     * Search couples by husband name, wife name, or mobile number.
     */
    @Query("SELECT c FROM Couple c WHERE " +
           "LOWER(c.husbandName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.wifeName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "c.mobileNumber LIKE CONCAT('%', :query, '%')")
    Page<Couple> searchCouples(@Param("query") String query, Pageable pageable);

    /**
     * Get distinct week numbers that have at least one couple.
     */
    @Query("SELECT DISTINCT c.weekNumber FROM Couple c ORDER BY c.weekNumber")
    List<Integer> findDistinctWeekNumbers();

    /**
     * Count distinct week numbers (occupied weeks).
     */
    @Query("SELECT COUNT(DISTINCT c.weekNumber) FROM Couple c")
    long countDistinctWeekNumbers();

    /**
     * Find all couples paginated.
     */
    Page<Couple> findAllByOrderByWeekNumberAscCreatedAtDesc(Pageable pageable);
}
