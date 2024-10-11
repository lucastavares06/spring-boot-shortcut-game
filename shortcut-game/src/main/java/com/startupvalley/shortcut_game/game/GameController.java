package com.startupvalley.shortcut_game.game;

import com.startupvalley.shortcut_game.category.CategoryService;
import com.startupvalley.shortcut_game.platform.Platform;
import com.startupvalley.shortcut_game.platform.PlatformService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("selectedPlatformIds", new ArrayList<Long>()); // Inicializa como lista vazia
        return "games/form";
    }

    @PostMapping("/save")
    public String saveGame(@ModelAttribute Game game) {
        gameService.save(game);
        return "redirect:/games";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Game game = gameService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game not found"));

        model.addAttribute("game", game);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("platforms", platformService.findAll());

        // Adiciona a lista de IDs das plataformas selecionadas
        List<Long> selectedPlatformIds = game.getPlatforms().stream()
                .map(Platform::getId)
                .collect(Collectors.toList());
        model.addAttribute("selectedPlatformIds", selectedPlatformIds);

        return "games/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteGame(@PathVariable Long id) {
        gameService.deleteById(id);
        return "redirect:/games";
    }
}
