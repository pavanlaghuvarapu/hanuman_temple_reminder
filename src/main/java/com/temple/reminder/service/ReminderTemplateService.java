package com.temple.reminder.service;

import com.temple.reminder.dto.ReminderTemplateDto;
import com.temple.reminder.entity.ReminderTemplate;
import com.temple.reminder.exception.ResourceNotFoundException;
import com.temple.reminder.repository.ReminderTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing WhatsApp reminder templates.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReminderTemplateService {

    private final ReminderTemplateRepository templateRepository;

    /**
     * Gets all reminder templates.
     */
    @Transactional(readOnly = true)
    public List<ReminderTemplateDto> getAllTemplates() {
        return templateRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Gets a template by ID.
     */
    @Transactional(readOnly = true)
    public ReminderTemplateDto getTemplateById(Long id) {
        return templateRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("ReminderTemplate", "id", id));
    }

    /**
     * Gets the currently active template (most recently updated).
     */
    @Transactional(readOnly = true)
    public Optional<ReminderTemplateDto> getActiveTemplate() {
        return templateRepository.findTopByOrderByUpdatedAtDesc()
                .map(this::toDto);
    }

    /**
     * Gets the active template message content.
     * Returns a default message if no template is configured.
     */
    @Transactional(readOnly = true)
    public String getActiveTemplateContent() {
        return templateRepository.findTopByOrderByUpdatedAtDesc()
                .map(ReminderTemplate::getMessageContent)
                .orElse(getDefaultTemplate());
    }

    /**
     * Creates a new reminder template.
     */
    public ReminderTemplateDto createTemplate(ReminderTemplateDto dto) {
        log.debug("Creating new reminder template: {}", dto.getTemplateName());
        ReminderTemplate template = toEntity(dto);
        ReminderTemplate saved = templateRepository.save(template);
        log.info("Created reminder template with id: {}", saved.getId());
        return toDto(saved);
    }

    /**
     * Updates an existing reminder template.
     */
    public ReminderTemplateDto updateTemplate(Long id, ReminderTemplateDto dto) {
        log.debug("Updating reminder template with id: {}", id);
        ReminderTemplate existing = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReminderTemplate", "id", id));

        existing.setTemplateName(dto.getTemplateName());
        existing.setMessageContent(dto.getMessageContent());

        ReminderTemplate updated = templateRepository.save(existing);
        log.info("Updated reminder template with id: {}", updated.getId());
        return toDto(updated);
    }

    /**
     * Deletes a reminder template by ID.
     */
    public void deleteTemplate(Long id) {
        log.debug("Deleting reminder template with id: {}", id);
        if (!templateRepository.existsById(id)) {
            throw new ResourceNotFoundException("ReminderTemplate", "id", id);
        }
        templateRepository.deleteById(id);
        log.info("Deleted reminder template with id: {}", id);
    }

    /**
     * Gets total number of templates.
     */
    @Transactional(readOnly = true)
    public long getTotalTemplates() {
        return templateRepository.count();
    }

    /**
     * Returns a default template when none is configured.
     */
    private String getDefaultTemplate() {
        return "Jai Shri Ram! 🙏\n\nDear {HUSBAND_NAME} ji and {WIFE_NAME} ji,\n\n" +
               "This is a respectful reminder from Hanuman Temple.\n\n" +
               "You are scheduled to perform the special Tuesday Puja on:\n" +
               "📅 Date: {PUJA_DATE}\n" +
               "🔢 Week Number: {WEEK_NUMBER}\n\n" +
               "Please arrive at the temple by 6:00 AM.\n\n" +
               "Jai Bajrang Bali! 🚩\n\n- Temple Administration";
    }

    // ===================== CONVERTERS =====================

    public ReminderTemplateDto toDto(ReminderTemplate template) {
        return ReminderTemplateDto.builder()
                .id(template.getId())
                .templateName(template.getTemplateName())
                .messageContent(template.getMessageContent())
                .updatedAt(template.getUpdatedAt())
                .build();
    }

    private ReminderTemplate toEntity(ReminderTemplateDto dto) {
        return ReminderTemplate.builder()
                .id(dto.getId())
                .templateName(dto.getTemplateName())
                .messageContent(dto.getMessageContent())
                .build();
    }
}
