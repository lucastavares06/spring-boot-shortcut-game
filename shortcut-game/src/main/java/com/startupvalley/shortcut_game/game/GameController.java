package com.startupvalley.shortcut_game.game;

import com.startupvalley.shortcut_game.category.CategoryService;
import com.startupvalley.shortcut_game.exception.ResourceNotFoundException;
import com.startupvalley.shortcut_game.platform.Platform;
import com.startupvalley.shortcut_game.platform.PlatformService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PlatformService platformService;

    private static final String SAVE_SUCCESS_MESSAGE = "Game saved successfully.";
    private static final String DELETE_SUCCESS_MESSAGE = "Game deleted successfully.";
    private static final String UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred: ";

    @GetMapping
    public String listGames(Model model) {
        model.addAttribute("games", gameService.findAll());
        return "games/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("game", new Game());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("platforms", platformService.findAll());
        model.addAttribute("selectedPlatformIds", new ArrayList<Long>());
        return "games/form";
    }

    @PostMapping("/save")
    public String saveGame(@Valid @ModelAttribute Game game, RedirectAttributes redirectAttributes) {
        try {
            gameService.save(game);
            addFlashMessage(redirectAttributes, true, SAVE_SUCCESS_MESSAGE);
        } catch (Exception ex) {
            addFlashMessage(redirectAttributes, false, UNEXPECTED_ERROR_MESSAGE + ex.getMessage());
        }
        return "redirect:/games";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Game game = gameService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(gameService.getErrorMessageNotFound(id)));

        model.addAttribute("game", game);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("platforms", platformService.findAll());

        List<Long> selectedPlatformIds = game.getPlatforms().stream()
                .map(Platform::getId)
                .collect(Collectors.toList());
        model.addAttribute("selectedPlatformIds", selectedPlatformIds);

        return "games/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteGame(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            gameService.deleteById(id);
            addFlashMessage(redirectAttributes, true, DELETE_SUCCESS_MESSAGE);
        } catch (ResourceNotFoundException ex) {
            addFlashMessage(redirectAttributes, false, gameService.getErrorMessageNotFound(id));
        } catch (Exception ex) {
            addFlashMessage(redirectAttributes, false, UNEXPECTED_ERROR_MESSAGE + ex.getMessage());
        }
        return "redirect:/games";
    }

    private void addFlashMessage(RedirectAttributes redirectAttributes, boolean success, String message) {
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", message);
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", message);
        }
    }
}
