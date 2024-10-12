package com.startupvalley.shortcut_game.platform;

import com.startupvalley.shortcut_game.exception.ResourceDependencyException;
import com.startupvalley.shortcut_game.exception.ResourceNotFoundException;
import com.startupvalley.shortcut_game.util.FlashMessageUtil;
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
            FlashMessageUtil.addSuccessMessage(redirectAttributes, SAVE_SUCCESS_MESSAGE);
        } catch (Exception ex) {
            FlashMessageUtil.addErrorMessage(redirectAttributes, UNEXPECTED_ERROR_MESSAGE + ex.getMessage());
        }
        return "redirect:/platforms";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Platform platform = platformService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        platformService.getErrorMessageNotFound(id)));

        model.addAttribute("platform", platform);

        return "platforms/form";
    }

    @GetMapping("/delete/{id}")
    public String deletePlatform(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            platformService.deleteById(id);
            FlashMessageUtil.addSuccessMessage(redirectAttributes, DELETE_SUCCESS_MESSAGE);
        } catch (ResourceDependencyException ex) {
            FlashMessageUtil.addErrorMessage(redirectAttributes, platformService.getErrorMessageDependency());
        } catch (ResourceNotFoundException ex) {
            FlashMessageUtil.addErrorMessage(redirectAttributes, platformService.getErrorMessageNotFound(id));
        } catch (Exception ex) {
            FlashMessageUtil.addErrorMessage(redirectAttributes, UNEXPECTED_ERROR_MESSAGE + ex.getMessage());
        }
        return "redirect:/platforms";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException ex, RedirectAttributes redirectAttributes) {
        FlashMessageUtil.addErrorMessage(redirectAttributes, ex.getMessage());
        return "redirect:/platforms";
    }
}
