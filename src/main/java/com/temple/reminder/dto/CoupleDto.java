//package com.temple.reminder.dto;
//
//import jakarta.validation.constraints.*;
//import lombok.*;
//
//import java.time.LocalDateTime;
//
///**
// * Data Transfer Object for Couple entity.
// * Used for form binding and API responses.
// */
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@ToString
//public class CoupleDto {
//
//    private Long id;
//
//    @NotBlank(message = "Husband name is required")
//    @Size(min = 2, max = 100, message = "Husband name must be between 2 and 100 characters")
//    private String husbandName;
//
//    @NotBlank(message = "Wife name is required")
//    @Size(min = 2, max = 100, message = "Wife name must be between 2 and 100 characters")
//    private String wifeName;
//
//    @NotBlank(message = "Mobile number is required")
//    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10-digit Indian mobile number")
//    private String mobileNumber;
//
//    @NotNull(message = "Week number is required")
//    @Min(value = 1, message = "Week number must be at least 1")
//    @Max(value = 52, message = "Week number must not exceed 52")
//    private Integer weekNumber;
//
//    private LocalDateTime createdAt;
//}

package com.temple.reminder.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Couple entity.
 * Supports Indian (10-digit) and International mobile numbers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CoupleDto {

    private Long id;

    @NotBlank(message = "Husband name is required")
    @Size(min = 2, max = 100, message = "Husband name must be between 2 and 100 characters")
    private String husbandName;

    @NotBlank(message = "Wife name is required")
    @Size(min = 2, max = 100, message = "Wife name must be between 2 and 100 characters")
    private String wifeName;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
        regexp = "^(\\+?[1-9]\\d{1,14}|[6-9]\\d{9})$",
        message = "Enter a valid mobile number (Indian 10-digit or international with country code e.g. +14804103707)"
    )
    private String mobileNumber;

    @NotNull(message = "Week number is required")
    @Min(value = 1, message = "Week number must be at least 1")
    @Max(value = 52, message = "Week number must not exceed 52")
    private Integer weekNumber;

    private LocalDateTime createdAt;
}
