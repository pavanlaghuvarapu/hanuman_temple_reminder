package com.temple.reminder.controller;

import com.temple.reminder.dto.CoupleDto;
import com.temple.reminder.dto.DashboardDto;
import com.temple.reminder.service.CoupleService;
import com.temple.reminder.service.DashboardService;
import com.temple.reminder.service.ReminderService;
import com.temple.reminder.service.ReminderTemplateService;
import com.temple.reminder.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for WhatsApp Reminder operations.
 */
@Controller
@RequestMapping("/reminders")
@RequiredArgsConstructor
@Slf4j
public class ReminderController {

    private final ReminderService reminderService;
    private final DashboardService dashboardService;
    private final CoupleService coupleService;
    private final ReminderTemplateService templateService;
    private final DateUtil dateUtil;

    /**
     * Show reminders page with upcoming Tuesday couples.
     */
    @GetMapping
    public String remindersPage(Model model) {
        DashboardDto dashboard = dashboardService.getDashboardData();
        boolean hasActiveTemplate = templateService.getActiveTemplate().isPresent();

        model.addAttribute("dashboard", dashboard);
        model.addAttribute("hasActiveTemplate", hasActiveTemplate);
        model.addAttribute("formattedDate",
                dateUtil.formatDisplayDate(dashboard.getUpcomingTuesdayDate()));
        model.addAttribute("pageTitle", "Send Reminders");
        model.addAttribute("activePage", "reminders");
        return "reminders/index";
    }

    /**
     * Generate WhatsApp URL for a couple and redirect.
     * Called via JavaScript in the UI.
     */
    @GetMapping("/whatsapp/{coupleId}")
    @ResponseBody
    public String generateWhatsAppUrl(@PathVariable Long coupleId) {
        log.debug("Generating WhatsApp URL for couple id: {}", coupleId);
        CoupleDto couple = coupleService.getCoupleById(coupleId);
        return reminderService.generateWhatsAppUrl(couple);
    }

    /**
     * Preview the reminder message for a specific couple.
     */
    @GetMapping("/preview/{coupleId}")
    public String previewReminder(@PathVariable Long coupleId, Model model) {
        CoupleDto couple = coupleService.getCoupleById(coupleId);
        String message = reminderService.generatePreviewMessage(couple);
        String whatsAppUrl = reminderService.generateWhatsAppUrl(couple);

        model.addAttribute("couple", couple);
        model.addAttribute("message", message);
        model.addAttribute("whatsAppUrl", whatsAppUrl);
        model.addAttribute("pujaDate", dateUtil.getFormattedUpcomingTuesday());
        model.addAttribute("pageTitle", "Preview Reminder");
        model.addAttribute("activePage", "reminders");
        return "reminders/preview";
    }

    /**
     * Show reminder history / send reminders for a specific week.
     */
    @GetMapping("/week/{weekNumber}")
    public String remindersByWeek(@PathVariable int weekNumber, Model model) {
        var couples = coupleService.getCouplesByWeek(weekNumber);
        model.addAttribute("couples", couples);
        model.addAttribute("weekNumber", weekNumber);
        model.addAttribute("pageTitle", "Week " + weekNumber + " Reminders");
        model.addAttribute("activePage", "reminders");
        return "reminders/week";
    }
}
