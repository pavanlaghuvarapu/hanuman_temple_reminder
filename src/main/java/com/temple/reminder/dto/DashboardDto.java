package com.temple.reminder.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for Dashboard summary data.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDto {

    private long totalCouples;
    private long totalOccupiedWeeks;
    private long totalAvailableWeeks;
    private LocalDate upcomingTuesdayDate;
    private int upcomingWeekNumber;
    private List<CoupleDto> upcomingCouples;
    private long totalTemplates;
}
