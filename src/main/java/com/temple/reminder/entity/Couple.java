//package com.temple.reminder.entity;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.*;
//import lombok.*;
//
//import java.time.LocalDateTime;
//
///**
// * Entity representing a couple registered for weekly Puja at Hanuman Temple.
// * Each couple is permanently assigned to a specific week number (1-52).
// */
//@Entity
//@Table(name = "couples",
//       uniqueConstraints = {
//           @UniqueConstraint(columnNames = "mobile_number", name = "unique_mobile")
//       })
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString
//@Builder
//public class Couple {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotBlank(message = "Husband name is required")
//    @Size(min = 2, max = 100, message = "Husband name must be between 2 and 100 characters")
//    @Column(name = "husband_name", nullable = false, length = 100)
//    private String husbandName;
//
//    @NotBlank(message = "Wife name is required")
//    @Size(min = 2, max = 100, message = "Wife name must be between 2 and 100 characters")
//    @Column(name = "wife_name", nullable = false, length = 100)
//    private String wifeName;
//
//    @NotBlank(message = "Mobile number is required")
//    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10-digit Indian mobile number")
//    @Column(name = "mobile_number", nullable = false, unique = true, length = 15)
//    private String mobileNumber;
//
//    @NotNull(message = "Week number is required")
//    @Min(value = 1, message = "Week number must be at least 1")
//    @Max(value = 52, message = "Week number must not exceed 52")
//    @Column(name = "week_number", nullable = false)
//    private Integer weekNumber;
//
//    @Column(name = "created_at", nullable = false, updatable = false)
//    private LocalDateTime createdAt;
//
//    @PrePersist
//    protected void onCreate() {
//        this.createdAt = LocalDateTime.now();
//    }
//}


package com.temple.reminder.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity representing a couple registered for weekly Puja at Hanuman Temple.
 * Each couple is permanently assigned to a specific week number (1-52).
 * Mobile number supports Indian (10-digit) and International formats (e.g., +1 US numbers).
 */
@Entity
@Table(name = "couples",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "mobile_number", name = "unique_mobile")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Couple {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Husband name is required")
    @Size(min = 2, max = 100, message = "Husband name must be between 2 and 100 characters")
    @Column(name = "husband_name", nullable = false, length = 100)
    private String husbandName;

    @NotBlank(message = "Wife name is required")
    @Size(min = 2, max = 100, message = "Wife name must be between 2 and 100 characters")
    @Column(name = "wife_name", nullable = false, length = 100)
    private String wifeName;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
        regexp = "^(\\+?[1-9]\\d{1,14}|[6-9]\\d{9})$",
        message = "Enter a valid mobile number (Indian 10-digit or international with country code e.g. +14804103707)"
    )
    @Column(name = "mobile_number", nullable = false, unique = true, length = 20)
    private String mobileNumber;

    @NotNull(message = "Week number is required")
    @Min(value = 1, message = "Week number must be at least 1")
    @Max(value = 52, message = "Week number must not exceed 52")
    @Column(name = "week_number", nullable = false)
    private Integer weekNumber;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
