package mk.ukim.finki.wp.lab.service.impl;

import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.model.Dish;
import mk.ukim.finki.wp.lab.repository.jpa.JpaChefRepository;
import mk.ukim.finki.wp.lab.repository.jpa.JpaDishRepository;
import mk.ukim.finki.wp.lab.service.DishService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    private final JpaDishRepository dishRepository;
    private final JpaChefRepository chefRepository;

    public DishServiceImpl(JpaDishRepository dishRepository, JpaChefRepository chefRepository) {
        this.dishRepository = dishRepository;
        this.chefRepository = chefRepository;
    }

    @Override
    public List<Dish> listDishes() {
        return this.dishRepository.findAll();
    }

    @Override
    public Dish findByDishId(String dishId) {
        return this.dishRepository.findByDishId(dishId);
    }

    @Override
    public Dish findById(Long id) {
        return this.dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found with id: " + id));
    }

    @Override
    public Dish create(String dishId, String name, String cuisine, int preparationTime, Long chefId, Integer rating) {
        Chef chef = null;
        if (chefId != null) {
            chef = chefRepository.findById(chefId).orElse(null);
        }
        Dish dish = new Dish(dishId, name, cuisine, preparationTime, rating);
        dish.setChef(chef);
        return this.dishRepository.save(dish);
    }

    @Override
    public Dish update(Long id, String dishId, String name, String cuisine, int preparationTime, Long chefId, Integer rating) {
        Dish dish = this.findById(id);
        dish.setDishId(dishId);
        dish.setName(name);
        dish.setCuisine(cuisine);
        dish.setPreparationTime(preparationTime);
        dish.setRating(rating);

        if (chefId != null) {
            Chef chef = chefRepository.findById(chefId).orElse(null);
            dish.setChef(chef);
        } else {
            dish.setChef(null);
        }

        return this.dishRepository.save(dish);
    }

    @Override
    public void delete(Long id) {
        this.dishRepository.deleteById(id);
    }

    @Override
    public List<Dish> findAllByChefId(Long chefId) {
        return this.dishRepository.findAllByChef_Id(chefId);
    }

    @Override
    public List<Dish> findAllByRating(Integer rating) {
        return this.dishRepository.findAllByRating(rating);
    }
}
