package com.temple.reminder.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the Temple Reminder application.
 * Handles exceptions and returns appropriate error pages.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("errorTitle", "Resource Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", "404");
        return "error/error";
    }

    @ExceptionHandler(DuplicateEntryException.class)
    public String handleDuplicateEntry(DuplicateEntryException ex, Model model) {
        model.addAttribute("errorTitle", "Duplicate Entry");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", "409");
        return "error/error";
    }

    @ExceptionHandler(WeekFullException.class)
    public String handleWeekFull(WeekFullException ex, Model model) {
        model.addAttribute("errorTitle", "Week Limit Reached");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", "400");
        return "error/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("errorTitle", "Unexpected Error");
        model.addAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());
        model.addAttribute("errorCode", "500");
        return "error/error";
    }
}
