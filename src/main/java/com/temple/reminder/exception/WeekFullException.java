package com.temple.reminder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a week already has 2 couples assigned.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WeekFullException extends RuntimeException {

    public WeekFullException(int weekNumber) {
        super(String.format("Week %d already has 2 couples assigned. Maximum limit reached.", weekNumber));
    }
}
