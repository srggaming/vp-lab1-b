package mk.ukim.finki.wp.lab.web.controller;

import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.model.Dish;
import mk.ukim.finki.wp.lab.service.ChefService;
import mk.ukim.finki.wp.lab.service.DishService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dishes")
public class DishController {
    private final DishService dishService;
    private final ChefService chefService;

    public DishController(DishService dishService, ChefService chefService) {
        this.dishService = dishService;
        this.chefService = chefService;
    }

    @GetMapping
    public String getDishesPage(@RequestParam(required = false) String error,
                                 @RequestParam(required = false) Integer rating,
                                 Model model) {
        List<Dish> dishes;
        if (rating != null) {
            dishes = this.dishService.findAllByRating(rating);
        } else {
            dishes = this.dishService.listDishes();
        }
        model.addAttribute("dishes", dishes);
        model.addAttribute("error", error);
        model.addAttribute("selectedRating", rating);
        return "listDishes";
    }

    @GetMapping("/dish-form")
    public String getAddDishPage(Model model) {
        model.addAttribute("dish", null);
        List<Chef> chefs = this.chefService.listChefs();
        model.addAttribute("chefs", chefs);
        return "dish-form";
    }

    @GetMapping("/dish-form/{id}")
    public String getEditDishForm(@PathVariable Long id, Model model) {
        try {
            Dish dish = this.dishService.findById(id);
            model.addAttribute("dish", dish);
            List<Chef> chefs = this.chefService.listChefs();
            model.addAttribute("chefs", chefs);
            return "dish-form";
        } catch (RuntimeException e) {
            return "redirect:/dishes?error=DishNotFound";
        }
    }

    @PostMapping("/add")
    public String saveDish(@RequestParam String dishId,
                           @RequestParam String name,
                           @RequestParam String cuisine,
                           @RequestParam int preparationTime,
                           @RequestParam(required = false) Long chefId,
                           @RequestParam(required = false) Integer rating) {
        this.dishService.create(dishId, name, cuisine, preparationTime, chefId, rating);
        return "redirect:/dishes";
    }

    @PostMapping("/edit/{id}")
    public String editDish(@PathVariable Long id,
                           @RequestParam String dishId,
                           @RequestParam String name,
                           @RequestParam String cuisine,
                           @RequestParam int preparationTime,
                           @RequestParam(required = false) Long chefId,
                           @RequestParam(required = false) Integer rating) {
        this.dishService.update(id, dishId, name, cuisine, preparationTime, chefId, rating);
        return "redirect:/dishes";
    }

    @GetMapping("/delete/{id}")
    public String deleteDish(@PathVariable Long id) {
        this.dishService.delete(id);
        return "redirect:/dishes";
    }
}
