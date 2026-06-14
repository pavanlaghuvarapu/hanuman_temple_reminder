package com.temple.reminder.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for ReminderTemplate entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReminderTemplateDto {

    private Long id;

    @NotBlank(message = "Template name is required")
    @Size(min = 3, max = 100, message = "Template name must be between 3 and 100 characters")
    private String templateName;

    @NotBlank(message = "Message content is required")
    @Size(min = 10, message = "Message content must be at least 10 characters")
    private String messageContent;

    private LocalDateTime updatedAt;
}
