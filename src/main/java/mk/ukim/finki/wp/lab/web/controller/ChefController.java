package mk.ukim.finki.wp.lab.web.controller;

import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.service.ChefService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chefs")
public class ChefController {
    private final ChefService chefService;

    public ChefController(ChefService chefService) {
        this.chefService = chefService;
    }

    @GetMapping
    public String getChefsPage(@RequestParam(required = false) String error, Model model) {
        List<Chef> chefs = this.chefService.listChefs();
        model.addAttribute("chefs", chefs);
        model.addAttribute("error", error);
        return "listChefsManagement";
    }

    @GetMapping("/chef-form")
    public String getAddChefPage(Model model) {
        model.addAttribute("chef", null);
        return "chef-form";
    }

    @GetMapping("/chef-form/{id}")
    public String getEditChefForm(@PathVariable Long id, Model model) {
        try {
            Chef chef = this.chefService.findById(id);
            model.addAttribute("chef", chef);
            return "chef-form";
        } catch (RuntimeException e) {
            return "redirect:/chefs?error=ChefNotFound";
        }
    }

    @PostMapping("/add")
    public String saveChef(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String bio) {
        this.chefService.create(firstName, lastName, bio);
        return "redirect:/chefs";
    }

    @PostMapping("/edit/{id}")
    public String editChef(@PathVariable Long id,
                           @RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String bio) {
        this.chefService.update(id, firstName, lastName, bio);
        return "redirect:/chefs";
    }

    @GetMapping("/delete/{id}")
    public String deleteChef(@PathVariable Long id) {
        this.chefService.delete(id);
        return "redirect:/chefs";
    }
}
