package mk.ukim.finki.wp.lab.repository;

import mk.ukim.finki.wp.lab.model.Dish;

import java.util.List;

public interface DishRepository {
    List<Dish> findAll();
    Dish findByDishId(String dishId);
}
