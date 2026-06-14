package com.temple.reminder.controller;

import com.temple.reminder.dto.CoupleDto;
import com.temple.reminder.exception.DuplicateEntryException;
import com.temple.reminder.exception.WeekFullException;
import com.temple.reminder.service.CoupleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for Couple CRUD operations.
 */
@Controller
@RequestMapping("/couples")
@RequiredArgsConstructor
@Slf4j
public class CoupleController {

    private final CoupleService coupleService;

    private static final int PAGE_SIZE = 10;

    /**
     * List all couples with search and pagination.
     */
    @GetMapping
    public String listCouples(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String search,
            Model model) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<CoupleDto> couplePage;

        if (search != null && !search.trim().isEmpty()) {
            couplePage = coupleService.searchCouples(search.trim(), pageable);
        } else {
            couplePage = coupleService.getAllCouples(pageable);
        }

        model.addAttribute("couples", couplePage);
        model.addAttribute("search", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", couplePage.getTotalPages());
        model.addAttribute("totalElements", couplePage.getTotalElements());
        model.addAttribute("pageTitle", "Manage Couples");
        model.addAttribute("activePage", "couples");
        return "couples/list";
    }

    /**
     * Show add couple form.
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("couple", new CoupleDto());
        model.addAttribute("pageTitle", "Add New Couple");
        model.addAttribute("activePage", "couples");
        model.addAttribute("formAction", "add");
        return "couples/form";
    }

    /**
     * Process add couple form.
     */
    @PostMapping("/add")
    public String addCouple(
            @Valid @ModelAttribute("couple") CoupleDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Add New Couple");
            model.addAttribute("activePage", "couples");
            model.addAttribute("formAction", "add");
            return "couples/form";
        }

        try {
            coupleService.createCouple(dto);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Couple added successfully! 🙏");
            return "redirect:/couples";
        } catch (DuplicateEntryException | WeekFullException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("pageTitle", "Add New Couple");
            model.addAttribute("activePage", "couples");
            model.addAttribute("formAction", "add");
            return "couples/form";
        }
    }

    /**
     * Show edit couple form.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        CoupleDto couple = coupleService.getCoupleById(id);
        model.addAttribute("couple", couple);
        model.addAttribute("pageTitle", "Edit Couple");
        model.addAttribute("activePage", "couples");
        model.addAttribute("formAction", "edit");
        return "couples/form";
    }

    /**
     * Process edit couple form.
     */
    @PostMapping("/edit/{id}")
    public String editCouple(
            @PathVariable Long id,
            @Valid @ModelAttribute("couple") CoupleDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Couple");
            model.addAttribute("activePage", "couples");
            model.addAttribute("formAction", "edit");
            return "couples/form";
        }

        try {
            coupleService.updateCouple(id, dto);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Couple updated successfully! 🙏");
            return "redirect:/couples";
        } catch (DuplicateEntryException | WeekFullException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("pageTitle", "Edit Couple");
            model.addAttribute("activePage", "couples");
            model.addAttribute("formAction", "edit");
            return "couples/form";
        }
    }

    /**
     * View couple details.
     */
    @GetMapping("/view/{id}")
    public String viewCouple(@PathVariable Long id, Model model) {
        CoupleDto couple = coupleService.getCoupleById(id);
        model.addAttribute("couple", couple);
        model.addAttribute("pageTitle", "Couple Details");
        model.addAttribute("activePage", "couples");
        return "couples/view";
    }

    /**
     * Delete a couple.
     */
    @PostMapping("/delete/{id}")
    public String deleteCouple(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        coupleService.deleteCouple(id);
        redirectAttributes.addFlashAttribute("successMessage",
                "Couple deleted successfully.");
        return "redirect:/couples";
    }
}
