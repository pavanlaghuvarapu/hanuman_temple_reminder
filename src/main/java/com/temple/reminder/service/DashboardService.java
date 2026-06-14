package com.temple.reminder.service;

import com.temple.reminder.dto.DashboardDto;
import com.temple.reminder.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Service class for dashboard data aggregation.
 * Calculates upcoming Tuesday and gathers dashboard statistics.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DashboardService {

    private final CoupleService coupleService;
    private final ReminderTemplateService templateService;
    private final DateUtil dateUtil;

    /**
     * Builds complete dashboard data including statistics and upcoming puja details.
     *
     * @return DashboardDto with all required data
     */
    public DashboardDto getDashboardData() {
        log.debug("Building dashboard data");

        // Calculate upcoming Tuesday
        LocalDate upcomingTuesday = dateUtil.getUpcomingTuesday();
        int upcomingWeekNumber = dateUtil.getWeekNumber(upcomingTuesday);

        log.debug("Upcoming Tuesday: {} (Week {})", upcomingTuesday, upcomingWeekNumber);

        return DashboardDto.builder()
                .totalCouples(coupleService.getTotalCouples())
                .totalOccupiedWeeks(coupleService.getOccupiedWeeksCount())
                .totalAvailableWeeks(52 - coupleService.getOccupiedWeeksCount())
                .upcomingTuesdayDate(upcomingTuesday)
                .upcomingWeekNumber(upcomingWeekNumber)
                .upcomingCouples(coupleService.getCouplesByWeek(upcomingWeekNumber))
                .totalTemplates(templateService.getTotalTemplates())
                .build();
    }
}
