package com.startupvalley.shortcut_game.platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/platforms")
public class PlatformController {

    @Autowired
    private PlatformService platformService;

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
    public String savePlatform(@ModelAttribute Platform platform) {
        platformService.save(platform);
        return "redirect:/platforms";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("platform", platformService.findById(id).orElse(new Platform()));
        return "platforms/form";
    }

    @GetMapping("/delete/{id}")
    public String deletePlatform(@PathVariable Long id) {
        platformService.deleteById(id);
        return "redirect:/platforms";
    }
}
