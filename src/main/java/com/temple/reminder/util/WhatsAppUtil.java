//package com.temple.reminder.util;
//
//import org.springframework.stereotype.Component;
//
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//
///**
// * Utility class for generating WhatsApp reminder URLs.
// * Uses wa.me API for semi-automatic WhatsApp messaging.
// */
//@Component
//public class WhatsAppUtil {
//
//    private static final String WHATSAPP_BASE_URL = "https://wa.me/";
//
//    /**
//     * Generates a WhatsApp URL with pre-filled message.
//     *
//     * @param mobileNumber 10-digit Indian mobile number
//     * @param message      the pre-filled message text
//     * @return complete WhatsApp URL
//     */
//    public String generateWhatsAppUrl(String mobileNumber, String message) {
//        // Convert Indian number to international format (+91)
//        String internationalNumber = formatToInternational(mobileNumber);
//
//        // URL encode the message
//        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
//
//        return WHATSAPP_BASE_URL + internationalNumber + "?text=" + encodedMessage;
//    }
//
//    /**
//     * Formats a 10-digit Indian mobile number to international format.
//     * Example: 9876543210 -> 919876543210
//     *
//     * @param mobileNumber 10-digit number
//     * @return international format number
//     */
//    private String formatToInternational(String mobileNumber) {
//        if (mobileNumber == null) return "";
//        // Remove any spaces, dashes, or +91 prefix
//        String cleaned = mobileNumber.replaceAll("[\\s\\-+]", "");
//        if (cleaned.startsWith("91") && cleaned.length() == 12) {
//            return cleaned; // Already has country code
//        }
//        if (cleaned.length() == 10) {
//            return "91" + cleaned; // Add India country code
//        }
//        return cleaned;
//    }
//
//    /**
//     * Replaces placeholders in the message template.
//     *
//     * @param template     message template with placeholders
//     * @param husbandName  husband's name
//     * @param wifeName     wife's name
//     * @param mobileNumber mobile number
//     * @param weekNumber   week number
//     * @param pujaDate     puja date string
//     * @return message with all placeholders replaced
//     */
//    public String replacePlaceholders(String template, String husbandName, String wifeName,
//                                       String mobileNumber, int weekNumber, String pujaDate) {
//        if (template == null) return "";
//        return template
//                .replace("{HUSBAND_NAME}", husbandName != null ? husbandName : "")
//                .replace("{WIFE_NAME}", wifeName != null ? wifeName : "")
//                .replace("{MOBILE_NUMBER}", mobileNumber != null ? mobileNumber : "")
//                .replace("{WEEK_NUMBER}", String.valueOf(weekNumber))
//                .replace("{PUJA_DATE}", pujaDate != null ? pujaDate : "");
//    }
//}

package com.temple.reminder.util;

import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for generating WhatsApp reminder URLs.
 * Supports Indian numbers (10-digit) and International numbers (e.g., US +1).
 */
@Component
public class WhatsAppUtil {

    private static final String WHATSAPP_BASE_URL = "https://wa.me/";

    /**
     * Generates a WhatsApp URL with pre-filled message.
     * Handles both Indian and international numbers automatically.
     *
     * @param mobileNumber Indian 10-digit OR international number (with or without +)
     * @param message      the pre-filled message text
     * @return complete WhatsApp URL
     */
    public String generateWhatsAppUrl(String mobileNumber, String message) {
        String internationalNumber = formatToInternational(mobileNumber);
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        return WHATSAPP_BASE_URL + internationalNumber + "?text=" + encodedMessage;
    }

    /**
     * Converts any mobile number to international format (digits only, no + or spaces).
     *
     * Examples:
     *   9876543210        → 919876543210   (Indian 10-digit → add +91)
     *   +919876543210     → 919876543210   (already international)
     *   +14804103707      → 14804103707    (US number)
     *   14804103707       → 14804103707    (US number without +)
     *   +1 (480) 410-3707 → 14804103707   (US formatted)
     */
    private String formatToInternational(String mobileNumber) {
        if (mobileNumber == null || mobileNumber.isBlank()) return "";

        // Remove all non-digit characters (spaces, dashes, brackets, +)
        String digitsOnly = mobileNumber.replaceAll("[^0-9]", "");

        // Indian 10-digit number starting with 6-9 → add 91
        if (digitsOnly.length() == 10 && digitsOnly.matches("[6-9]\\d{9}")) {
            return "91" + digitsOnly;
        }

        // Already has country code (91 + 10 digits = 12 digits for India)
        if (digitsOnly.length() == 12 && digitsOnly.startsWith("91")) {
            return digitsOnly;
        }

        // US number: 11 digits starting with 1 (1 + 10 digits)
        if (digitsOnly.length() == 11 && digitsOnly.startsWith("1")) {
            return digitsOnly;
        }

        // US number: 10 digits (no country code) — less common but handle it
        if (digitsOnly.length() == 10 && !digitsOnly.matches("[6-9]\\d{9}")) {
            return "1" + digitsOnly; // assume US
        }

        // Any other international number — use as-is (already has country code digits)
        return digitsOnly;
    }

    /**
     * Replaces placeholders in the message template.
     */
    public String replacePlaceholders(String template, String husbandName, String wifeName,
                                       String mobileNumber, int weekNumber, String pujaDate) {
        if (template == null) return "";
        return template
                .replace("{HUSBAND_NAME}", husbandName != null ? husbandName : "")
                .replace("{WIFE_NAME}", wifeName != null ? wifeName : "")
                .replace("{MOBILE_NUMBER}", mobileNumber != null ? mobileNumber : "")
                .replace("{WEEK_NUMBER}", String.valueOf(weekNumber))
                .replace("{PUJA_DATE}", pujaDate != null ? pujaDate : "");
    }

    /**
     * Returns a display-friendly version of the number for UI.
     */
    public String getDisplayNumber(String mobileNumber) {
        if (mobileNumber == null) return "";
        String digits = mobileNumber.replaceAll("[^0-9]", "");
        // Indian number
        if (digits.length() == 10 && digits.matches("[6-9]\\d{9}")) {
            return "+91 " + digits;
        }
        // Already has + prefix stored
        if (mobileNumber.startsWith("+")) {
            return mobileNumber;
        }
        return "+" + digits;
    }
}
