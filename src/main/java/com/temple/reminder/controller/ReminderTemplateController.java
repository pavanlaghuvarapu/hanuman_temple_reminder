package com.temple.reminder.controller;

import com.temple.reminder.dto.ReminderTemplateDto;
import com.temple.reminder.service.ReminderTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for Reminder Template management.
 */
@Controller
@RequestMapping("/templates")
@RequiredArgsConstructor
@Slf4j
public class ReminderTemplateController {

    private final ReminderTemplateService templateService;

    /**
     * List all templates.
     */
    @GetMapping
    public String listTemplates(Model model) {
        List<ReminderTemplateDto> templates = templateService.getAllTemplates();
        model.addAttribute("templates", templates);
        model.addAttribute("pageTitle", "Reminder Templates");
        model.addAttribute("activePage", "templates");
        return "templates/list";
    }

    /**
     * Show create template form.
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        ReminderTemplateDto dto = new ReminderTemplateDto();
        dto.setMessageContent(getDefaultPlaceholderMessage());
        model.addAttribute("template", dto);
        model.addAttribute("pageTitle", "Create Template");
        model.addAttribute("activePage", "templates");
        model.addAttribute("formAction", "add");
        return "templates/form";
    }

    /**
     * Process create template form.
     */
    @PostMapping("/add")
    public String addTemplate(
            @Valid @ModelAttribute("template") ReminderTemplateDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Create Template");
            model.addAttribute("activePage", "templates");
            model.addAttribute("formAction", "add");
            return "templates/form";
        }

        templateService.createTemplate(dto);
        redirectAttributes.addFlashAttribute("successMessage",
                "Template created successfully! 🙏");
        return "redirect:/templates";
    }

    /**
     * Show edit template form.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ReminderTemplateDto template = templateService.getTemplateById(id);
        model.addAttribute("template", template);
        model.addAttribute("pageTitle", "Edit Template");
        model.addAttribute("activePage", "templates");
        model.addAttribute("formAction", "edit");
        return "templates/form";
    }

    /**
     * Process edit template form.
     */
    @PostMapping("/edit/{id}")
    public String editTemplate(
            @PathVariable Long id,
            @Valid @ModelAttribute("template") ReminderTemplateDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Template");
            model.addAttribute("activePage", "templates");
            model.addAttribute("formAction", "edit");
            return "templates/form";
        }

        templateService.updateTemplate(id, dto);
        redirectAttributes.addFlashAttribute("successMessage",
                "Template updated successfully! 🙏");
        return "redirect:/templates";
    }

    /**
     * Preview a template.
     */
    @GetMapping("/preview/{id}")
    public String previewTemplate(@PathVariable Long id, Model model) {
        ReminderTemplateDto template = templateService.getTemplateById(id);
        // Create a sample preview with dummy data
        String preview = template.getMessageContent()
                .replace("{HUSBAND_NAME}", "Ramesh Kumar")
                .replace("{WIFE_NAME}", "Sunita Kumar")
                .replace("{MOBILE_NUMBER}", "9876543210")
                .replace("{WEEK_NUMBER}", "12")
                .replace("{PUJA_DATE}", "15 January 2025");

        model.addAttribute("template", template);
        model.addAttribute("previewText", preview);
        model.addAttribute("pageTitle", "Preview Template");
        model.addAttribute("activePage", "templates");
        return "templates/preview";
    }

    /**
     * Delete a template.
     */
    @PostMapping("/delete/{id}")
    public String deleteTemplate(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        templateService.deleteTemplate(id);
        redirectAttributes.addFlashAttribute("successMessage",
                "Template deleted successfully.");
        return "redirect:/templates";
    }

    private String getDefaultPlaceholderMessage() {
        return "Jai Shri Ram! 🙏\n\nDear {HUSBAND_NAME} ji and {WIFE_NAME} ji,\n\n" +
               "This is a respectful reminder from Hanuman Temple.\n\n" +
               "You are scheduled to perform the special Tuesday Puja on:\n" +
               "📅 Date: {PUJA_DATE}\n" +
               "🔢 Week Number: {WEEK_NUMBER}\n\n" +
               "Please arrive at the temple by 6:00 AM for Puja preparations.\n\n" +
               "For any queries, please contact the temple administration.\n\n" +
               "Jai Bajrang Bali! 🚩\n\n- Temple Administration";
    }
}
