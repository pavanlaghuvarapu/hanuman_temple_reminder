package com.temple.reminder.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity representing a WhatsApp reminder message template.
 * Supports placeholders: {HUSBAND_NAME}, {WIFE_NAME}, {MOBILE_NUMBER}, {WEEK_NUMBER}, {PUJA_DATE}
 */
@Entity
@Table(name = "reminder_templates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ReminderTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Template name is required")
    @Size(min = 3, max = 100, message = "Template name must be between 3 and 100 characters")
    @Column(name = "template_name", nullable = false, length = 100)
    private String templateName;

    @NotBlank(message = "Message content is required")
    @Size(min = 10, message = "Message content must be at least 10 characters")
    @Column(name = "message_content", nullable = false, columnDefinition = "TEXT")
    private String messageContent;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
