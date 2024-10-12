package com.startupvalley.shortcut_game.category;

import com.startupvalley.shortcut_game.exception.ResourceNotFoundException;
import com.startupvalley.shortcut_game.game.Game;
import com.startupvalley.shortcut_game.util.FlashMessageUtil;
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
            FlashMessageUtil.addSuccessMessage(redirectAttributes, SAVE_SUCCESS_MESSAGE);
        } catch (Exception ex) {
            FlashMessageUtil.addErrorMessage(redirectAttributes, UNEXPECTED_ERROR_MESSAGE + ex.getMessage());
        }
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        categoryService.getErrorMessageNotFound(id)));

        model.addAttribute("category", category);

        return "categories/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            String message = categoryService.deleteById(id); // Capture the message from the service
            FlashMessageUtil.addSuccessMessage(redirectAttributes, DELETE_SUCCESS_MESSAGE + " " + message);
        } catch (ResourceNotFoundException ex) {
            FlashMessageUtil.addErrorMessage(redirectAttributes, categoryService.getErrorMessageNotFound(id));
        } catch (Exception ex) {
            FlashMessageUtil.addErrorMessage(redirectAttributes, UNEXPECTED_ERROR_MESSAGE + ex.getMessage());
        }
        return "redirect:/categories";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException ex, RedirectAttributes redirectAttributes) {
        FlashMessageUtil.addErrorMessage(redirectAttributes, ex.getMessage());
        return "redirect:/categories";
    }
}
