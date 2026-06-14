package com.temple.reminder.service;

import com.temple.reminder.dto.CoupleDto;
import com.temple.reminder.util.DateUtil;
import com.temple.reminder.util.WhatsAppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service for generating WhatsApp reminder messages.
 * Handles template processing and WhatsApp URL generation.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderService {

    private final ReminderTemplateService templateService;
    private final DateUtil dateUtil;
    private final WhatsAppUtil whatsAppUtil;

    /**
     * Generates a WhatsApp URL for a specific couple.
     *
     * @param couple the couple to send reminder to
     * @return WhatsApp URL with pre-filled message
     */
    public String generateWhatsAppUrl(CoupleDto couple) {
        log.debug("Generating WhatsApp URL for couple: {} & {}",
                couple.getHusbandName(), couple.getWifeName());

        // Get active template
        String templateContent = templateService.getActiveTemplateContent();

        // Get upcoming Tuesday date for the message
        String pujaDate = dateUtil.getFormattedUpcomingTuesday();

        // Replace placeholders in template
        String message = whatsAppUtil.replacePlaceholders(
                templateContent,
                couple.getHusbandName(),
                couple.getWifeName(),
                couple.getMobileNumber(),
                couple.getWeekNumber(),
                pujaDate
        );

        // Generate WhatsApp URL
        String url = whatsAppUtil.generateWhatsAppUrl(couple.getMobileNumber(), message);
        log.debug("Generated WhatsApp URL for mobile: {}", couple.getMobileNumber());
        return url;
    }

    /**
     * Generates a preview of the message for a couple (without URL encoding).
     *
     * @param couple the couple for preview
     * @return raw message text
     */
    public String generatePreviewMessage(CoupleDto couple) {
        String templateContent = templateService.getActiveTemplateContent();
        String pujaDate = dateUtil.getFormattedUpcomingTuesday();

        return whatsAppUtil.replacePlaceholders(
                templateContent,
                couple.getHusbandName(),
                couple.getWifeName(),
                couple.getMobileNumber(),
                couple.getWeekNumber(),
                pujaDate
        );
    }
}
