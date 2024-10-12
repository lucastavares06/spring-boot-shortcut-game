package com.startupvalley.shortcut_game.category;

import com.startupvalley.shortcut_game.exception.ResourceDependencyException;
import com.startupvalley.shortcut_game.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    private static final String SAVE_SUCCESS_MESSAGE = "Category saved successfully.";
    private static final String DELETE_SUCCESS_MESSAGE = "Category deleted successfully.";
    private static final String UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred: ";

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "categories/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        return "categories/form";
    }

    @PostMapping("/save")
    public String saveCategory(@Valid @ModelAttribute Category category, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "categories/form";
        }
        try {
            categoryService.save(category);
            addFlashMessage(redirectAttributes, true, SAVE_SUCCESS_MESSAGE);
        } catch (Exception ex) {
            addFlashMessage(redirectAttributes, false, UNEXPECTED_ERROR_MESSAGE + ex.getMessage());
        }
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "categories/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            String message = categoryService.deleteById(id); // Capture the message from the service
            addFlashMessage(redirectAttributes, true, DELETE_SUCCESS_MESSAGE + " " + message);
        } catch (ResourceNotFoundException ex) {
            addFlashMessage(redirectAttributes, false, categoryService.getErrorMessageNotFound(id));
        } catch (Exception ex) {
            addFlashMessage(redirectAttributes, false, UNEXPECTED_ERROR_MESSAGE + ex.getMessage());
        }
        return "redirect:/categories";
    }

    private void addFlashMessage(RedirectAttributes redirectAttributes, boolean success, String message) {
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", message);
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", message);
        }
    }
}
