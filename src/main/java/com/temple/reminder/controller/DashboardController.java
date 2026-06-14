package com.temple.reminder.controller;

import com.temple.reminder.dto.DashboardDto;
import com.temple.reminder.service.DashboardService;
import com.temple.reminder.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for the Admin Dashboard.
 */
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    private final DashboardService dashboardService;
    private final DateUtil dateUtil;

    @GetMapping
    public String dashboard(Model model) {
        log.debug("Loading dashboard");
        DashboardDto data = dashboardService.getDashboardData();
        model.addAttribute("dashboard", data);
        model.addAttribute("formattedDate", dateUtil.formatDisplayDate(data.getUpcomingTuesdayDate()));
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("activePage", "dashboard");
        return "dashboard";
    }
}
