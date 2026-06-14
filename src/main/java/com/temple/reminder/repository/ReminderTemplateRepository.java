package com.temple.reminder.repository;

import com.temple.reminder.entity.ReminderTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for ReminderTemplate entity.
 */
@Repository
public interface ReminderTemplateRepository extends JpaRepository<ReminderTemplate, Long> {

    /**
     * Find the first/active reminder template.
     * The system uses one active template at a time.
     */
    Optional<ReminderTemplate> findTopByOrderByUpdatedAtDesc();

    /**
     * Find template by name.
     */
    Optional<ReminderTemplate> findByTemplateNameIgnoreCase(String templateName);
}
