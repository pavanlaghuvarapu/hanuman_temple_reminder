package com.temple.reminder.util;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

/**
 * Utility class for date calculations related to Puja scheduling.
 * Week number = which Tuesday of the year (1st Tuesday = Week 1, 2nd = Week 2 etc.)
 */
@Component
public class DateUtil {

    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    private static final DateTimeFormatter SHORT_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Calculates the upcoming Tuesday from today.
     * If today is Tuesday, returns next Tuesday (7 days ahead).
     */
    public LocalDate getUpcomingTuesday() {
        LocalDate today = LocalDate.now();
        if (today.getDayOfWeek() == DayOfWeek.TUESDAY) {
            return today.plusWeeks(1);
        }
        return today.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
    }

    /**
     * Calculates which Tuesday number of the year this date is.
     * Example: 1st Tuesday of year = Week 1, 2nd Tuesday = Week 2 etc.
     * This matches the temple register numbering exactly.
     *
     * @param date the Tuesday date
     * @return Tuesday count (1-52)
     */
    public int getWeekNumber(LocalDate date) {
        // Find the first Tuesday of the same year
        LocalDate firstTuesdayOfYear = LocalDate.of(date.getYear(), 1, 1)
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));

        // Count how many Tuesdays from first Tuesday to this date
        long daysBetween = date.toEpochDay() - firstTuesdayOfYear.toEpochDay();
        int tuesdayNumber = (int)(daysBetween / 7) + 1;

        // Clamp between 1 and 52
        if (tuesdayNumber < 1) tuesdayNumber = 1;
        if (tuesdayNumber > 52) tuesdayNumber = 52;

        return tuesdayNumber;
    }

    /**
     * Formats a date for display in the UI.
     */
    public String formatDisplayDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DISPLAY_FORMAT);
    }

    /**
     * Formats a date in short format.
     */
    public String formatShortDate(LocalDate date) {
        if (date == null) return "";
        return date.format(SHORT_FORMAT);
    }

    /**
     * Gets the upcoming Tuesday formatted for WhatsApp message.
     */
    public String getFormattedUpcomingTuesday() {
        return formatDisplayDate(getUpcomingTuesday());
    }
}