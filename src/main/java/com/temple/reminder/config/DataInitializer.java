package com.temple.reminder.config;

import com.temple.reminder.entity.ReminderTemplate;
import com.temple.reminder.repository.ReminderTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initializes default data on application startup if not already present.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ReminderTemplateRepository templateRepository;

    @Override
    public void run(String... args) {
        if (templateRepository.count() == 0) {
            log.info("No reminder templates found. Creating default template...");
            ReminderTemplate defaultTemplate = ReminderTemplate.builder()
                    .templateName("Default Puja Reminder")
                    .messageContent(
                        "Jai Shri Ram! 🙏\n\n" +
                        "Dear {HUSBAND_NAME} ji and {WIFE_NAME} ji,\n\n" +
                        "This is a respectful reminder from Hanuman Temple.\n\n" +
                        "You are scheduled to perform the special Tuesday Puja on:\n" +
                        "📅 Date: {PUJA_DATE}\n" +
                        "🔢 Week Number: {WEEK_NUMBER}\n\n" +
                        "Please arrive at the temple by 6:00 AM for the Puja preparations.\n\n" +
                        "For any queries, please contact the temple administration.\n\n" +
                        "Jai Bajrang Bali! 🚩\n\n" +
                        "- Temple Administration"
                    )
                    .build();
            templateRepository.save(defaultTemplate);
            log.info("Default reminder template created successfully.");
        }
    }
}
