package com.startupvalley.shortcut_game.platform;

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
@RequestMapping("/platforms")
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    private static final String SAVE_SUCCESS_MESSAGE = "Platform saved successfully.";
    private static final String DELETE_SUCCESS_MESSAGE = "Platform deleted successfully.";
    private static final String UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred: ";

    @GetMapping
    public String listPlatforms(Model model) {
        model.addAttribute("platforms", platformService.findAll());
        return "platforms/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("platform", new Platform());
        return "platforms/form";
    }

    @PostMapping("/save")
    public String savePlatform(@Valid @ModelAttribute Platform platform, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "platforms/form";
        }
        try {
            platformService.save(platform);
            addFlashMessage(redirectAttributes, true, SAVE_SUCCESS_MESSAGE);
        } catch (Exception ex) {
            addFlashMessage(redirectAttributes, false, UNEXPECTED_ERROR_MESSAGE + ex.getMessage());
        }
        return "redirect:/platforms";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("platform", platformService.findById(id).orElse(new Platform()));
        return "platforms/form";
    }

    @GetMapping("/delete/{id}")
    public String deletePlatform(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            platformService.deleteById(id);
            addFlashMessage(redirectAttributes, true, DELETE_SUCCESS_MESSAGE);
        } catch (ResourceDependencyException ex) {
            addFlashMessage(redirectAttributes, false, platformService.getErrorMessageDependency());
        } catch (ResourceNotFoundException ex) {
            addFlashMessage(redirectAttributes, false, platformService.getErrorMessageNotFound(id));
        } catch (Exception ex) {
            addFlashMessage(redirectAttributes, false, UNEXPECTED_ERROR_MESSAGE + ex.getMessage());
        }
        return "redirect:/platforms";
    }

    private void addFlashMessage(RedirectAttributes redirectAttributes, boolean success, String message) {
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", message);
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", message);
        }
    }
}
